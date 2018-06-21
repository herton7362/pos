require(['jquery', 'vue', 'utils', 'messager'], function($, Vue, utils, messager) {
    new Vue({
        el: '#content',
        data: {
            pageSize: 20,
            data: [],
            count: 0,
            currentPage: 1
        },
        filters: {
            coverPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            },
            downloadPath: function (val) {
                return utils.patchUrl('/attachment/download/' + val);
            },
            sizeFormatter: function (val) {
                return utils.bytesToSize(val);
            }
        },
        methods: {
            goToPage: function (page) {
                this.currentPage = page;
                this.load({
                    currentPage: page
                });
            },
            choseFile: function () {
                $(this.$refs['fileInput']).trigger('click');
            },
            upload: function(event) {
                var self = this;
                if(event.srcElement.files.length <= 0) {
                    return;
                }
                $.ajax({
                    url: utils.patchUrl('/attachment/upload'),
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    data: new FormData(this.$refs['uploadForm']),
                    success: function() {
                        messager.bubble('上传完毕');
                        self.load();
                    }
                });
            },
            load: function (data) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/attachment'),
                    data: $.extend({
                        pageSize: this.pageSize,
                        currentPage: this.currentPage
                    }, data),
                    success: function(data) {
                        self.data = data.content;
                        self.count = data.totalElements;
                    }
                });
            },
            remove: function (id, event) {
                var self = this;
                messager.alert('确定要删除吗？', event, function() {
                    var ids = [id];
                    $.ajax({
                        url: utils.patchUrl('/api/attachment/' + ids.join(',')),
                        type: 'DELETE',
                        success: function() {
                            messager.bubble('删除成功');
                            self.load();
                        }
                    });
                });
            }
        },
        mounted: function() {
            this.load();
        }
    });
});