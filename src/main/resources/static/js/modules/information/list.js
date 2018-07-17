require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    content: ''
                },
                columns: [
                    {field:'title', title:'标题', width: '300px'},
                    {field:'category', title:'类别', width: '100px'},
                    {field:'content', title:'内容', formatter: function(value) {
                        if(value.length > 50) {
                            return '<span title="'+value+'">' + value.substring(0, 40) + '...</span>';
                        } else {
                            return value;
                        }
                        }}
                ]
            },
            formData: {
                id: null,
                title: null,
                category: null,
                content: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
