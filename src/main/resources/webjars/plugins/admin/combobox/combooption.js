define(function() {
    return {
        props: ['value'],
        template: '<option :value="value"><slot></slot></option>',
        mounted: function () {

        }
    }
});