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
                    {field:'headPhoto', title:'头像', formatter: function(value) {
                            if(value) {
                                return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                            } else {
                                return '无';
                            }

                        }},
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
                headPhoto: null,
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
                    order: 'asc,desc',
                    logicallyDeleted: false
                },
                success: function(data) {
                    self.role.data = data.content;
                }
            })
        }
    });
});
