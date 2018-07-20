require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    organizationNo: '',
                    organizationName: '',
                    userNo: '',
                    userName: '',
                    sn: '',
                    transactionType: '',
                    operateTransactionId: '',
                    profitType: '2'
                },
                columns: [
                    {field:'organizationNo', title:'机构编号'},
                    {field:'organizationName', title:'机构名称'},
                    {field:'userNo', title:'用户编号'},
                    {field:'userName', title:'用户名称'},
                    {field:'transactionAmount', title:'交易金额'},
                    {field:'sn', title:'机器码'},
                    {field:'transactionType', title:'交易类型', formatter: function (value) {
                            if(1 == value) {
                                return '返现奖';
                            } else if(2 == value) {
                                return '直营奖';
                            } else if(3 == value) {
                                return '管理奖';
                            } else if(4 == value) {
                                return '团建奖';
                            }
                            return '未知' + value;
                        }},
                    {field:'profit', title:'收益'},
                    {field:'transactionDate', title:'交易日期', formatter: function (value) {
                            return new Date(value).format('yyyy-MM-dd HH:mm')
                        }},
                    {field:'operateTransactionId', title:'批次号'}
                ]
            }
        },
        methods: {
            search: function () {
                this.crudgrid.$instance.load(this.crudgrid.queryParams);
            }
        },
        mounted: function() {
           this.search();
        }
    });
});
