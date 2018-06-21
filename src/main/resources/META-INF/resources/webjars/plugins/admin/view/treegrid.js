define(['jquery', 'utils'], function($, utils) {
    return {
        props: {
            columns: Array,
            domain: String,
            root: Object,
            formData: {
                type: Object,
                default: Object
            },
            queryKey: {
                type: String,
                default: 'name'
            },
            queryParams: {
                type: Object,
                default: function() {
                    return {
                        sort:'sortNumber,updatedDate',
                        order: 'asc,desc'
                    }
                }
            }
        },
        template: '<div class="row">\n' +
        '                <div class="col-md-3">\n' +
        '                    <div class="box">\n' +
        '                        <div class="box-body">\n' +
        '                            <sidebar\n' +
        '                                    :data="sidebar.data"\n' +
        '                                    :root="sidebar.root"\n' +
        '                                    :selected-id="sidebar.selectedId"\n' +
        '                                    :instance="sidebar"\n' +
        '                                    @click-row="sidebarClick"></sidebar>\n' +
        '                        </div>\n' +
        '                    </div>\n' +
        '                </div>\n' +
        '                <div class="col-md-9">\n' +
        '                    <crudgrid' +
        '                            :domain="domain"\n' +
        '                            :columns="columns"\n' +
        '                            :form-data="formData"\n' +
        '                            :query-params="queryParams"\n' +
        '                            :instance="crudgrid"\n' +
        '                            @refresh="refresh"\n' +
        '                            @open="modalOpen"\n' +
        '                            @sort="datagridSort"\n' +
        '                    >\n' +
        '                        <template slot="form-body" scope="props">\n' +
        '                            <slot name="form-body" :form="props.form"></slot>\n' +
        '                        </template>\n' +
        '                    </crudgrid>\n' +
        '                </div>\n' +
        '            </div>',
        data: function() {
            return {
                crudgrid: {
                    $instance: {},
                    queryParams: this.queryParams,
                    columns: this.columns
                },
                sidebar: {
                    $instance: {},
                    root: this.root,
                    selectedId: this.root.id,
                    data: []
                }
            };
        },
        methods: {
            modalOpen: function() {
                var $form = this.crudgrid.$instance.getForm();
                this.$emit('open', $form);
                if(this.sidebar.$instance.getSelectedId()) {
                    var id = null;
                    if(this.sidebar.$instance.getSelectedId() !== 'isNull') {
                        id = this.sidebar.$instance.getSelectedId();
                    }
                    $form.parent = {
                        id: id
                    }
                } else {
                    $form.parent = {};
                }
            },
            refresh: function() {
                this.loadSidebar(function(data) {
                    this.$emit('refresh', data);
                });
            },
            loadSidebar: function(callback) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/' + this.domain),
                    data: this.crudgrid.queryParams,
                    success: function(data) {
                        self.sidebar.data = data.content;

                        if(callback instanceof Function) {
                            callback.call(self, data.content);
                        }
                    }
                });
            },
            sidebarClick: function(row) {
                this.crudgrid.$instance.load({'parent.id': row.id});
            },
            getForm: function() {
                return this.crudgrid.$instance.getForm();
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
            }
        },
        mounted: function() {
            this.loadSidebar(function() {
                this.sidebarClick(this.sidebar.root);
            });
            this.$emit('mounted', this);
        }
    };
});