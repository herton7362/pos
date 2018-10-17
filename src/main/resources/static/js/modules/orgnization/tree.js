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
                    url: utils.patchUrl('/api/member/searchForTree'),
                    data: {
                        sort:'sortNumber,updatedDate',
                        order: 'asc,desc',
                        // memberId:"000000006601505f01660eb749130c8d"
                    },
                    success: function(data) {
                        $.each(data, function () {
                            this.name = this.name || this.loginName;
                            this.icon = utils.patchUrlPrefixUrl('/static/image/person.png')
                            if(!this.fatherId) {
                                this.parent = null;
                            } else {
                                this.parent = self.findParent(data, this.fatherId);
                            }
                        });
                        self.tree.data = data;
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
