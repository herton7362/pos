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
                this.mutedInput = true;
                if(!this.multiple) {
                    this.$emit('input', this.files[0])
                } else {
                    this.$emit('input', this.files)
                }
            },
            upload: function(event) {
                var self = this;
                if(event.srcElement.files.length <= 0) {
                    return;
                }
                this.uploading = true;
                $.ajax({
                    url: utils.patchUrl('/attachment/upload'),
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    data: new FormData(this.$refs['uploadForm']),
                    success: function() {
                        require(['messager'], function(messager) {
                            messager.bubble('上传完毕');
                        });
                        self.uploading = false;
                        self.changeValue();
                    }
                });
            },
        },
        mounted: function() {
        }
    });
});
