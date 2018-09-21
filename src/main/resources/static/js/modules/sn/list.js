require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            columns: [
                      {field:'sn', title:'sn号'},
                      {field:'transDate', title:'划拨日期'}
                  ],
            datagrid: {
                queryParams: {
                	clientId: '123'
                }
            },
            data: [],
            queryParams: {
            },
            currentPage: 1,
            pageSize: 15,
            pageNum: 0,
            selectedRows: [],
            formData: {
                id: null,
                sn: null
            }
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
                    url: utils.patchUrl('/api/sn/importSn'),
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    data: new FormData(this.$refs['uploadForm']),
                    success: function(data) {
                        alert(data.responseText)
                        self.uploading = false;
                        self.changeValue();
                        self.loadRecords();
                    },
                    error: function(data) {
                        alert(data.responseText)
                        self.uploading = false;
                        self.changeValue();
                        self.loadRecords();
                    }
                });
            },
            loadRecords: function () {
                this.datagrid.$instance.load(this.datagrid.queryParams);
            },
            clearSelected: function () {
                this.selectedRows.splice(0);
            },
            load: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/sn/getAllSnInfo'),
                    data: $.extend({
                        sort: 'updatedDate',
                        order: 'desc',
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        pageNum: this.pageNum
                    }, this.queryParams),
                    success: function(data) {
                        self.data = data.content;
                        self.count = data.totalElements;
                        self.clearSelected();
                    }
                })
            }
        },
        mounted: function() {
        	this.loadRecords();
        },
        watch: {
            'datagrid.queryParams.clientId': function () {
                this.loadRecords();
            }
        }
    });
});
