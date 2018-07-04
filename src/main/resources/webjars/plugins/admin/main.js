require(['jquery', 'vue', 'utils', 'adminlte'], function ($, Vue, utils){
    if($('.sidebar-menu').length > 0) {
        // menu
        var vueSideBar = new Vue({
            el: '.sidebar',
            data: {
                list: [],
                admin: {
                    headPhoto: {},
                    roles: [{}]
                }
            },
            filters: {
                imgPath: function (id) {
                    if(id) {
                        return utils.patchUrl('/attachment/download/' + id);
                    } else {
                        return null;
                    }
                }
            },
            mounted: function() {
                $('.sidebar-menu').tree();
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/user/info'),
                    cache: false,
                    success: function (user) {
                        self.admin = user;
                    }
                })
            },
            methods: {
                logout: function() {
                    window.localStorage.accessToken = null;
                    window.localStorage.expiration = null;
                    window.localStorage.refreshToken = null;
                    window.top.location.href = window._appConf.ctx + window._appConf.loginUrl
                }
            }
        });

        // header
        new Vue({
            el: '.main-header',
            data: {
                admin: {
                    headPhoto: {},
                    roles: [{}]
                }
            },
            filters: {
                imgPath: function (id) {
                    if(id) {
                        return utils.patchUrl('/attachment/download/' + id);
                    } else {
                        return null;
                    }
                }
            },
            methods: {
                logout: function() {
                    window.localStorage.accessToken = null;
                    window.localStorage.expiration = null;
                    window.localStorage.refreshToken = null;
                    window.top.location.href = utils.patchUrlPrefixUrl(window._appConf.ctx + window._appConf.loginUrl);
                }
            },
            mounted: function () {
                var self = this;
                $.ajax({
                    url: utils.patchUrl('/user/info'),
                    cache: false,
                    success: function (user) {
                        self.admin = user;
                    }
                })
            }
        });

        // 每隔1个小时重新获取一次token
        function refreshToken() {
            if(new Date().getTime() > window.localStorage.expiration - 10 * 1000) {
                $.ajax({
                    url: utils.patchUrl('/refresh/token?appId='+_appConf.appId+'&appSecret='+_appConf.appSecret+'&refreshToken=' + window.localStorage.refreshToken),
                    contentType: 'application/json',
                    type: 'POST',
                    cache: false,
                    success: function(data) {
                        window.localStorage.accessToken = data['access_token'];
                        window.localStorage.expiration = new Date().getTime() + ((data['expires_in'] / 2) * 1000);
                    }
                });
            }
        }
        refreshToken();
        setInterval(refreshToken, 3 * 1000);

        $.ajax({
            url: utils.patchUrl('/api/admin/menus'),
            cache: false,
            success: function(data) {
                var parent, pathname = location.pathname;
                var roots = utils.treeDataConverter(data);

                vueSideBar.list = roots;
                $.each(vueSideBar.list, function(){
                    parent = this;
                    this.active = this.url === pathname;
                    this.children = this.children || [];
                    $.each(this.children, function() {
                        this['url'] = utils.patchUrlPrefixUrl(this['url']);
                        if(this['url'] === pathname) {
                            parent.active = true;
                            this['active'] = true;
                        }
                    });
                });

            }
        });
    }
});
