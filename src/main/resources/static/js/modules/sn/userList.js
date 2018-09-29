require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            columns: [
                      {field:'sn', title:'sn号'}
                  	
                  ],
            data: [],
            datagrid:{
                $instance: {}
            },
            queryParams: {
            	status: '',
            	startSn:'',
            	endSn:'',
            	bindStatus:'UN_BIND'
            },
            currentPage: 1,
            pageSize: 50,
            pagerSize: 5,
            pageNum: 0,
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
            members: [],
            currentMemberId: null
        },
        methods: {
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
                        pageNum: this.pageNum,
                        memberId: this.currentMemberId
                    }, this.queryParams),
                    complete: function(data) {
                    data = data.responseJSON;
                        self.data = data.content;
                        self.count = data.totalElements;
                        self.clearSelected();
                    }
                })

            },

            goToPage: function (page) {
                this.currentPage = page;
                this.load();
            },

            distribute: function () {
                this.distribution.modal.$instance.open();
            },
            submitDistribution: function () {
                var self = this;
                if(!this.distribution.validator.$instance.isFormValid()) {
                    return;
                }
                var sns = [];
                $.each(this.datagrid.$instance.selectedRows, function () {
                    sns.push(this.sn);
                });
                $.ajax({
                    url: utils.patchUrl('/api/sn/transSnByMember'),
                    contentType: 'application/json',
                    type: 'POST',
                    dataType: 'text',
                    data: JSON.stringify($.extend(this.distribution.formData, {
                        sns: sns.join(','),
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

    });
});
