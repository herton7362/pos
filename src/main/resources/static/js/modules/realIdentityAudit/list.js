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
                    {field:'name', title:'姓名'},
                    {field:'idCardNumber', title:'身份证'},
                    {field:'idCardFront', title:'身份证正面', formatter: function(value) {
                        if(value)
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        else
                            return '';
                        }},
                    {field:'idCardBack', title:'身份证背面', formatter: function(value) {
                        if(value)
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        else
                            return '';
                        }},
                    {field:'status', title:'认证状态' , formatter: function(value) {
                        if('PENDING' === value) {
                            return '待审核';
                        } else if('PASS' === value) {
                            return '已通过';
                        } else if('UN_PASS' === value) {
                            return '未通过';
                        }
                        }},
                    {field:'reason', title:'未通过原因'}
                ]
            },
            formData: {
                id: null,
                name: null,
                idCardNumber: null,
                idCardFront: null,
                idCardBack: null,
                status: null,
                reason: null
            },
            searchStatus: 'PENDING'
        },
        methods: {
        },
        watch: {
            searchStatus: function (val) {
                this.crudgrid.$instance.load({
                    status: val
                });
            }
        },
        mounted: function() {
            this.crudgrid.$instance.load({
                status: 'PENDING'
            });
        }
    });
});
