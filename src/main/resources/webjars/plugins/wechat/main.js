require(['jquery', 'vue', 'utils', 'adminlte', 'weui'], function ($, Vue, utils){
// 每隔1个小时重新获取一次token
    function refreshToken() {
        if(window.localStorage.refreshToken) {
            if(new Date().getTime() > window.localStorage.expiration - 10 * 1000) {
                $.ajax({
                    url: utils.patchUrl('/oauth/token?client_id='+_appConf.appId+'&client_secret='+_appConf.appSecret+'&grant_type=refresh_token&refresh_token=' + window.localStorage.refreshToken),
                    contentType: 'application/json',
                    headers: {
                        Authorization: "Basic " + btoa(_appConf.appId + ':' + _appConf.appSecret)
                    },
                    type: 'POST',
                    cache: false,
                    success: function(data) {
                        window.localStorage.accessToken = data['access_token'];
                        window.localStorage.expiration = new Date().getTime() + ((data['expires_in'] / 2) * 1000);
                    }
                });
            }
        }
    }
    refreshToken();
    setInterval(refreshToken, 3 * 1000);
});
