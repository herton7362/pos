require(['jquery', 'vue', 'messager', 'utils'], function($, Vue, messager, utils) {
    new Vue({
        el: '#content',
        data: {
            crudgrid: {
                queryParams: {
                    quickSearch: ''
                },
                columns: [
                    {field:'headPhoto', title:'头像', formatter: function(value) {
                        if(value) {
                            return '<img width="50" src="'+utils.patchUrl('/attachment/download/' + value.id)+'">';
                        } else {
                            return '无';
                        }

                    }},
                    {field:'loginName', title:'登录名'},
                    {field:'name', title:'姓名'},
                    {field:'mobile', title:'手机'},
                    {field:'status', title:'状态', formatter: function(value) {
                        if(value === 'ACTIVE') {
                            return '激活';
                        }
                        return '未激活';
                    }},
                    {field:'supportManagerAward', title:'是否支持管理奖', formatter: function(value) {
                            if(value === '0') {
                                return '不支持';
                            } else if(value === '1') {
                                return '支持';
                            }
                            return '未配置（默认支持）';
                        }},
                    {field:'manualLevel', title:'等级设置方式', formatter: function(value) {
                            if(value === '0') {
                                return '自动';
                            } else if(value === '1') {
                                return '手动';
                            }
                            return '未配置（默认自动升级）';
                        }},
                    {field:'idCard', title:'身份证'},
                    {field:'activeTime', title:'激活时间', formatter: function (value) {
                        if (value==null){
                            return "未记录";
                        }
                            return new Date(value).format('yyyy-MM-dd HH:mm:ss')
                        }},
                ]
            },
            formData: {
                id: null,
                name: null,
                headPhoto: null,
                memberLevel: null,
                mobile: null,
                gender: '1',
                birthday: null,
                idCard: null,
                address: null,
                supportManagerAward: null,
                manualLevel: null
            },
            memberLevelParams: []
        },
        methods: {
            loadMemberLevelParams: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/memberLevelParam'),
                    data: {
                        sort: 'sortNumber',
                        order: 'desc',
                        logicallyDeleted:false
                    },
                    success: function (data) {
                        self.memberLevelParams = data.content;
                    }
                })
            }
        },
        mounted: function() {
            this.crudgrid.$instance.load();
            this.loadMemberLevelParams();
        },
        watch: {
            'cards.form.memberCardType.id': function (val) {
                var cardType = null;
                $.each(this.cardType.list, function () {
                    if(this.id === val) {
                        cardType = this;
                    }
                });
                if(cardType) {
                    this.cards.form.discount = cardType.discount;
                }
            }
        }
    });
});
