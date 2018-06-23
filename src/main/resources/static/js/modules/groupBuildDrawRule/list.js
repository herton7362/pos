require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                queryParams: {
                    remark: ''
                },
                columns: [
                    {field:'memberCount', title:'拉人数量', formatter: function(value) {
                            return '拉人数量大于 '+ value;
                        }},
                    {field:'reward', title:'奖励'},
                    {field:'remark', title:'备注'}
                ]
            },
            formData: {
                id: null,
                memberCount: null,
                reward: null,
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
