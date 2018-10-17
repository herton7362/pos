require(['jquery', 'vue', 'messager', 'utils'], function ($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            tree: {
                $instance: {},
                data: []
            },
            choose: {
                validator: {
                    $instance: {}
                },
                formData: {
                    memberId: null,
                    sns: null,
                    currentMemberId: null
                },
                modal: {
                    $instance: {}
                }
            },
            members: []
        },
        methods: {
            loadSidebar: function (memberId) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/member/searchForTree'),
                    data: {
                        sort: 'sortNumber,updatedDate',
                        order: 'asc,desc',
                        memberId: memberId
                    },
                    success: function (data) {
                        $.each(data, function () {
                            this.name = this.name || this.loginName;
                            this.icon = utils.patchUrlPrefixUrl('/static/image/person.png')
                            if (!this.fatherId) {
                                this.parent = null;
                            } else {
                                this.parent = self.findParent(data, this.fatherId);
                            }
                        });
                        self.tree.data = data;
                        $(self.tree.$instance.$el).jstree({
                            checkbox : {
                                keep_selected_style : false
                            },
                            core: {
                                data: data
                            },
                            plugins : []
                        });
                        $(self.tree.$instance.$el).jstree(true).refresh();
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
                    if (this.id === id) {
                        result = this;
                    }
                });
                return result;
            },
            search: function () {
                this.choose.modal.$instance.open();
            },
            submitChoose: function () {
                var self = this;
                self.loadSidebar(self.choose.formData.memberId);
                self.choose.modal.$instance.close();
                messager.bubble('保存成功！');
            }

        },
        mounted: function () {
            var self = this;
            $.ajax({
                url: utils.patchUrl('/api/member'),
                data: {
                    sort: 'sortNumber',
                    order: 'asc',
                    logicallyDeleted: false
                },
                success: function (data) {
                    $.each(data.content, function () {
                        this.name = this.name + '(' + this.loginName + ')';
                    });
                    self.members = data.content;
                }
            });
            this.loadSidebar()
        }
    });
});
