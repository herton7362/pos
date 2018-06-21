define(['jquery'], function($) {
    return {
        props: {
            title: String,
            footer: {
                type: Boolean,
                default: false
            },
            type: {
                type: String,
                default: ''
            },
            width: {
                type: [String, Number],
                default: '550'
            }
        },
        template: '<div class="sidemodal">' +
        '              <aside class="control-sidebar control-sidebar-light" :style="getStyle()">\n' +
        '                  <div class="box">\n' +
        '                      <div class="box-header with-border">\n' +
        '                          <button type="button" class="close" @click="close">' +
        '                               <span aria-hidden="true">&times;</span>' +
        '                          </button>\n' +
        '                          <h4 v-if="title" class="box-title">{{title}}</h4>\n' +
        '                          <slot name="header"></slot>\n' +
        '                      </div>\n' +
        '                      <div class="box-body">\n' +
        '                          <slot name="body"></slot>\n' +
        '                      </div>\n' +
        '                      <div v-if="footer" class="box-footer">\n' +
        '                          <slot name="footer"></slot>\n' +
        '                      </div>\n' +
        '                  </div>\n' +
        '              </aside>' +
        '              <div class="sidemodal-mask"  @click="close"></div>' +
        '          </div>',
        data: function() {
            return {

            };
        },
        methods: {
            getStyle: function () {
                return 'width: ' + this.width + 'px;right: -' + this.width + 'px';
            },
            open: function() {
                this.$emit('open', this);
                $(this.$el)
                    .addClass('sidemodal-open')
                    .find('.control-sidebar')
                    .addClass('control-sidebar-open');
            },
            close: function() {
                var self = this;
                $(this.$el)
                    .find('.control-sidebar')
                    .removeClass('control-sidebar-open');
                setTimeout(function () {
                    $(self.$el)
                        .removeClass('sidemodal-open')
                }, 300)
            }
        },
        mounted: function () {
            this.$emit('mounted', this);
        }
    };
});