define(['jquery', 'utils'], function($, utils){
    return {
        props: {
            data: Array,
            selectedId: String,
            open: {
                type: Boolean,
                default: true
            },
            root: Object,
            isRoot: {
                type: Boolean,
                default: true
            }
        },
        template: '<ul :data-widget="isRoot?\'tree\':\'\'" :class="isRoot?\'sidebar-plugin tree\':\'treeview-menu\'" :style="open?\'display:block\':\'display:none\'">\n' +
        '              <li v-for="row in innerData" :class="{\'treeview\':!row.alwaysExpended && row.children && row.children.length > 0, \'active\': isActive(row), \'menu-open\': row.open}">\n' +
        '                  <a href="javascript:;" @click="_clickRow(row)">\n' +
        '                      <i :class="row.icon?row.icon:\'fa fa-circle-o\'"></i>\n' +
        '                      <span>{{row.name}}</span>\n' +
        '                      <span class="pull-right-container">\n' +
        '                          <i class="fa fa-angle-left pull-right" v-if="row.children && row.children.length > 0"></i>\n' +
        '                      </span>\n' +
        '                  </a>\n' +
        '                  <sidebar :data="row.children" :is-root="false" :open="!!row.open"></sidebar>\n' +
        '              </li>\n' +
        '          </ul>',
        data: function() {
            return {
                innerData: this._convert(this.data),
                innerSelectedId: this.selectedId
            };
        },
        watch: {
            data: function(val) {
                this.innerData = this._convert(val);
            },
            selectedId: function(val) {
                this.innerSelectedId = val;
            }
        },
        methods: {
            findRoot: function(obj) {
                if(obj.isRoot) {
                    return obj;
                } else {
                    return this.findRoot(obj.$parent)
                }
            },
            _convert: function(data) {
                if(this.isRoot) {
                    var roots = utils.treeDataConverter(data);
                    if(this.root) {
                        this.root.children = roots;
                        roots = [this.root];
                    }
                    return roots;
                }
                return data;
            },
            getData: function() {
                return this.innerData;
            },
            isActive: function(row) {
                return row.id === this.findRoot(this).innerSelectedId;
            },
            getSelectedId: function() {
                return this.findRoot(this).innerSelectedId;
            },
            _clickRow: function(row) {
                var root = this.findRoot(this);
                root.innerSelectedId = row.id;
                // 触发根节点的事件
                root.$emit('click-row', row);
            }
        },
        mounted: function () {
            this.$emit('mounted', this);
            $(window).trigger('load');
        }
    };
});