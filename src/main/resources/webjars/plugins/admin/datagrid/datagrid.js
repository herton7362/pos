define(['jquery', 'utils'], function($, utils) {
    return {
        props: {
            columns: Array,
            data: Array,
            summary: Function,
            url: String,
            queryParams: {
                type: Object,
                default: Object
            },
            operator: {
                type: Boolean,
                default: false
            },
            checkbox: {
                type: Boolean,
                default: false
            },
            draggable: {
                type: Boolean,
                default: false
            },
            pagination: {
                type: Boolean,
                default: false
            },
            currentPage: {
                type: [Number, String],
                default: 1
            },
            pagerSize: {
                type: [Number, String],
                default: 5
            },
            pageSize: {
                type: [Number, String],
                default: 15
            },
            count: {
                type: [Number, String],
                default: 0
            },
            tableTransformResponse: {
                type: Function,
                default: function(response) {
                    return response
                }
            }
        },
        template: '<div class="dataTables_wrapper">\n' +
        '              <div class="row">\n' +
        '                  <div class="col-sm-12">\n' +
        '                      <table class="table table-bordered table-hover dataTable" role="grid"\n' +
        '                             aria-describedby="example2_info">\n' +
        '                          <thead class="table-header">\n' +
        '                              <tr>\n' +
        '                                  <th v-if="checkbox && checkbox !== \'false\'" style="text-align: center;width: 40px;">' +
        '                                    <checkbox :value="checkAll" v-model="checkAll" @change="selectAll"/>' +
        '                                  </th>\n' +
        '                                  <th ' +
        '                                   v-if="!column.hidden" ' +
        '                                   v-for="column in columns" ' +
        '                                   :width="column.width?column.width:\'\'"' +
        '                                   :style="\'text-align:\'+(column.align||\'left\')">{{column.title}}</th>\n' +
        '                                  <th v-if="operator && operator !== \'false\'" style="text-align: center">操作</th>\n' +
        '                              </tr>\n' +
        '                          </thead>\n' +
        '                          <draggable v-model="innerProps.data" @update="_drop" :element="\'tbody\'" :options="{handle:\'.draggable-handler\'}">\n' +
        '                          <tr v-for="(row, index) in innerProps.data" >\n' +
        '                              <td v-if="checkbox && checkbox !== \'false\'" align="center"><checkbox :value="row" v-model="selectedRows"/></td>\n' +
        '                              <datagrid-cell v-for="column in columns" :key="row.id" :column="column" :row="row" @click-cell="_clickCell"></datagrid-cell>\n' +
        '                              <td v-if="operator && operator !== \'false\'" style="text-align: center">' +
        '                                   <div class="col-xs-4 cell-title">操作：</div>\n' +
        '                                   <a v-if="draggable" class="btn btn-xs bg-purple draggable-handler" title="拖拽移动"><i class="fa fa-arrows"></i></a>' +
        '                                   <slot name="operator" v-bind:row="row" v-bind:index="index"></slot>' +
        '                              </td>\n' +
        '                          </tr>\n' +
        '                          <tr v-if="innerProps.data == null || innerProps.data.length <= 0">\n' +
        '                              <td :colspan="columns.length + 2" align="center">没有数据</td>\n' +
        '                          </tr>\n' +
        '                          </draggable>\n' +
        '                          <tfoot v-if="summary">\n' +
        '                              <tr>\n' +
        '                                  <th :colspan="columns.length + 2" v-html="summary(innerProps.data)"></th>\n' +
        '                              </tr>\n' +
        '                          </tfoot>\n' +
        '                      </table>\n' +
        '                  </div>\n' +
        '              </div>\n' +
        '              <pagination v-if="pagination" :current-page="innerProps.currentPage" :pager-size="pagerSize" :page-size="pageSize" :count="innerProps.count" @go="_goToPage"></pagination>\n' +
        '          </div>',
        data: function() {
            return {
                innerProps: {
                    data: this.data,
                    count: this.count,
                    currentPage: this.currentPage,
                    queryParams: {}
                },
                selectedRows: [],
                checkAll: false
            };
        },
        watch: {
            data: function(val) {
                this.innerProps.data = val;
            },
            count: function(val) {
                this.innerProps.count = val;
            },
            currentPage: function(val) {
                this.innerProps.currentPage = val;
            }
        },
        methods: {
            _goToPage: function(page) {
                this.$emit('go', page);
                if(this.url) {
                    this.reload({
                        currentPage: page
                    })
                }
            },
            _drop: function() {
                this.$emit('sort', this.innerProps.data);
            },
            selectRow: function(row, forceSelect) {
                if(forceSelect || $.inArray(row, this.selectedRows) < 0) {
                    this.selectedRows.push(row);
                } else {
                    this.selectedRows.splice($.inArray(row, this.selectedRows), 1);
                }
                this.$emit('select-row', row, this.selectedRows);
            },
            selectRows: function(rows, forceSelect) {
                var self = this;
                $.each(rows, function() {
                    self.selectRow(this, forceSelect);
                });
            },
            load: function(data) {
                if(!this.url && this.$emit('before-load', data)) {
                    throw new Error('datagrid url is required');
                }
                this.$emit('load', data);
                var self = this;
                this.innerProps.queryParams = data;
                $.ajax({
                    url: utils.patchUrl(this.url),
                    data: $.extend({},
                        this.queryParams,
                        {
                            pageSize: this.pageSize,
                            currentPage: 1
                        },
                        data
                    ),
                    success: function(data) {
                        self.loadData(data.content, data.totalElements);
                        self.$emit('after-load', data.content);
                    }
                });
            },
            reload: function(data) {
                this.load($.extend({
                    currentPage: this.innerProps.currentPage
                }, this.innerProps.queryParams, data));
            },
            loadData: function(data, count) {
                this.innerProps.data = this.tableTransformResponse(data);
                this.innerProps.count = count;
                this.clearSelected();
            },
            clearSelected: function() {
                this.selectedRows.splice(0);
            },
            selectAll: function(event) {
                if($(event.target).is(':checked')) {
                    this.selectRows(this.innerProps.data, true);
                } else {
                    this.clearSelected();
                }
            },
            _clickCell: function(row) {
                this.selectRow(row);
                this.$emit('click-row', row);
            }
        },
        mounted: function () {
            this.$emit('mounted', this);
        }
    };
});
