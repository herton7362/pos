define(function() {
    return {
        props: {
            column: Object,
            row: Object
        },
        template: '<td' +
        '                   @click="_clickCell(row, column)"\n' +
        '                   v-if="!column.hidden"\n' +
        '                   :style="_createStyle(column)"\n' +
        '                   \n' +
        '           >\n' +
        '               <div class="col-xs-4 cell-title">{{column.title}}：</div>\n' +
        '               <span v-if="!_isCellEditing(column, row)" v-html="_renderColumn(column, row)"></span>\n' +
        '               <input \n' +
        '                       type="number" \n' +
        '                       v-if="_canEditorShow(\'number\', column, row)" \n' +
        '                       :style="_createStyle(column)" \n' +
        '                       v-focus \n' +
        '                       @blur="cancelEditCell(column, row)" \n' +
        '                       v-model="row[column.field]">\n' +
        '               <input \n' +
        '                       type="text" \n' +
        '                       v-if="_canEditorShow(\'text\', column, row)" \n' +
        '                       :style="_createStyle(column)" \n' +
        '                       v-focus \n' +
        '                       @blur="cancelEditCell(column, row)" \n' +
        '                       v-model="row[column.field]">\n' +
        '           </td>',
        data: function() {
            return {
                fieldTypes: ['number', 'text']
            };
        },
        methods: {
            _clickCell: function(row, column) {
                this.editCell(column, row);
                this.$emit('click-cell', row, column);
            },
            _renderColumn: function (column, row) {
                return column.formatter?
                    column.formatter(eval('(row.' + column.field + ')'), row):
                    eval('(row.' + column.field + ')');
            },
            _isCellEditing: function(column, row) {
                return row[column.field + '_editing'];
            },
            _createStyle: function(column) {
                return 'text-align:'+(column.align||'left') + ';width:'+(column.width+'px'||'auto');
            },
            _canEditorShow: function(type, column, row) {
                if(withEditor() && this.fieldTypes.indexOf(column.editor.type) < 0) {
                    throw Error('editor [' + column.editor.type + '] dose not exist');
                }
                function withEditor() {
                    return !!column.editor;
                }
                return withEditor() && column.editor.type === type && this._isCellEditing(column, row);
            },
            editCell: function(column, row) {
                if(column.editor && !this._isCellEditing(column, row)) {
                    this.$set(row, column.field + '_editing', true);
                }
            },
            cancelEditCell: function(column, row) {
                if(column.editor && this._isCellEditing(column, row)) {
                    this.$set(row, column.field + '_editing', false);
                }
            }
        },
        mounted: function () {
            this.$emit('mounted', this);
        }
    };
});