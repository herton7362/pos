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
