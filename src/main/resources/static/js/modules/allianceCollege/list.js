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
            formData: {
                id: null,
                pdf: null,
                remark: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
