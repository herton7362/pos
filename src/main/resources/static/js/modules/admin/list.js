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
                    {field:'loginName', title:'登录名'},
                    {field:'name', title:'姓名'},
                    {field:'roles', title:'角色', formatter: function(value) {
                        var text = [];
                        $.each(value, function() {
                            text.push('<span class="label label-success">'+this.name+'</span>')
                        });
                        return text.join(" ");
                    }}
                ]
            },
            role: {
                data: []
            },
            formData: {
                id: null,
                loginName: null,
                name: null,
                password: null,
                setPassword: false,
                roleIds: []
            }
        },
        methods: {
            afterSave: function () {
                this.formData.setPassword = false;
            }
        },
        mounted: function() {
            var self = this;
            this.crudgrid.$instance.load();
            $.ajax({
                url: utils.patchUrl('/api/role'),
                data: {
                    sort: 'sortNumber,updatedDate',
                    order: 'asc,desc'
                },
                success: function(data) {
                    self.role.data = data.content;
                }
            })
        }
    });
});