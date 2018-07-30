require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    content: ''
                },
                columns: [
                    {field:'wechatSubscription', title:'公众号'},
                    {field:'customerServiceNumber', title:'客服电话'},
                    {field:'contactPhone', title:'联系电话'}
                ]
            },
            formData: {
                id: null,
                wechatSubscription: null,
                customerServiceNumber: null,
                contactPhone: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
