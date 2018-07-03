define(['jquery', 'vue'], function($, Vue){
    var Messager = {
        $container: $('body'),
        _loading: false,
        vueAlert: null,
        $alert: $("<div id='common_alert' v-cloak>\n" +
            "        <modal :title=\"title\" class='messager' :footer=\"true\" @mounted=\"modalMounted\" type=\"MODAL_SM\">\n" +
            "            <template slot=\"body\">\n" +
            "                <i class=\"fa fa-question-circle alert-icon\"></i>{{msg}}\n" +
            "            </template>\n" +
            "            <template slot=\"footer\">\n" +
            "                <div class=\"row\">\n" +
            "                    <div class=\"col-xs-6\">\n" +
            "                        <button type=\"button\" class=\"btn btn-block btn-default btn-flat\" data-dismiss=\"modal\">关闭</button>\n" +
            "                    </div>\n" +
            "                    <div class=\"col-xs-6\">\n" +
            "                        <button type=\"button\" class=\"btn btn-block btn-primary btn-flat\" @click=\"$root.confirm\" data-dismiss=\"modal\">确定</button>\n" +
            "                    </div>\n" +
            "                </div>" +
            "            </template>\n" +
            "        </modal>\n" +
            "    </div>"),
        $loading: $("<div class=\"am-modal am-modal-loading am-modal-no-btn\" tabindex=\"-1\">\n" +
            "    <div class=\"am-modal-dialog\">\n" +
            "        <div class=\"am-modal-hd\"></div>\n" +
            "        <div class=\"am-modal-bd\">\n" +
            "            <span class=\"am-icon-spinner am-icon-spin\"></span>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</div>"),
        $bubble: $('<div class="bubble-info"></div>'),
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
                    title: '系统提示',
                    callback: function(){}
                },
                methods: {
                    confirm: function() {
                        this.callback();
                    },
                    modalMounted: function(modal) {
                        this.modal = modal;
                    }
                }
            });
        },
        alert: function(msg, event, callback) {
            this.vueAlert.msg = msg;
            var $win = $(window);
            if(event instanceof MouseEvent && $win.width() > 768) {
                var $dialog = $(this.vueAlert.$el).find('.modal-content');

                var width = 300;
                $dialog.css({
                    top: event.clientY,
                    left: event.clientX + width > $win.width()? $win.width() - 320: event.clientX
                });
            }
            if(!event) {
                var $dialog = $(this.vueAlert.$el).find('.modal-dialog');
                $dialog.css({
                    margin: '0 auto',
                    'margin-top': '20%'
                });
            }
            if(typeof event === 'function') {
                callback = event;
            }
            this.vueAlert.callback = callback || function(){};
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
            Messager.$loading.find('.am-modal-hd').html(options.message || '正在载入...');
            Messager.$loading.modal();
            this._loading = true;
        },
        // icon 支持 success，warning，danger，secondary
        bubble: function(options) {
            if(typeof options === 'string') {
                options = {
                    message: arguments[0],
                    icon: arguments[1] || 'info',
                    title: arguments[2]
                }
            }
            options = options || {};
            var $bubble = $('<div class="alert alert-dismissible animation-slide-left" data-am-alert></div>');
            var title = '<h4><i class="icon fa fa-info"></i> '+(options.title || '系统提示')+'</h4>';
            if(options.icon) {
                $bubble.addClass('alert-'+ options.icon);
            }
            $bubble
                .append('<button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>')
                .append(title)
                .append('<p>'+options.message+'</p>')
                .appendTo(Messager.$bubble);

            setTimeout(function() {
                $bubble
                    .addClass('animation-slide-right animation-reverse');
                setTimeout(function() {
                    $bubble
                        .removeClass('animation-slide-right animation-reverse animation-slide-left');
                    $bubble.remove();
                }, 500);
            }, 2000);
        }
    };

    Messager.loading.methods = {
        close: function() {
            Messager.$loading.modal('close');
            Messager._loading = false;
        }
    };

    Messager.init();

    return Messager;
});
