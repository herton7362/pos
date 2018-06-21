require(['jquery', 'vue', 'messager', 'utils', _appConf.ctx + '/static/js/modules/icon/iconSelector.js'], function($, Vue, messager, utils, iconSelector) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                $instance: {},
                columns: [
                    {field:'name', title:'名称'},
                    {field:'icon', title:'图标', width: 30, align: 'center', formatter: function (val) {
                        return '<i class="'+(val || 'fa fa-circle-o')+'"></i>';
                    }},
                    {field:'type', title:'类型', align: 'center', formatter: function(value){
                        if(value === 'MENU') {
                            return '<small class="label bg-green">菜单</small>';
                        }
                        return '<small class="label label-primary">功能</small>';
                    }},
                    {field:'url', title:'链接'},
                    {field:'code', title:'权限代码'},
                    {field:'disabled', title:'状态', align: 'center', formatter: function(value){
                        if(value) {
                            return '<small class="label bg-gray">正常</small>';
                        }
                        return '<small class="label bg-green">启用</small>';
                    }}
                ]
            },
            sidebar: {
                root: {
                    id: 'isNull',
                    name: '所有模块',
                    open: true,
                    alwaysExpended: true
                }
            },
            parent: {
                data: []
            },
            formData: {
                id: '',
                parent: {},
                name: '',
                icon: null,
                code: '',
                url: '',
                type: 'MENU',
                naviNamePath: '',
                naviIdPath: '',
                disabled: false
            }
        },
        methods: {
            refresh: function () {
                this.loadCombobox();
            },
            selectIcon: function () {
                iconSelector.open();
            },
            loadCombobox: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/module'),
                    data: {
                        sort: 'sortNumber',
                        order: 'asc'
                    },
                    success: function(data) {
                        self.parent.data = data.content;
                    }
                })
            },
            modalOpen: function(form) {
                form.type = form.type || 'MENU';
            }
        },
        mounted: function() {
            var self = this;
            this.loadCombobox();
            iconSelector.onSelect = function (icon) {
                self.crudgrid.$instance.getForm().icon = icon;
            }
        }
    });
});