require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'name', title:'名称'},
                    {field:'clientId', title:'client_id'},
                    {field:'clientSecret', title:'client_secret'}
                ]
            },
            formData: {
                id: null,
                name: null,
                clientId: null,
                clientSecret: null,
                scope:null,
                authorizedGrantTypes:null,
                webServerRedirectUri:null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});