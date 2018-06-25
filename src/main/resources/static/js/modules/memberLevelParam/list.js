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
                    {field:'level', title:'级别'},
                    {field:'totalTransactionVolume', title:'总交易量'},
                    {field:'teamScale', title:'团队要求'},
                    {field:'mPosProfit', title:'MPOS收益'},
                    {field:'bigPosProfit', title:'大POS收益'},
                    {field:'allyTitle', title:'盟友称号'},
                    {field:'honor', title:'升级荣誉'},
                    {field:'headquartersSupport', title:'总部支持'}
                ]
            },
            formData: {
                id: null,
                level: null,
                totalTransactionVolume: null,
                teamScale: null,
                mPosProfit: null,
                bigPosProfit: null,
                allyTitle: null,
                honor: null,
                headquartersSupport: null
            }
        },
        methods: {
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
