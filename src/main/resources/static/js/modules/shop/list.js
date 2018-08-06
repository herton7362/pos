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
            loadMember: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/member'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc',
                        logicallyDeleted: false
                    },
                    success: function(data) {
                        self.members = data.content;
                    }
                })
            }
        },
        mounted: function() {
            this.loadMember();
            this.crudgrid.$instance.load();
        }
    });
});
