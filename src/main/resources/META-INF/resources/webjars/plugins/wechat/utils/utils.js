define(['app'], function(APP) {
    var utils = APP.utils;
    utils.getLoginMember = function (callback, forceLogin) {
        require(['jquery','messager'], function ($, messager) {
            $.ajax({
                url: utils.patchUrl('/user/info'),
                cache: false,
                success: function (member) {
                    if(member && member.userType === 'MEMBER') {
                        callback(member);
                    } else {
                        messager.alert('您尚未登录，将会前往登录。', function () {
                            window.location.href = utils.patchUrlPrefixUrl(window._appConf.ctx + window._appConf.loginUrl);
                        }, function () {
                            if(forceLogin) {
                                window.location.href = utils.patchUrlPrefixUrl(window._appConf.ctx + window._appConf.loginUrl);
                            }
                        });
                    }
                }
            })
        });
    };
    return utils;
});