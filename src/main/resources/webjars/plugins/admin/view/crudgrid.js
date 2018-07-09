define(['jquery', 'utils'], function($, utils) {
    return {
        props: {
            columns: Array,
            domain: String,
            addAble: {
                type: Boolean,
                default: true
            },
            formData: {
                type: Object,
                default: Object
            },
            queryKey: {
                type: String,
                default: 'name'
            },
            searchAble: {
                type: Boolean,
                default: true
            },
            queryParams: {
                type: Object,
                default: function() {
                    return {
                        sort:'sortNumber,updatedDate',
                        order: 'asc,desc'
                    }
                }
            },
            tableTransformResponse: {
                type: Function,
                default: function(response) {
                    return response
                }
            }
        },
        template: '<div class="box">\n' +
        '              <div class="box-header">\n' +
        '                  <div class="row">\n' +
        '                      <div class="col-md-8">\n' +
        '                          <button type="button" class="btn btn-flat btn-primary" @click="refresh"><i class="fa fa-refresh"></i> </button>\n' +
        '                          <button v-if="addAble" class="btn btn-flat btn-success" @click="add"><i class="fa fa-plus-square"></i> 添加</button>\n' +
        '                          <button\n' +
        '                                  class="btn btn-flat btn-success"\n' +
        '                                  :disabled="datagrid.$instance.selectedRows <= 0"\n' +
        '                                  :class="{\'disabled\': datagrid.$instance.selectedRows <= 0}"\n' +
        '                                  @click="edit(datagrid.$instance.selectedRows[0])">\n' +
        '                              <i class="fa fa-pencil"></i> 修改\n' +
        '                          </button>\n' +
        '                          <button\n' +
        '                                  class="btn btn-flat btn-danger"\n' +
        '                                  :disabled="datagrid.$instance.selectedRows <= 0"\n' +
        '                                  :class="{\'disabled\': datagrid.$instance.selectedRows <= 0}"\n' +
        '                                  @click="remove(datagrid.$instance.selectedRows, $event)">\n' +
        '                              <i class="fa fa-trash"></i> 删除\n' +
        '                          </button>\n' +
        '                          <button\n' +
        '                                  class="btn btn-flat btn-danger"\n' +
        '                                  :disabled="datagrid.$instance.selectedRows <= 0"\n' +
        '                                  :class="{\'disabled\': datagrid.$instance.selectedRows <= 0}"\n' +
        '                                  @click="disable(datagrid.$instance.selectedRows, $event)">\n' +
        '                              <i class="fa fa-stop"></i> 禁用\n' +
        '                          </button>\n' +
        '                          <button\n' +
        '                                  class="btn btn-flat btn-success"\n' +
        '                                  :disabled="datagrid.$instance.selectedRows <= 0"\n' +
        '                                  :class="{\'disabled\': datagrid.$instance.selectedRows <= 0}"\n' +
        '                                  @click="enable(datagrid.$instance.selectedRows, $event)">\n' +
        '                              <i class="fa fa-play"></i> 启用\n' +
        '                          </button>\n' +
        '                          <slot name="toolbar"></slot>\n' +
        '                      </div>\n' +
        '                      <div class="col-md-3">\n' +
        '                          <div class="input-group pull-right has-feedback search" v-if="searchAble">\n' +
        '                              <input\n' +
        '                                      class="form-control"\n' +
        '                                      type="text"\n' +
        '                                      placeholder="搜索"\n' +
        '                                      v-model="datagrid.queryParams.name"\n' +
        '                                      @input="datagrid.$instance.reload()"\n' +
        '                              >\n' +
        '                              <span\n' +
        '                                      v-if="datagrid.queryParams[queryKey]"\n' +
        '                                      class="fa fa-times-circle-o form-control-feedback"\n' +
        '                                      aria-hidden="true"\n' +
        '                                      @click="datagrid.queryParams.name=\'\';datagrid.$instance.reload()"\n' +
        '                              ></span>\n' +
        '                          </div>\n' +
        '                      </div>\n' +
        '                      <div class="col-md-1 hidden-xs hidden-sm">\n' +
        '                          <div class="btn-group pull-right">' +
        '                               <button type="button"' +
        '                                       class="btn btn-default dropdown-toggle"' +
        '                                       data-toggle="dropdown"' +
        '                                       aria-expanded="false">\n' +
        '                                    <i class="glyphicon glyphicon-th icon-th"></i>\n' +
        '                                    <span class="caret"></span>\n' +
        '                                    <span class="sr-only">Toggle Dropdown</span>\n' +
        '                               </button>\n' +
        '                               <ul class="dropdown-menu" role="menu">\n' +
        '                                    <li v-for="column in datagrid.columns">\n' +
        '                                       <a href="javascript:void(0)" @click="toggleColumnVisibility(column)">' +
        '                                           <input type="checkbox" :checked="!column.hidden"> {{column.title}}' +
        '                                       </a>\n' +
        '                                    </li>\n' +
        '                               </ul>\n' +
        '                          </div>\n' +
        '                      </div>\n' +
        '                  </div>\n' +
        '              </div>\n' +
        '              <div class="box-body">\n' +
        '                  <datagrid\n' +
        '                          :checkbox="true"\n' +
        '                          :operator="true"\n' +
        '                          :draggable="true"\n' +
        '                          :query-params="datagrid.queryParams"\n' +
        '                          :url="datagrid.url"\n' +
        '                          :columns="datagrid.columns"\n' +
        '                          :data="datagrid.data"\n' +
        '                          :count="datagrid.count"\n' +
        '                          :pagination="true"\n' +
        '                          :instance="datagrid"\n' +
        '                          @load="onLoad"\n' +
        '                          :table-transform-response="tableTransformResponse"' +
        '                          @sort="datagridSort">\n' +
        '                      <template slot="operator" scope="props">\n' +
        '                          <a class="btn btn-xs bg-green" @click="edit(props.row)" title="修改"><i class="fa fa-pencil"></i></a>\n' +
        '                          <a class="btn btn-xs bg-red" @click="remove(props.row, $event)" title="删除"><i class="fa fa-trash"></i></a>\n' +
        '                          <a v-if="props.row.logicallyDeleted" class="btn btn-xs bg-green" @click="enable(props.row, $event)" title="启用"><i class="fa fa-play"></i></a>\n' +
        '                          <a v-if="!props.row.logicallyDeleted" class="btn btn-xs bg-red" @click="disable(props.row, $event)" title="禁用"><i class="fa fa-stop"></i></a>\n' +
        '                          <slot name="datagrid-operator" v-bind:row="props.row"></slot>\n' +
        '                      </template>\n' +
        '                  </datagrid>\n' +
        '              </div>\n' +
        '              <!-- /.box-body -->\n' +
        '              <!-- sidemodal -->\n' +
        '              <validator :instance="validator">\n' +
        '                  <form @submit.prevent="save">\n' +
        '                      <sidemodal title="维护窗口" :footer="true" @open="modalOpen" :instance="modal">\n' +
        '                          <template slot="body">\n' +
        '                              <slot name="form-body" :form="form"></slot>\n' +
        '                              <!-- /.box-body -->\n' +
        '                          </template>\n' +
        '                          <template slot="footer">\n' +
        '                              <button type="button" class="btn btn-default margin-r-5 btn-flat" @click="modal.$instance.close()">取消编辑</button>\n' +
        '                              <button type="submit" class="btn btn-primary btn-flat">保存数据</button>\n' +
        '                          </template>\n' +
        '                      </sidemodal>\n' +
        '                  </form>\n' +
        '              </validator>\n' +
        '              <!-- /.sidemodal -->\n' +
        '          </div>',
        data: function() {
            return {
                datagrid: {
                    $instance: {},
                    url: '/api/' + this.domain,
                    queryParams: $.extend({}, this.queryParams),
                    columns: this.columns
                },
                validator: {
                    $instance: {}
                },
                modal: {
                    $instance: {}
                },
                form: this.formData,
                defaultFormData: $.extend(true, {}, this.formData)
            };
        },
        methods: {
            add: function() {
                this.validator.$instance.removeMark();
                this.form = $.extend(true, {}, this.defaultFormData);
                this.modal.$instance.open();
            },
            edit: function(row) {
                var self = this;
                this.validator.$instance.removeMark();
                $.ajax({
                    url: utils.patchUrl('/api/' + this.domain + '/' + row.id),
                    success: function(data) {
                        $.each($.extend(true, {}, self.defaultFormData), function(key, value) {
                            if((value instanceof Object || value instanceof Array) && data[key] === null) {
                                data[key] = value;
                            }
                        });
                        self.form = data;
                        self.modal.$instance.open();
                    }
                });

            },
            save: function() {
                var self = this;
                if(!this.validator.$instance.isFormValid()) {
                    return;
                }
                $.ajax({
                    url: utils.patchUrl('/api/' + this.domain),
                    contentType: 'application/json',
                    type: 'POST',
                    dataType: 'JSON',
                    data: JSON.stringify(this.form),
                    success: function(data) {
                        self.modal.$instance.close();
                        require(['messager'], function(messager) {
                            messager.bubble('保存成功！');
                        });
                        self.$emit('save', data);
                        self.refresh();
                    }
                })
            },
            remove: function(row, event) {
                var rows = [];
                var self = this;
                require(['messager'], function(messager) {
                    messager.alert('确定要删除吗？', event, function() {
                        var ids = [];
                        $.each(rows.concat(row), function() {
                            ids.push(this.id);
                        });
                        $.ajax({
                            url: utils.patchUrl('/api/' + self.domain + '/' + ids.join(',')),
                            type: 'DELETE',
                            success: function() {
                                messager.bubble('删除成功');
                                self.$emit('remove', ids);
                                self.refresh();
                            }
                        });
                    });
                });
            },
            disable: function(row, event) {
                var rows = [];
                var self = this;
                require(['messager'], function(messager) {
                    messager.alert('确定要禁用吗？', event, function() {
                        var ids = [];
                        $.each(rows.concat(row), function() {
                            ids.push(this.id);
                        });
                        $.ajax({
                            url: utils.patchUrl('/api/' + self.domain + '/disable/' + ids.join(',')),
                            type: 'POST',
                            success: function() {
                                messager.bubble('禁用成功');
                                self.$emit('disable', ids);
                                self.refresh();
                            }
                        });
                    });
                });
            },
            enable: function(row, event) {
                var rows = [];
                var self = this;
                require(['messager'], function(messager) {
                    messager.alert('确定要启用吗？', event, function() {
                        var ids = [];
                        $.each(rows.concat(row), function() {
                            ids.push(this.id);
                        });
                        $.ajax({
                            url: utils.patchUrl('/api/' + self.domain + '/enable/' + ids.join(',')),
                            type: 'POST',
                            success: function() {
                                messager.bubble('启用成功');
                                self.$emit('enable', ids);
                                self.refresh();
                            }
                        });
                    });
                });
            },
            refresh: function() {
                this.$emit('refresh');
                this.datagrid.$instance.reload();
            },
            modalOpen: function() {
                this.$emit('open', this);
            },
            onLoad: function(data) {
                this.$emit('load', data);
            },
            getForm: function() {
                return this.form;
            },
            load: function(data) {
                this.datagrid.$instance.load($.extend({}, $.extend({}, this.datagrid.queryParams), data));
            },
            datagridSort: function(data) {
                if(this.$emit('sort', data) === false) {
                    return;
                }
                $.ajax({
                    url: utils.patchUrl('/api/' + this.domain + '/sort'),
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(data),
                    success: function () {
                        require(['messager'], function(messager) {
                            messager.bubble('排序成功');
                        });
                    }
                });
            },
            getSelectedRows: function () {
                return this.datagrid.$instance.selectedRows;
            },
            toggleColumnVisibility: function (column) {
                this.$set(column, 'hidden', !column.hidden);
            }
        },
        mounted: function() {
            this.$emit('mounted', this);
        }
    };
});
