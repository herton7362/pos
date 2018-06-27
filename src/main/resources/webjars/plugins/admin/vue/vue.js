define([
    'vuedraggable',
    'pagination',
    'datagrid',
    'datagrid-cell',
    'pagination',
    'validator',
    'sidebar',
    'modal',
    'crudgrid',
    'treegrid',
    'datepicker',
    'checkbox',
    'combobox',
    'radio',
    'combooption',
    'tree',
    'sidemodal',
    'uploader'
],function() {
    var Vue = require('base-vue');

    registerComponent('draggable', 'vuedraggable');
    registerComponent('pagination');
    registerComponent('datagrid');
    registerComponent('datagrid-cell');
    registerComponent('pagination');
    registerComponent('validator');
    registerComponent('sidebar');
    registerComponent('modal');
    registerComponent('crudgrid');
    registerComponent('treegrid');
    registerComponent('datepicker');
    registerComponent('checkbox');
    registerComponent('radio');
    registerComponent('combobox');
    registerComponent('combooption');
    registerComponent('tree');
    registerComponent('sidemodal');
    registerComponent('uploader');

    // 为所有组件增加instance属性，可以通过该属性找到当前组件
    // 如果component没有传值则使用name作为component
    function registerComponent(name, component) {
        if(component === undefined) {
            component = name;
        }
        var c = require(component);
        var mounted = c.mounted || function(){return this;};
        c.props = c.props || {};
        if(c.props instanceof Array) {
            c.props.push('instance');
        } else {
            c.props.instance = {
                type: Object,
                default: Object
            };
        }

        c.mounted = function () {
            if(this.instance) {
                this.instance.$instance = this;
            }
            return mounted.apply(this, arguments);
        };
        Vue.component(name,  c);
    }

    // 注册一个全局自定义指令 v-focus
    Vue.directive('focus', {
        // 当绑定元素插入到 DOM 中。
        inserted: function (el) {
            // 聚焦元素
            el.focus();
        }
    });

    return Vue;
});