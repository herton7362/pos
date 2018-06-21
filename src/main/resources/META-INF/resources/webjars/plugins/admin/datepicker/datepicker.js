define(['jquery', 'datepicker-cn', 'utils'], function($) {
    return {
        props: {
            value: [String, Number]
        },
        template: '<input type="text" class="form-control" :value="formattedDate">',
        methods: {
        },
        watch: {
            value: function(val) {
                if(val) {
                    this.formattedDate = new Date(val).format('yyyy-MM-dd');
                } else {
                    this.formattedDate = null;
                }
            }
        },
        data: function() {
            return {
                formattedDate: null
            }
        },
        mounted: function () {
            var self = this;
            $(this.$el).datepicker({
                language: 'zh-CN',
                format: 'yyyy-mm-dd',
                autoclose: true
            });
            $(this.$el).on('change', function() {
                self.$emit('input', new Date(this.value).getTime());
            });
            this.$emit('mounted', this);
        }
    };
});