define(['jquery', 'jstree'], function($) {
    return {
        props: {
            data: Array,
            checkAble: {
                type: Boolean,
                default: true
            }
        },
        template: '<div></div>',
        methods: {
            _convertData: function (data) {
                var innerData = [];
                $.each(data, function() {
                    innerData.push({
                        id: this.id,
                        text: this.name,
                        icon: this.icon,
                        parent: this.parent? this.parent.id: '#'
                    })
                });
                return innerData;
            },
            openAll: function () {
                $(this.$el).jstree('open_all');
            },
            closeAll: function () {
                $(this.$el).jstree('close_all');
            },
            getSelectedIds: function () {
                return $(this.$el).jstree('get_all_checked');
            },
            selectNode: function(ids) {
                $(this.$el).jstree('select_node', ids);
            },
            selectAll: function () {
                $(this.$el).jstree('select_all');
            },
            cancelSelectAll: function() {
                $(this.$el).jstree('deselect_all');
            }
        },
        data: function() {
            return {
                innerData: this._convertData(this.data)
            }
        },
        watch: {
            data: function(val) {
                this.innerData = this._convertData(val);
                $(this.$el).jstree({
                    checkbox : {
                        keep_selected_style : false
                    },
                    core: {
                        data: this.innerData
                    },
                    plugins : this.checkAble ? [ "checkbox" ] : []
                });
                $(this.$el).jstree(true).get_all_checked = function(full) {
                    var tmp= [];
                    for(var i in this._model.data){
                        if(this.is_undetermined(i)||this.is_checked(i)){tmp.push(full?this._model.data[i]:i);}
                    }
                    return tmp;
                };
            }
        },
        mounted: function () {

        }
    };
});
