define(['jquery', 'utils'], function($, utils) {
    return {
        props: {
            accept: {
                type: String,
                default: 'image/*'
            },
            multiple: {
                type: Boolean,
                default: false
            },
            value: [Array, Object]
        },
        template: '<div class="uploader">\n' +
        '              <form class="margin-bottom" ref="uploadForm">\n' +
        '                  <input ref="fileInput" type="file" name="attachments" :accept="accept" :multiple="multiple" @change="upload"  style="display: none;"/>\n' +
        '                  <button type="button" class="btn btn-flat btn-success btn-sm" @click="choseFile">\n' +
        '                      <i class="fa fa-paperclip"></i> 上传附件\n' +
        '                  </button>\n' +
        '                  <button type="button" class="btn btn-flat btn-sm" @click="openModal">\n' +
        '                      <i class="fa fa-fw fa-list"></i> 选择附件\n' +
        '                  </button>\n' +
        '              </form>\n' +
        '              <ul class="mailbox-attachments clearfix">\n' +
        '                  <li v-for="row in files">\n' +
        '                      <span class="mailbox-attachment-icon has-img">\n' +
        '                          <img :src="row.id | coverPath" alt="Attachment">\n' +
        '                      </span>\n' +
        '                      <div class="mailbox-attachment-info">\n' +
        '                          <span class="mailbox-attachment-size">\n' +
        '                            {{row.size | sizeFormatter}}\n' +
        '                            <a href="javascript:void(0)" class="btn btn-danger btn-xs pull-right" @click="remove(row.id, $event)" title="删除">\n' +
        '                                <i class="fa fa-trash"></i>\n' +
        '                            </a>\n' +
        '                            <a :href="row.id | downloadPath" target="_blank" class="btn btn-default btn-xs pull-right margin-r-5" title="下载">\n' +
        '                                <i class="fa fa-cloud-download"></i>\n' +
        '                            </a>\n' +
        '                          </span>\n' +
        '                      </div>\n' +
        '                  </li>\n' +
        '              </ul>\n' +
        '              <modal type="MODAL_LG" title="附件列表" :instance="modal">\n' +
        '                  <template slot="body">\n' +
        '                      <ul class="mailbox-attachments clearfix">\n' +
        '                          <li v-for="row in attachment.data" @click="selectAttachment(row)">\n' +
        '                              <span class="mailbox-attachment-icon has-img"> \n' +
        '                                  <img :src="row.id | coverPath" alt="Attachment">\n' +
        '                              </span>\n' +
        '                              <div class="mailbox-attachment-info">\n' +
        '                                  <a href="javascript:void(0)" :title="row.name" class="mailbox-attachment-name">\n' +
        '                                      <i class="fa fa-camera"></i> {{row.name}}\n' +
        '                                  </a>\n' +
        '                                  <span class="mailbox-attachment-size">\n' +
        '                                    {{row.size | sizeFormatter}}\n' +
        '                                  </span>\n' +
        '                              </div>\n' +
        '                          </li>\n' +
        '                      </ul>\n' +
        '                      <pagination :current-page="attachment.currentPage" :pager-size="7" :page-size="attachment.pageSize" :count="attachment.count" @go="_goToPage"></pagination>\n' +
        '                  </template>\n' +
        '              </modal>\n' +
        '          </div>',
        data: function () {
            return {
                files: (this.multiple)? this.value: this.value?[this.value]: [],
                attachment: {
                    pageSize: 20,
                    data: [],
                    count: 0,
                    currentPage: 1
                },
                modal: {
                    $instance: {}
                }
            }
        },
        watch: {
            value: function (val) {
                if(this.mutedInput) {
                    this.mutedInput = false;
                    return;
                }
                var self = this;
                this.files.splice(0);
                if(this.multiple) {
                    $.each(val, function () {
                        self.files.push(this);
                    })
                } else if(val) {
                    self.files.push(val);
                }
            }
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
            remove: function (id) {
                for(var i = this.files.length - 1; i >= 0; i--) {
                    if(this.files[i].id === id) {
                        this.files.splice(i, 1);
                        this.$emit('removed', id);
                        this.changeValue();
                        break;
                    }
                }
            },
            getFiles: function () {
                return this.files;
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
                    success: function(data) {
                        require(['messager'], function(messager) {
                            messager.bubble('上传完毕');
                        });
                        self.add(data);
                        self.changeValue();
                        self.$emit('uploaded', data);
                    }
                });
            },
            add: function (attachment) {
                var self = this;
                if(!this.multiple) {
                    this.files.splice(0);
                }
                $.each(attachment, function() {
                    self.files.push(this);
                });
                this.changeValue();
            },
            loadAttachment: function (data) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/attachment'),
                    data: $.extend({
                        pageSize: this.attachment.pageSize,
                        currentPage: this.attachment.currentPage
                    }, data),
                    success: function(data) {
                        self.attachment.data = data.content;
                        self.attachment.count = data.totalElements;
                    }
                });
            },
            selectAttachment: function (row) {
                this.add([row]);
                this.modal.$instance.close();
            },
            _goToPage: function (page) {
                this.attachment.currentPage = page;
                this.loadAttachment({
                    currentPage: page
                });
            },
            openModal: function () {
                this.loadAttachment();
                this.modal.$instance.open();
            }
        }
    }
});