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
                    {field:'name', title:'姓名'},
                    {field:'sn', title:'sn码'},
                    {field:'member.name', title:'会员'},
                    {field:'mobile', title:'手机'},
                    {field:'transactionAmount', title:'交易总额'},
                    {field:'status', title:'激活状态', formatter: function(value) {
                            return 'ACTIVE' === value ? '已激活' : '未激活';
                        }}
                ]
            },
            formData: {
                id: null,
                name: null,
                sn: null,
                mobile: null,
                transactionAmount: null,
                status: null,
                memberId: null
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
