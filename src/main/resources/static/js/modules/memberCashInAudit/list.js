require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'member.name', title:'会员'},
                    {field:'cashAmount', title:'提现金额'},
                    {field:'collectAccount', title:'收款银行卡卡号'},
                    {field:'collectName', title:'收款人'},
                    {field:'serialNum', title:'流水单号'},
                    {field:'bankName', title:'银行卡银行'},
                    {field:'status', title:'认证状态' , formatter: function(value) {
                        if('PENDING' === value) {
                            return '待审核';
                        } else if('PASS' === value) {
                            return '已通过';
                        } else if('UN_PASS' === value) {
                            return '未通过';
                        }
                        }},
                    {field:'createdDate', title:'申请时间', formatter: function (value) {
                        return new Date(value).format('yyyy-MM-dd HH:mm')
                    }},
                    {field:'reason', title:'提现失败理由'}
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
                        if(row.memberId === this.id)
                            row.member = this;
                    })
                });
                return data;
            },
            choseFile: function () {
                window.location.href = utils.patchUrlPrefixUrl('/memberprofit/exportCashInRecords')
            }
        },
        watch: {
            searchStatus: function (val) {
                this.crudgrid.$instance.load({
                    status: val
                });
            }
        },
        mounted: function() {
            var self = this;
            $.ajax({
                url: utils.patchUrl('/api/member'),
                data: {
                    sort: 'sortNumber,updatedDate',
                    order: 'asc,desc',
                    logicallyDeleted: false
                },
                success: function(data) {
                    $.each(data.content, function () {
                        if(!this.name) {
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
