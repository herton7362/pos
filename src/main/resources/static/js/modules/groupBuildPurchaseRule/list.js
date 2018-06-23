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
                    {field:'minPurchaseCount', title:'最低采购数'},
                    {field:'remark', title:'备注'}
                ]
            },
            formData: {
                id: null,
                minPurchaseCount: null,
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
