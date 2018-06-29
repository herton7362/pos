require(['jquery', 'vue', 'utils', 'messager'], function ($, Vue, utils, messager) {
    new Vue({
        el: '#content',
        data: {
            username: null,
            password: null,
            verifyCode: null,
            invitePersonMobile: null,
            appId: 'tonr',
            appSecret: 'secret',
            countDownSecond: 0
        },
        methods: {
            checkTelExist: function () {
                var self = this;
                if(!this.username) {
                    return;
                }
                $.ajax({
                    url: utils.patchUrlPrefixUrl('/user/mobile/' + this.username),
                    cache: false,
                    success: function (data) {
                        if(data) {
                            self.pageType = '使用手机号密码登录';
                        } else {
                            self.pageType = '注册用户';
                        }
                    }
                })
            },
            register: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrlPrefixUrl('/register'),
                    data: {
                        mobile: this.username,
                        code: this.verifyCode,
                        password: this.password,
                        invitePersonMobile: this.invitePersonMobile
                    },
                    type: 'POST',
                    success: function () {
                        messager.bubble('注册成功，前往登录');
                        self.pageType = '使用手机号密码登录';
                    }
                })
            },
            sendVerifyCode: function () {
                var self = this;
                disableVerifyButton();
                function disableVerifyButton() {
                    self.countDownSecond = 60;
                    var interval = setInterval(function () {
                        self.countDownSecond--;
                        if(self.countDownSecond === 0) {
                            clearInterval(interval);
                        }
                    }, 1000);
                }
                $.ajax({
                    url: utils.patchUrlPrefixUrl('/verifyCode'),
                    cache: false,
                    data: {
                        mobile: this.username
                    }
                })
            }
        },
        mounted: function () {
            this.invitePersonMobile = utils.getQueryString("mobile");
        }
    });
});
