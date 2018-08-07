require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            tree: {
                $instance: {},
                data: []
            }
        },
        methods: {
            loadSidebar: function() {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/member'),
                    data: {
                        sort:'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function(data) {
                        $.each(data.content, function () {
                            this.name = this.name || this.loginName;
                            this.icon = utils.patchUrlPrefixUrl('/static/image/person.png')
                            if(!this.fatherId) {
                                this.parent = null;
                            } else {
                                this.parent = self.findParent(data.content, this.fatherId);
                            }
                        });
                        self.tree.data = data.content;
                    }
                });
            },
            closeAll: function () {
                this.tree.$instance.closeAll();
            },
            openAll: function () {
                this.tree.$instance.openAll();
            },
            findParent: function (content, id) {
                var result = null;
                $.each(content, function () {
                    if(this.id === id) {
                        result = this;
                    }
                });
                return result;
            }
        },
        mounted: function() {
            this.loadSidebar()
        }
    });
});
