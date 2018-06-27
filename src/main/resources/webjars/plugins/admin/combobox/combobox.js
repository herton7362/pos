define(['jquery', 'utils', 'base-select2'], function($, utils) {
    return {
        props: {
            value: [String, Number, Array],
            multiple: {
                type: Boolean,
                default: false
            },
            valueKey: {
                type: String,
                default: 'id'
            },
            textKey: {
                type: String,
                default: 'name'
            },
            root: {
                type: Object,
                default: function() {
                    if(!this.multiple) {
                        var node = {};
                        node[this.textKey] = "请选择...";
                        node[this.valueKey] = null;
                        return node;
                    }
                    return null;
                }
            },
            options: {
                type: [Array],
                default: Array
            }
        },
        template: '<select :multiple="multiple">' +
        '              <template v-for="row in innerOptions">' +
        '                  <option :value="row[valueKey]" v-html="_printPrefix(row, \'\') + row[textKey]"></option>' +
        '              </template>' +
        '              <slot></slot>' +
        '          </select>',
        methods: {
            _printPrefix: function(row, prefix) {
                if(row.parent) {
                    return this._printPrefix(row.parent, prefix + '&nbsp;&nbsp;&nbsp;&nbsp;');
                }
                return prefix;
            },
            _createRootAndSortOptions: function() {
                var array = [];
                var roots = utils.treeDataConverter(this.options);
                if(this.root)
                    array.push(this.root);
                this._sortOptions(roots, array);
                return array;
            },
            _sortOptions: function(rootOptions, array) {
                var self = this;
                $.each(rootOptions, function() {
                    array.push(this);
                    if(this.children) {
                        self._sortOptions(this.children, array);
                    }
                });
            }
        },
        data: function() {
            return {
                innerOptions: this._createRootAndSortOptions(this.options)
            }
        },
        watch: {
            value: function(val) {
                this.muteEvent = true;
                $(this.$el).select2("val", [val]);
            },
            options: function(val) {
                this.innerOptions = this._createRootAndSortOptions(val);
            }
        },
        mounted: function () {
            var self = this;
            $(this.$el).select2({
                width: '100%',
                allowClear: false,
                formatNoMatches: function() {
                    return "没有选项";
                },
                templateSelection: function(val) {
                    return $.trim(val.text);
                }
            });

            $(this.$el).on('change', function() {
                if(self.muteEvent) {
                    self.muteEvent = false;
                    return;
                }
                var val = self.value;
                if(self.multiple) {
                    val = val || [];
                    val.splice(0);
                    val.push.apply(val, $(self.$el).select2("val") || []);
                } else {
                    val = this.value;
                }
                self.$emit('input', val);
            });

            this.$emit('mounted', this);
        }
    };
});