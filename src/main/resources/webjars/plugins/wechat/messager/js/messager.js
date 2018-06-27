define(['jquery', 'vue'], function($, Vue){
    var Messager = {
        $container: $('body'),
        _loading: false,
        vueAlert: null,
        $alert: $("<div id='common_alert' v-cloak>\n" +
            "        <modal :title=\"null\" @mounted=\"modalMounted\">\n" +
            "            <template slot=\"body\">\n" +
            "                {{msg}}\n" +
            "            </template>\n" +
            "            <template slot=\"footer\">\n" +
            "               <a href=\"javascript:void(0);\" type=\"button\" class=\"weui-dialog__btn weui-dialog__btn_default\" @click=\"$root.close\">关闭</a>\n" +
            "               <a href=\"javascript:void(0);\" type=\"button\" class=\"weui-dialog__btn weui-dialog__btn_primary\" @click=\"$root.confirm\">确定</a>\n" +
            "            </template>\n" +
            "        </modal>\n" +
            "    </div>"),
        $loading: $("<div style=\"display: none;\">\n" +
            "        <div class=\"weui-mask_transparent\"></div>\n" +
            "        <div class=\"weui-toast\">\n" +
            "            <i class=\"weui-loading weui-icon_toast\"></i>\n" +
            "            <p class=\"weui-toast__content\">数据加载中</p>\n" +
            "        </div>\n" +
            "    </div>"),
        $bubble: $('<div style="display: none;">\n' +
            '        <div class="weui-mask_transparent"></div>\n' +
            '        <div class="weui-toast" style="display: none;">\n' +
            '            <i class="weui-icon_toast"></i>\n' +
            '            <p class="weui-toast__content">已完成</p>\n' +
            '        </div>\n' +
            '    </div>'),
        ok: function(){},
        cancel: function(){},
        init: function() {
            Messager.$container.append(Messager.$loading);
            Messager.$container.append(Messager.$bubble);
            Messager.$container.append(Messager.$alert);
            this.alertInit();
        },
        alertInit: function() {
            this.vueAlert = new Vue({
                el: '#common_alert',
                data: {
                    msg: '',
                    modal: {},
                    ok: function(){},
                    cancel: function () {}
                },
                methods: {
                    confirm: function() {
                        this.ok();
                        this.modal.close();
                    },
                    close: function() {
                        this.cancel();
                        this.modal.close();
                    },
                    modalMounted: function(modal) {
                        this.modal = modal;
                    }
                }
            });
        },
        alert: function(msg, ok, cancel) {
            this.vueAlert.msg = msg;
            this.vueAlert.ok = ok || function(){};
            this.vueAlert.cancel = cancel || function(){};
            this.vueAlert.modal.open();
        },
        loading: function(options, param) {
            if (typeof options === 'string'){
                Messager.loading.methods[options](this, param);
                return;
            }
            options = options || {};
            if(this._loading) {
                return;
            }
            Messager.$loading.find('.weui-toast__content').html(options.message || '正在载入...');
            Messager.$loading.show();
            Messager.$loading.find('.weui-toast').fadeIn(100);
            this._loading = true;
        },
        // icon 支持 success，warning，danger，secondary
        bubble: function(message, icon) {
            icon = icon || 'info';
            if(icon === 'info') {
                Messager.$bubble.find('.weui-icon_toast')
                    .removeClass('weui-icon-success-no-circle')
                    .removeClass('fa fa-exclamation')
                    .removeClass('fa fa-close')
                    .addClass('weui-icon-info-circle');
            } else if(icon === 'success') {
                Messager.$bubble.find('.weui-icon_toast')
                    .removeClass('weui-icon-info-circle')
                    .removeClass('fa fa-exclamation')
                    .removeClass('fa fa-close')
                    .addClass('weui-icon-success-no-circle');
            } else if(icon === 'danger') {
                Messager.$bubble.find('.weui-icon_toast')
                    .removeClass('weui-icon-info-circle')
                    .removeClass('fa fa-exclamation')
                    .removeClass('weui-icon-success-no-circle')
                    .addClass('fa fa-close');
            } else if(icon === 'warning') {
                Messager.$bubble.find('.weui-icon_toast')
                    .removeClass('weui-icon-info-circle')
                    .removeClass('fa fa-exclamation')
                    .removeClass('weui-icon-success-no-circle')
                    .addClass('fa fa-exclamation');
            }
            Messager.$bubble.find('.weui-toast__content').html(message);
            if (Messager.$bubble.css('display') !== 'none') return;
            Messager.$bubble.show();
            Messager.$bubble.find('.weui-toast').fadeIn(100);
            setTimeout(function () {
                Messager.$bubble.find('.weui-toast').fadeOut(100);
                setTimeout(function () {
                    Messager.$bubble.hide();
                }, 100);
            }, 1000);
        }
    };

    Messager.loading.methods = {
        close: function() {
            Messager.$loading.find('.weui-toast').fadeOut(100);
            setTimeout(function () {
                Messager.$loading.hide();
            }, 100);
            Messager._loading = false;
        }
    };

    Messager.init();

    return Messager;
});