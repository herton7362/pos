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
                        var length = data.length-1;
                        $("#search").html("当前总人数"+data.length);
                        $(self.tree.$instance.$el).jstree("destroy");
                        $(self.tree.$instance.$el).jstree({
                            checkbox : {
                                keep_selected_style : false
                            },
                            core: {
                                data: self._convertData(data)
                            },
                            plugins : []
                        });
                        $(self.tree.$instance.$el).jstree(true).refresh();
                    }
                });
            },
            _convertData: function (data) {
                var innerData = [];
                $.each(data, function() {
                    innerData.push({
                        id: this.id,
                        text: this.name,
                        icon: this.icon,
                        parent: this.parent? this.parent.id: '#'
                    })
                });
                return innerData;
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
