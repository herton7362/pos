require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                columns: [
                    {field:'name', title:'名称'}
                ]
            },
            productStandards: [],
            sidebar: {
                root: {
                    id: 'isNull',
                    name: '所有类别',
                    open: true,
                    alwaysExpended: true
                }
            },
            parent: {
                data: []
            },
            formData: {
                id: null,
                parent: {},
                name: null
            }
        },
        methods: {
            refresh: function () {
                this.loadCombobox();
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
                        self.parent.data = data.content;
                    }
                })
            }
        },
        mounted: function() {
            this.loadCombobox();
        }
    });
});
