define(['jquery','icheck', 'utils'], function($) {
    return {
        props: {
            value: null,
            checked: null
        },
        template: '<input type="radio" :value="innerValue" :checked="isChecked()">',
        data: function() {
            return {
                innerValue: this.value,
                innerChecked: this.checked
            }
        },
        watch: {
            checked: function(val) {
                this.innerChecked = val;
                if(this.isChecked()) {
                    $(this.$el).iCheck('check');
                } else {
                    $(this.$el).iCheck('uncheck');
                }
            },
            value: function(val) {
                this.innerValue = val;
            },
            innerValue: function(val) {
                this.$emit('input', val);
            }
        },
        methods: {
            init: function() {
                if(this.isChecked()) {
                    $(this.$el).iCheck('check');
                } else {
                    $(this.$el).iCheck('uncheck');
                }
            },
            isChecked: function() {
                return this.innerChecked === this.innerValue;
            }
        },
        model: {
            prop: 'checked',
            event: 'check'
        },
        mounted: function () {
            var self = this;
            $(this.$el).iCheck({
                radioClass: 'iradio_minimal-blue'
            });
            this.init();

            $(this.$el).on('ifChanged', function(event){
                if(event.target.checked) {
                    if(!self.isChecked()){
                        self.innerChecked = self.innerValue;
                    }
                }
                self.$emit('change', event);
                self.$emit('check', self.innerChecked);
            });
            $(this.$el).on('ifClicked', function(event){
                self.$emit('click', event);
            });
            this.$emit('mounted', this);
        }
    };
});