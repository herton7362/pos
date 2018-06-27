define(['jquery'], function($) {
    var APP = {};
    APP.utils = {};
    APP.support = {};
    var $win = $(window);

    /**
     * Debounce function
     *
     * @param {function} func  Function to be debounced
     * @param {number} wait Function execution threshold in milliseconds
     * @param {bool} immediate  Whether the function should be called at
     *                          the beginning of the delay instead of the
     *                          end. Default is false.
     * @description Executes a function when it stops being invoked for n seconds
     * @see  _.debounce() http://underscorejs.org
     */
    APP.utils.debounce = function(func, wait, immediate) {
        var timeout;
        return function() {
            var context = this;
            var args = arguments;
            var later = function() {
                timeout = null;
                if (!immediate) {
                    func.apply(context, args);
                }
            };
            var callNow = immediate && !timeout;

            clearTimeout(timeout);
            timeout = setTimeout(later, wait);

            if (callNow) {
                func.apply(context, args);
            }
        };
    };

    APP.utils.isInView = function(element, options) {
        var $element = $(element);
        var visible = !!($element.width() || $element.height()) &&
            $element.css('display') !== 'none';

        if (!visible) {
            return false;
        }

        var windowLeft = $win.scrollLeft();
        var windowTop = $win.scrollTop();
        var offset = $element.offset();
        var left = offset.left;
        var top = offset.top;

        options = $.extend({topOffset: 0, leftOffset: 0}, options);

        return (top + $element.height() >= windowTop &&
            top - options.topOffset <= windowTop + $win.height() &&
            left + $element.width() >= windowLeft &&
            left - options.leftOffset <= windowLeft + $win.width());
    };

    // handle multiple browsers for requestAnimationFrame()
    // http://www.paulirish.com/2011/requestanimationframe-for-smart-animating/
    // https://github.com/gnarf/jquery-requestAnimationFrame
    APP.utils.rAF = (function() {
        return window.requestAnimationFrame ||
            window.webkitRequestAnimationFrame ||
            window.mozRequestAnimationFrame ||
            window.oRequestAnimationFrame ||
            // if all else fails, use setTimeout
            function(callback) {
                return window.setTimeout(callback, 1000 / 60); // shoot for 60 fps
            };
    })();

    APP.utils.formatMoney = function (s, n) {
        if (!s && s !== 0) {
            return '';
        }
        if (n === undefined) {
            n = 2;
        }
        n = n > 0 && n <= 20 ? n : 2;
        s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";
        var l = s.split(".")[0].split("").reverse(),
            r = s.split(".")[1];
        var t = "";
        for (var i = 0; i < l.length; i++) {
            t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");
        }
        if (t.split("").reverse().join("")) {
            return t.split("").reverse().join("") + "." + r;
        } else {
            return '';
        }
    };

    APP.utils.isNull = function(data) {
        return data === undefined || data === null;
    };

    APP.utils.patchUrlPrefixUrl = function (url) {
        return window._appConf.ctx + url;
    };

    APP.utils.patchUrl = function (url) {
        if (url.indexOf('?') >= 0) {
            url += '&access_token=' + window.localStorage.accessToken;
        } else {
            url += '?access_token=' + window.localStorage.accessToken;
        }
        return APP.utils.patchUrlPrefixUrl(url);
    };

    APP.utils.treeDataConverter = function (data) {
        var map = {}, node, roots = [];
        $.each(data, function() {
            map[this.id] = this;
            if(!this.parent || !this.parent.id) {
                roots.push(this);
            }
        });
        for(var key in map) {
            node = map[key];
            if(!node.parent) {
                continue;
            }
            var parent = map[node.parent.id];
            if(!parent) {
                roots.push(node);
                continue;
            }
            parent.children = parent.children || [];
            if($.inArray(node, parent.children) < 0){
                parent.children.push(node);
            }
        }
        return roots;
    };

    APP.utils.bytesToSize = function (bytes) {
        if (!bytes) return '0 B';

        var k = 1024;

        var sizes = ['B','KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

        var i = Math.floor(Math.log(bytes) / Math.log(k));

        return (bytes / Math.pow(k, i)).toFixed(2) + ' ' + sizes[i];
    };

    APP.utils.getQueryString = function (name) {
        var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if(r !== null)return  decodeURI(r[2]); return null;
    };

    APP.support.touch = (
        ('ontouchstart' in window &&
            navigator.userAgent.toLowerCase().match(/mobile|tablet/)) ||
        (window.DocumentTouch && document instanceof window.DocumentTouch) ||
        (window.navigator['msPointerEnabled'] &&
            window.navigator['msMaxTouchPoints'] > 0) || // IE 10
        (window.navigator['pointerEnabled'] &&
            window.navigator['maxTouchPoints'] > 0) || // IE >=11
        false);

    APP.support.mobile = $win.width() < 768;

    // https://github.com/Modernizr/Modernizr/blob/924c7611c170ef2dc502582e5079507aff61e388/feature-detects/forms/validation.js#L20
    APP.support.formValidation = (typeof document.createElement('form').
        checkValidity === 'function');
    return APP
});

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.format = function(fmt)
{ //author: meizz
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "H+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}

Date.prototype.formatPretty = function() {
    var second = 1000;
    var minutes = second*60;
    var hours = minutes*60;
    var days = hours*24;
    var months = days*30;
    var myDate = this;
    var nowtime = new Date();
    var longtime =nowtime.getTime()- myDate.getTime();
    if( longtime > months*2 ) {
        return this.format("yyyy-MM-dd HH:mm:ss");
    }
    else if (longtime > months)
    {
        return "1个月前";
    }
    else if (longtime > days*7)
    {
        return ("1周前");
    }
    else if (longtime > days)
    {
        return (Math.floor(longtime/days)+"天前");
    }
    else if ( longtime > hours)
    {
        return (Math.floor(longtime/hours)+"小时前");
    }
    else if (longtime > minutes)
    {
        return (Math.floor(longtime/minutes)+"分钟前");
    }
    else if (longtime > second)
    {
        return (Math.floor(longtime/second)+"秒前");
    }else
    {
        return (longtime+" error ");
    }
};