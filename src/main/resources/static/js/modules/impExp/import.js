require(['jquery', 'vue', 'utils', 'messager'], function($, Vue, utils, messager) {
    var vue = new Vue({
        el: '#content',
        data: {
            lastRow: {},
            uploading: false,
            columns: [
                {field:'organizationNo', title:'机构编号'},
                {field:'organizationName', title:'机构名称'},
                {field:'userNo', title:'用户编号'},
                {field:'userName', title:'用户名称'},
                {field:'transactionAmount', title:'交易金额'},
                {field:'sn', title:'机器码'},
                {field:'transactionType', title:'交易类型'},
                {field:'transactionDate', title:'交易日期'},
                {field:'operateTransactionId', title:'批次号'}
            ],
            memberProfitTmpRecords: []
        },
        methods: {
            choseFile: function () {
                $(this.$refs['fileInput']).trigger('click');
            },
            changeValue: function () {
                var file = $(this.$refs['fileInput'])[0];
                var ie = (navigator.appVersion.indexOf("MSIE")!=-1);
                if( ie ){
                    var file2= file.cloneNode(false);
                    file2.onchange= file.onchange;
                    file.parentNode.replaceChild(file2,file);
                }else{
                    $(file).val("");
                }
            },
            upload: function(event) {
                var self = this;
                if((event.srcElement || event.target).files.length <= 0) {
                    return;
                }
                this.uploading = true;
                $.ajax({
                    url: utils.patchUrl('/api/memberprofit/import/profit'),
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    data: new FormData(this.$refs['uploadForm']),
                    success: function(data) {
                        // messager.bubble(data.responseText);
                        alert(data.responseText)
                        self.uploading = false;
                        self.changeValue();
                    },
                    error: function(data) {
                        // messager.bubble(data.responseText);
                        alert(data.responseText)
                        self.uploading = false;
                        self.changeValue();
                    }
                });
            },
            loadRecords: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/memberprofit/getMemberProfitTmpRecords'),
                    cache: false,
                    success: function (data) {
                        self.memberProfitTmpRecords = data;
                    }
                })
            },
            audit: function (row, flag) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/memberprofit/examineImportProfit/' + row.operateTransactionId + '/' + flag),
                    type: 'POST',
                    success: function () {
                        messager.bubble("审核成功");
                        self.loadRecords();
                    }
                })
            }
        },
        mounted: function() {
        }
    });
});
