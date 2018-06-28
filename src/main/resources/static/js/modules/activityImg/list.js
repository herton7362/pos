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
                    {field:'image', title:'图片', formatter: function(value) {
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        }},
                    {field:'link', title:'活动链接地址'},
                    {field:'remark', title:'备注'}
                ]
            },
            formData: {
                id: null,
                image: null,
                link: null,
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
