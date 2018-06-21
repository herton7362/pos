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
            }
        },
        template: '<div class="modal fade" role="dialog">\n' +
        '              <div class="modal-dialog" :class="TYPE_ENUM[type]" role="document">\n' +
        '                  <div class="modal-content">\n' +
        '                      <div class="modal-header">\n' +
        '                          <button type="button" class="close" data-dismiss="modal" aria-label="Close">' +
        '                               <span aria-hidden="true">&times;</span>' +
        '                          </button>\n' +
        '                          <h4 v-if="title" class="modal-title text-light-blue">{{title}}</h4>\n' +
        '                          <slot name="header"></slot>\n' +
        '                      </div>\n' +
        '                      <div class="modal-body">\n' +
        '                          <slot name="body"></slot>\n' +
        '                      </div>\n' +
        '                      <div v-if="footer" class="modal-footer">\n' +
        '                          <slot name="footer"></slot>\n' +
        '                      </div>\n' +
        '                  </div>\n' +
        '              </div>\n' +
        '          </div>',
        data: function() {
            return {
                TYPE_ENUM: {
                    DEFAULT: '',
                    MODAL_SM: 'modal-sm',
                    MODAL_LG: 'modal-lg'
                }
            };
        },
        methods: {
            open: function() {
                this.$emit('open', this);
                $(this.$el).modal();
            },
            close: function() {
                $(this.$el).modal('hide');
            }
        },
        mounted: function () {
            this.$emit('mounted', this);
        }
    };
});