require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                queryParams: {
                    name: ''
                },
                columns: [
                    {field:'dictionaryCategory.name', title:'分类'},
                    {field:'name', title:'名称'},
                    {field:'code', title:'代码'}
                ]
            },
            dictionaryCategory: {
                selectedId: null,
                data: []
            },
            formData: {
                id: '',
                name: null,
                remark: null,
                points: null,
                price: null,
                dictionaryCategory: {
                    id: null
                }
            }
        },
        methods: {
            modalOpen: function() {
                var $form = this.crudgrid.$instance.getForm();
                if(this.dictionaryCategory.$instance.getSelectedId()) {
                    $form.dictionaryCategory = {
                        id: this.dictionaryCategory.$instance.getSelectedId()
                    }
                } else {
                    $form.dictionaryCategory = {id: null};
                }
            },
            sidebarClick: function(row) {
                this.crudgrid.$instance.load({'dictionaryCategory.id': row.id});
            }
        },
        mounted: function() {
            var self = this;
            $.ajax({
                url: utils.patchUrl('/api/dictionaryCategory'),
                data: {
                    sort: 'sortNumber,updatedDate',
                    order: 'asc,desc'
                },
                success: function(data) {
                    self.dictionaryCategory.selectedId = data.content[0].id;
                    self.dictionaryCategory.data = data.content;
                    self.sidebarClick(data.content[0]);
                }
            })
        }
    });
});