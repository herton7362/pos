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
                    {field:'name', title:'名称'}
                ]
            },
            tree: {
                $instance: {},
                data: [],
                roleId: null,
                roleName: null
            },
            formData: {
                id: null,
                name: null
            }
        },
        methods: {
            loadSidebar: function() {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/module'),
                    data: {
                        sort:'sortNumber,updatedDate',
                        order: 'asc,desc'
                    },
                    success: function(data) {
                        self.tree.data = data.content;
                    }
                });
            },
            getSelectedRows: function () {
                if(this.crudgrid.$instance.getSelectedRows)
                    return this.crudgrid.$instance.getSelectedRows();
                else
                    return [];
            },
            closeAll: function () {
                this.tree.$instance.closeAll();
            },
            openAll: function () {
                this.tree.$instance.openAll();
            },
            checkAll: function () {
                this.tree.$instance.selectAll();
            },
            cancelCheckAll: function () {
                this.tree.$instance.cancelSelectAll();
            },
            loadAuthorizations: function (row) {
                var self = this;
                this.tree.roleId = row.id;
                this.tree.roleName = row.name;
                $.ajax({
                    url: utils.patchUrl('/api/role/authorizations/' + row.id),
                    contentType: 'application/json',
                    success: function(modules) {
                        var moduleIds = [];
                        var parentIds = {};
                        $.each(modules, function() {
                            if(this.parent !== null) {
                                parentIds[this.parent.id] = true;
                            }
                            moduleIds.push(this.id);
                        });
                        $.each(parentIds, function(key) {
                            moduleIds.splice($.inArray(key, moduleIds), 1);
                        });
                        self.tree.$instance.cancelSelectAll();
                        self.tree.$instance.selectNode(moduleIds);
                    }
                });
            },
            authorize: function() {
                $.ajax({
                    url: utils.patchUrl('/api/role/authorize'),
                    contentType: 'application/json',
                    type: 'POST',
                    data: JSON.stringify({
                        roleId: this.tree.roleId,
                        moduleIds: this.tree.$instance.getSelectedIds()
                    }),
                    success: function() {
                        messager.bubble('授权成功。');
                    }
                });
            }
        },
        mounted: function() {
            this.crudgrid.$instance.load();
            this.loadSidebar()
        }
    });
});