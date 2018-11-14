require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            columns: [
                      {field:'sn', title:'sn号'},
                      {field:'shopId', title:'绑定状态', formatter: function(value) {
                              return null != value ? '绑定' : '未绑定';
                          }},
                      {field:'memberName', title:'归属合伙人'},
                      {field:'transMemberName', title:'划拨人'},
                      {field:'shopName', title:'归属用户'}
                  	
                  ],
            data: [],
            datagrid:{
                $instance: {}
            },
            queryParams: {
            	status: '',
            	startSn:'',
            	endSn:''
            },
            currentPage: 1,
            pageSize: 50,
            pagerSize: 5,
            count: 0,
            selectedRows: [],
            formData: {
                id: null,
                sn: null
            },
            modal: {
                $instance: {}
            },
            validator: {
                $instance: {}
            },
            distribution: {
                validator: {
                    $instance: {}
                },
                formData: {
                    memberId: null,
                    sns: null,
                    currentMemberId: null
                },
                modal: {
                    $instance: {}
                }
            },
            sns : [],
            members: [],
            currentMemberId: null
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
                this.load();
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
                        //memberId: this.currentMemberId,
                        status: ""
                    }, this.queryParams),
                    complete: function(data) {
                    data = data.responseJSON;
                        self.data = data.content;
                        self.count = data.totalElements;
                        self.clearSelected();
                        self.datagrid.$instance.selectedRows = [];
                    }
                })
/*                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/sn'),
                    data: $.extend({
                        sort: 'updatedDate',
                        order: 'desc',
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        pageNum: this.pageNum
                        //memberId: this.currentMemberId
                    }, this.queryParams),
                    success: function(data) {
                        self.data = data.content;
                        self.count = data.totalElements;
                        self.clearSelected();
                    }
                })*/
            },
            search: function (val) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/sn/getAllSnInfo'),
                    data: $.extend({
                        sort: 'updatedDate',
                        order: 'desc',
                        currentPage: this.currentPage,
                        pageSize: this.pageSize,
                        //memberId: this.currentMemberId,
                        status: val
                    }, this.queryParams),
                    complete: function(data) {
                    data = data.responseJSON;
                        self.data = data.content;
                        self.count = data.totalElements;
                        self.clearSelected();
                        self.datagrid.$instance.selectedRows = [];
                    }
                })
            },
            goToPage: function (page) {
                this.currentPage = page;
                this.load();
            },
            add: function () {
                this.validator.$instance.removeMark();
                this.formData = {};
                this.modal.$instance.open();
            },
            edit: function (row) {
                var self = this;
                this.validator.$instance.removeMark();
                $.ajax({
                    url: utils.patchUrl('/api/sn/' + row.id),
                    success: function(data) {
                        self.formData = data;
                        self.modal.$instance.open();
                    }
                });
            },
            remove: function (row) {
                var rows = [];
                var self = this;
                messager.alert('确定要删除吗？', event, function() {
                    var ids = [];
                    $.each(rows.concat(row), function() {
                        ids.push(this.id);
                    });
                    $.ajax({
                        url: utils.patchUrl('/api/sn/' + ids.join(',')),
                        type: 'DELETE',
                        success: function() {
                            messager.bubble('删除成功');
                            self.load();
                            self.datagrid.$instance.selectedRows = [];
                        }
                    });
                });
            },
            save: function () {
                var self = this;
                if(!this.validator.$instance.isFormValid()) {
                    return;
                }
                $.ajax({
                    url: utils.patchUrl('/api/sn'),
                    contentType: 'application/json',
                    type: 'POST',
                    data: JSON.stringify($.extend(this.formData, {
                        //memberId: this.currentMemberId
                    })),
                    success: function(data) {
                        self.modal.$instance.close();
                        messager.bubble('保存成功！');
                        self.load();
                    }
                })
            },
            snExtendDel: function(){
            	var flag = true;
                $("input[name='snExtendName']").each(function (index, item) {
                	  if($(this).prop("checked")){
                		  flag = false;
                		  $(this).parent().parent().remove();
                	  }
                });
                if(flag){
                	messager.bubble('请勾选一个sn，再删除！！');
                }
            },
            snExtendAdd: function(){
            	if($("#snAddValue").val()==''){
            		messager.bubble('不可以为空');
            		return;
            	}
                $("#snExtend").append("<div><label><input type='checkbox' name='snExtendName' value='"+$("#snAddValue").val()+"'>待选sn："+$("#snAddValue").val()+"</label><br/></div>");
            },
            distribute: function () {
            	$("#snExtend").html("");
                $.each(this.datagrid.$instance.selectedRows, function () {
                    $("#snExtend").append("<div><label><input type='checkbox' name='snExtendName' value='"+this.sn+"'>待选sn："+this.sn+"</label><br/></div>");
                });
                this.distribution.modal.$instance.open();
            },
            submitDistribution: function () {
                var self = this;
                if(!this.distribution.validator.$instance.isFormValid()) {
                    return;
                }

                self.sns = [];
                $("input[name='snExtendName']").each(function (index, item) {
                	self.sns.push($(this).val());
              });
                $.ajax({
                    url: utils.patchUrl('/api/sn/transSnByAdmin'),
                    contentType: 'application/json',
                    type: 'POST',
                    dataType: 'text',
                    data: JSON.stringify($.extend(this.distribution.formData, {
                        sns: self.sns.join(','),
                        currentMemberId: self.currentMemberId
                    })),
                    success: function(data) {
                        self.distribution.modal.$instance.close();
                        messager.bubble('保存成功！');
                        self.load();
                    }
              
                })
            }
        },
        mounted: function() {
        	var self = this;
            $.ajax({
                url: utils.patchUrl('/api/member'),
                data: {
                    sort: 'sortNumber',
                    order: 'asc',
                    logicallyDeleted: false
                },
                success: function(data) {
                    $.each(data.content, function () {
                        this.name = this.name + '(' + this.loginName + ')';
                    });
                    self.members = data.content;
                }
            });
            $.ajax({
                url: utils.patchUrl('/user/info'),
                cache: false,
                success: function (user) {
                    self.currentMemberId = user.id;
                    self.loadRecords();
                }
            })
        },
        watch: {
            'queryParams.status': function (val) {
                if(val == '') {
                    this.load();
                }
                if(val == 'DISTRIBUTION') {
                	this.search(val);
                }
                if(val == 'UN_DISTRIBUTION') {
                	this.search(val);
                }
            }
        }
    });
});
