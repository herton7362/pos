define(['jquery'], function($) {
    return {
        props: {
            title: String,
            menus: Array,
            button: {
                type: Object,
                default: function () {
                    return {
                        text: '取消',
                        callback: function() {
                            this.close();
                        }
                    }
                }
            }
        },
        template: '<div class="actionsheet">\n' +
        '        <div class="weui-mask" style="display: none" @click="close"></div>\n' +
        '        <div class="weui-actionsheet">\n' +
        '            <div class="weui-actionsheet__title">\n' +
        '                <p class="weui-actionsheet__title-text" v-if="title">{{title}}</p>\n' +
        '                <slot name="header"></slot>\n' +
        '            </div>\n' +
        '            <div class="weui-actionsheet__menu">\n' +
        '                <div class="weui-actionsheet__cell" v-for="menu in menus" @click="menu.callback">{{menu.text}}</div>\n' +
        '                <slot name="body"></slot>\n' +
        '            </div>\n' +
        '            <div class="weui-actionsheet__action">\n' +
        '                <div v-if="button" class="weui-actionsheet__cell" @click="buttonClick">{{button.text}}</div>\n' +
        '                <slot name="footer"></slot>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>',
        methods: {
            close: function () {
                $(this.$el).find('.weui-actionsheet').removeClass('weui-actionsheet_toggle');
                $(this.$el).find('.weui-mask').fadeOut(200);
                this.$emit('close', this);
            },
            open: function () {
                $(this.$el).find('.weui-actionsheet').addClass('weui-actionsheet_toggle');
                $(this.$el).find('.weui-mask').fadeIn(200);
                this.$emit('open', this);
            },
            buttonClick: function () {
                this.button.callback.call(this);
            }
        }
    }
});