require(['jquery', 'vue', 'utils', 'messager'], function($, Vue, utils, messager) {
    var vue = new Vue({
        el: '#content',
        data: {
            columns: [
                {field:'shop.name', title:'激活商户'},
                {field:'memberName', title:'合伙人姓名'},
                {field:'memberMobile', title:'合伙人电话'},
                {field:'activePosSn', title:'激活设备SN'},
                {field:'status', title:'兑换状态', formatter: function(value) {
                    if('EXCHANGING' === value) {
                        return '兑换中';
                    } else if('EXCHANGED' === value) {
                        return '兑换完成';
                    } else if('EXCHANGE_FAIL' === value) {
                        return '兑换失败';
                    }
                    }}
            ],
            datagrid: {
                queryParams: {
                    status: 'EXCHANGING'
                }
            },
            shops: [],
            shopExchangeRecords: []
        },
        methods: {
            loadRecords: function () {
                this.datagrid.$instance.load(this.datagrid.queryParams);
            },
            tableTransformResponse: function(data) {
                var shops = this.shops;
                $.each(data, function (k1, row) {
                    row.shop = {name: '未知'};
                    $.each(shops, function (k2, shop) {
                        if(row.shopId === shop.id) {
                            row.shop = shop;
                        }
                    })
                });
                return data;
            },
            audit: function (row, flag) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/shop/examineExchangeMachine/' + row.id + '/' + flag),
                    type: 'POST',
                    success: function () {
                        messager.bubble("审核成功");
                        self.loadRecords();
                    }
                })
            },
            loadShops: function (callback) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/shop'),
                    success: function (data) {
                        self.shops = data;
                        callback();
                    }
                })
            }
        },
        mounted: function() {
            var self = this;
            this.loadShops(function () {
                self.loadRecords();
            })
        },
        watch: {
            'datagrid.queryParams.status': function () {
                this.loadRecords();
            }
        }
    });
});
