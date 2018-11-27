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
                    // {field:'gender', title:'性别', formatter: function(value) {
                    //     if(value === '0') {
                    //         return '女士';
                    //     } else if(value === '1') {
                    //         return '先生';
                    //     }
                    //     return '未知';
                    // }},
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
                    // {field:'birthday', title:'生日', formatter: function(value) {
                    //     if(!value) {
                    //         return '';
                    //     }
                    //     return new Date(value).format("yyyy-MM-dd");
                    // }},
                    {field:'idCard', title:'身份证'},
                    {field:'activeTime', title:'激活时间', formatter: function (value) {
                        if (value==null){
                            return "未记录";
                        }
                            return new Date(value).format('yyyy-MM-dd HH:mm:ss')
                        }},
                    // {field:'address', title:'所在地区'}
                ]
            },
            cards: {
                columns: [
                    {field:'cardNumber', title:'卡号'},
                    {field:'memberCardType', title:'类型', formatter: function(value) {
                        return value.name;
                    }},
                    {field:'balance', title:'储值余额'},
                    {field:'discount', title:'折扣'},
                    {field:'expireDate', title:'过期时间'}
                ],
                validator: {
                    $instance: {}
                },
                modal: {
                    $instance: {}
                },
                sidemodal: {
                    $instance: {},
                    form: {
                        memberCards: []
                    }
                },
                datagrid: {
                    $instance: {},
                    url: '/api/memberCard',
                    data: [],
                    queryParams: {}
                },
                form: {
                    cardNumber: null,
                    balance: 0,
                    discount: 1,
                    expireDate: null,
                    memberCardType: {}
                }
            },
            cardType: {
                list: []
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
            openAddMemberCardModal: function () {
                this.cards.validator.$instance.removeMark();
                this.cards.form = {
                    memberCardType: {id: null}
                };
                this.cards.modal.$instance.open();
            },
            saveMemberCard: function () {
                var self = this;
                var cardType = null;
                if(!this.cards.form.memberCardType.id) {
                    messager.bubble('请选择会员卡类型！');
                    return;
                }
                $.each(this.cardType.list, function () {
                    if(this.id === self.cards.form.memberCardType.id) {
                        cardType = this;
                    }
                });
                $.ajax({
                    url: utils.patchUrl('/api/memberCard'),
                    contentType: 'application/json',
                    type: 'POST',
                    dataType: 'JSON',
                    data: JSON.stringify($.extend(this.cards.form, {
                        member: self.cards.sidemodal.form,
                        memberCardType: cardType
                    })),
                    success: function() {
                        self.cards.modal.$instance.close();
                        messager.bubble('保存成功！');
                        $.ajax({
                            url: utils.patchUrl('/api/member/' + self.cards.sidemodal.form.id),
                            success: function(data) {
                                self.cards.sidemodal.form = data;
                            }
                        });
                    }
                })
            },
            findMemberCard: function (row) {
                var self = this;
                this.cards.validator.$instance.removeMark();
                $.ajax({
                    url: utils.patchUrl('/api/memberCard/' + row.id),
                    success: function(data) {
                        self.cards.form = data;
                        self.cards.modal.$instance.open();
                    }
                });
            },
            editMemberCard: function (row) {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/api/member/' + row.id),
                    success: function(data) {
                        self.cards.sidemodal.form = data;
                        self.cards.sidemodal.$instance.open();
                    }
                });
            },
            removeMemberCard: function(row, event) {
                var self = this;
                require(['messager'], function(messager) {
                    messager.alert('确定要删除吗？', event, function() {
                        $.ajax({
                            url: utils.patchUrl('/api/memberCard/' + row.id),
                            type: 'DELETE',
                            success: function() {
                                messager.bubble('删除成功');
                                $.ajax({
                                    url: utils.patchUrl('/api/member/' + self.cards.sidemodal.form.id),
                                    success: function(data) {
                                        self.cards.sidemodal.form = data;
                                    }
                                });
                            }
                        });
                    });
                });
            },
            loadCardTypes: function () {
                var self = this;
                $.ajax({
                    url: '/api/memberCardType',
                    success: function (data) {
                        self.cardType.list = data;
                    }
                })
            },
            cardTypeChange: function (val) {
                console.log(val)
            },
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
            this.loadCardTypes();
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
