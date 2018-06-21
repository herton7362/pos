define(['jquery'], function($) {
    return {
        props: {
            title: String,
            footer: {
                type: Boolean,
                default: false
            }
        },
        template: '<div class="modal js_dialog" style="display: none;">\n' +
        '            <div class="weui-mask"></div>\n' +
        '            <div class="weui-dialog" style="display: none;">\n' +
        '                <div class="weui-dialog__hd" v-if="title"><strong class="weui-dialog__title">{{title}}</strong></div>\n' +
        '                <div class="weui-dialog__bd"><slot name="body"></slot></div>\n' +
        '                <div class="weui-dialog__ft">\n' +
        '                    <slot name="footer"></slot>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>',
        methods: {
            open: function() {
                var $el = this.$el;
                this.$emit('open', this);
                $($el).show();
                setTimeout(function () {
                    $($el).find('.weui-dialog').fadeIn(300);
                }, 100);
            },
            close: function() {
                var $el = this.$el;
                $($el).find('.weui-dialog').fadeOut(300);
                setTimeout(function () {
                    $($el).hide();
                }, 300)
            }
        },
        mounted: function () {
            this.$emit('mounted', this);
        }
    };
});