define([
    'jquery',
    'vue',
    'messager',
    'utils',
    'text!'+_appConf.ctx+'/static/js/modules/icon/icon.html',
    'css!'+_appConf.ctx+'/static/js/modules/icon/icon.css'
], function($, Vue, messager, utils, html) {
    $(html).appendTo('body');
    return new Vue({
        el: '#iconSelector',
        data: {
            iconModal: {
                $instance: {}
            },
            onSelect: function () {
                
            }
        },
        methods: {
            open: function() {
                this.iconModal.$instance.open();
            },
            choseIcon: function (event) {
                var self = this;
                var $element = $(event.target);
                var iconClass = ['fa', 'glyphicon', 'glyphicon-class'];

                $.each(iconClass, function () {
                    if($($element.children().get(0)).hasClass(this)) {
                        self.onSelect($($element.children().get(0)).attr('class'));
                        self.iconModal.$instance.close();
                    } else if($element.hasClass(this)) {
                        self.onSelect($($element.parent().children().get(0)).attr('class'));
                        self.iconModal.$instance.close();
                    }
                });
            }
        },
        mounted: function() {
        }
    });
});