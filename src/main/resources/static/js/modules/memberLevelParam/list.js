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
                    // {field:'teamScale', title:'团队要求'},
                    {field:'mPosProfit', title:'MPOS收益'},
                    // {field:'bigPosProfit', title:'大POS收益'},
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
                // bigPosProfit: null,
                allyTitle: null,
                honor: null,
                headquartersSupport: null
            },
            teamScale: {
                columns: [
                    {field:'title', title:'级别'},
                    {field:'scale', width: '50%', title:'人数', editor: {
                        type: 'number'
                        }}
                ],
                data: []
            }
        },
        methods: {
            onModalOpen: function () {
                var data = this.crudgrid.$instance.form;
                var scales, i, l;
                if(data.teamScale) {
                    scales = data.teamScale.split('|');
                    for (i = 0, l = scales.length; i < l; i ++) {
                        scales[i] = {title: 'v' + (i + 1), scale: scales[i]};
                    }
                } else {
                    scales = [];
                    for (i = 0; i < 12; i ++) {
                        scales.push({title: 'v' + (i + 1), scale: 0});
                    }
                }
                this.teamScale.data = scales;
            }
        },
        watch: {
            'teamScale.data': {
                handler: function (val) {
                    var scales = [];
                    $.each(val, function () {
                        scales.push(this.scale)
                    });
                    this.crudgrid.$instance.form.teamScale = scales.join('|')
                },
                deep: true
            }
        },
        mounted: function() {
            this.crudgrid.$instance.load();
        }
    });
});
