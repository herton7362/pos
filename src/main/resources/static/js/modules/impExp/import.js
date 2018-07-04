require(['jquery', 'vue', 'utils'], function($, Vue, utils) {
    var vue = new Vue({
        el: '#content',
        data: {
            uploading: false
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
                        require(['messager'], function(messager) {
                            // messager.bubble(data.responseText);
                            alert(data.responseText)
                        });
                        self.uploading = false;
                        self.changeValue();
                    },
                    error: function(data) {
                        require(['messager'], function(messager) {
                            // messager.bubble(data.responseText);
                            alert(data.responseText)
                        });
                        self.uploading = false;
                        self.changeValue();
                    }
                });
            }
        },
        mounted: function() {
        }
    });
});
