require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    remark: ''
                },
                columns: [
                    {field:'conditionValue', title:'激活奖励金额', formatter: function(value) {
                            return '大于 '+value;
                        }},
                    {field:'awardMoney', title:'激活奖励金额'},
                    {field:'remark', title:'备注'}
                ]
            },
            formData: {
                id: null,
                conditionValue: null,
                awardMoney: null,
                remark: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
