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
                    {field:'member.name', title:'会员'},
                    {field:'mobile', title:'手机'},
                    {field:'content', title:'内容'}
                ]
            },
            formData: {
                id: null,
                memberId: null,
                mobile: null,
                content: null
            },
            members: []
        },
        methods: {
            loadMember: function (fn) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/member'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc',
                        logicallyDeleted: false
                    },
                    success: function(data) {
                        $.each(data.content, function () {
                            if(!this.name)  {
                                this.name = this.loginName;
                            }
                        });
                        self.members = data.content;
                        fn();
                    }
                })
            },
            tableTransformResponse: function (data) {
                var self = this;
                $.each(data, function (k1, v1) {
                    this.member = {name: '未配置'};
                    $.each(self.members, function (k, v) {
                        if(v.id === v1.memberId) {
                            v1.member = v;
                        }
                    })
                });
                return data;
            }
        },
        mounted: function() {
            var self = this;
            this.loadMember(function () {
                self.crudgrid.$instance.load();
            });
        }
    });
});
