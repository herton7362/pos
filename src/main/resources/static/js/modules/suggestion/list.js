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
                    {field:'memberId', title:'会员id'},
                    {field:'mobile', title:'手机'},
                    {field:'content', title:'内容'}
                ]
            },
            formData: {
                id: null,
                memberId: null,
                mobile: null,
                content: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
