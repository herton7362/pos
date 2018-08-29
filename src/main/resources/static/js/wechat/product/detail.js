require([
    'jquery',
    'vue',
    'utils',
    _appConf.ctx + '/static/js/wechat/product/actionsheet/productsheet.js',
    'messager'
], function ($, Vue, utils, productsheet, messager) {
    new Vue({
        el: '#content',
        data: {
            product: {
                coverImage: {}
            }
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrlPrefixUrl('/attachment/download/' + val);
            },
            price: function (val) {
                return utils.formatMoney(val);
            }
        },
        methods: {
            addCart: function () {
                productsheet.open(this.product);
            },
            justBuy: function () {
                productsheet.open(this.product);
            },
            myOrder: function () {
                window.location.href = utils.patchUrl('/wechat/orderform/list')
            }
        },
        mounted: function () {
            var self =this;
            $.ajax({
                url: utils.patchUrl('/product/' + utils.getQueryString('id')),
                success: function(data) {
                    self.product = data;
                }
            })
        }
    });
})