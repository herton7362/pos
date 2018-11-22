require(['jquery', 'vue', 'messager', 'utils'], function ($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    collectName: ''
                },
                columns: [
                    {field: 'member.name', title: '会员'},
                    {field: 'cashAmount', title: '提现金额'},
                    {field: 'collectAccount', title: '收款银行卡卡号'},
                    {field: 'collectName', title: '收款人'},
                    {field: 'serialNum', title: '流水单号'},
                    {field: 'bankName', title: '银行卡银行'},
                    {
                        field: 'status', title: '认证状态', formatter: function (value) {
                            if ('PENDING' === value) {
                                return '待审核';
                            } else if ('PASS' === value) {
                                return '已通过';
                            } else if ('UN_PASS' === value) {
                                return '未通过';
                            }
                        }
                    },
                    {
                        field: 'createdDate', title: '申请时间', formatter: function (value) {
                            return new Date(value).format('yyyy-MM-dd HH:mm')
                        }
                    },
                    {field: 'reason', title: '提现失败理由'},
                    {
                        field: 'payOrderState', title: '转账状态', formatter: function (value) {
                            if ('UN_PAY' === value) {
                                return '未转账';
                            } else if ('SUCCESS' === value) {
                                return '成功';
                            } else if ('IN_PROCESS' === value) {
                                return '处理中';
                            } else if ('FAIL' === value) {
                                return '失败';
                            } else if ('CREATE' === value) {
                                return '已受理';
                            } else if ('REFUSE' === value) {
                                return '拒绝交易';
                            }
                        }
                    },
                    {field: 'payResultCode', title: '支付机构返回码'},
                    {field: 'payResultDes', title: '支付机构返回描述'}
                ]
            },
            formData: {
                id: null,
                memberId: null,
                cashAmount: null,
                collectAccount: null,
                collectName: null,
                serialNum: null,
                bankName: null,
                status: null,
                createdDate: null,
                reason: null
            },
            member: {
                data: []
            },
            searchStatus: 'PENDING'
        },
        methods: {
            tableTransformResponse: function (data) {
                var self = this;
                $.each(data, function (k, row) {
                    $.each(self.member.data, function () {
                        if (row.memberId === this.id)
                            row.member = this;
                    })
                });
                return data;
            },
            choseFile: function () {
                window.location.href = utils.patchUrlPrefixUrl('/memberprofit/exportCashInRecords')
            },
            pay: function (row) {
                messager.alert('确认给该用户转账？', function () {
                    var self = this;
                    $.ajax({
                        url: utils.patchUrl('/api/payHistory/pay'),
                        data: {
                            cashInId: row.id
                        },
                        type: 'GET',
                        success: function () {
                            messager.bubble("转账成功");
                            self.crudgrid.$instance.load({
                                status: 'PASS'
                            });
                        }
                    })
                });
            }
        },
        watch: {
            searchStatus: function (val) {
                this.crudgrid.$instance.load({
                    status: val
                });
            }
        },
        mounted: function () {
            var self = this;
            $.ajax({
                url: utils.patchUrl('/api/member'),
                data: {
                    sort: 'sortNumber,updatedDate',
                    order: 'asc,desc',
                    logicallyDeleted: false
                },
                success: function (data) {
                    $.each(data.content, function () {
                        if (!this.name) {
                            this.name = this.loginName;
                        }
                    });
                    self.member.data = data.content;
                    self.crudgrid.$instance.load({
                        status: 'PENDING'
                    });
                }
            })
        }
    });
});
