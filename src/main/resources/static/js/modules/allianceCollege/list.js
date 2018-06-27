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
                    {field:'pdf', title:'pdf', formatter: function(value) {
                            return '<a href="'+utils.patchUrl('/attachment/download/' + value.id)+'">'+value.name+'</a>';
                        }},
                    {field:'remark', title:'备注'}
                ]
            },
            sidebar: {
                $instance: {},
                root: {
                    id: null,
                    name: '所有类别',
                    open: true,
                    alwaysExpended: true
                },
                selectedId: null,
                data: []
            },
            formData: {
                id: null,
                allianceCollegeCategory: {},
                pdf: null,
                remark: null
            },
            allianceCollegeCategory: {
                data: []
            }
        },
        methods: {
            loadSidebar: function(callback) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/allianceCollegeCategory'),
                    data: {
                        sort:'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.sidebar.data = data.content;

                        if(callback instanceof Function) {
                            callback.call(self, data.content);
                        }
                    }
                });
            },
            sidebarClick: function(row) {
                var param = {};
                if(row.id !== null) {
                    param = {'allianceCollegeCategory.id': row.id};
                }
                this.crudgrid.$instance.load(param);
            },
            loadCombobox: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/allianceCollegeCategory'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc',
                        logicallyDeleted:false
                    },
                    success: function(data) {
                        self.allianceCollegeCategory.data = data.content;
                    }
                })
            }
        },
        mounted: function() {
            this.crudgrid.$instance.load();
            this.loadCombobox();
            this.loadSidebar(function() {
                this.sidebarClick(this.sidebar.root);
            });
        }
    });
});
