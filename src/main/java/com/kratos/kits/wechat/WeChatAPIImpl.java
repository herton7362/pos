package com.kratos.kits.wechat;

import com.kratos.common.utils.*;
import com.kratos.exceptions.BusinessException;
import com.kratos.kits.wechat.config.annotation.builder.WeChatPayConfig;
import com.kratos.kits.wechat.config.annotation.configuration.WeChatAPIConfigurer;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.*;

@Component
public class WeChatAPIImpl implements WeChatAPI {
    private static final Logger LOG = LoggerFactory.getLogger(WeChatAPI.class);
    private final WeChatPayConfig weChatPayConfig = new WeChatPayConfig();

    private CacheUtils cache = CacheUtils.getInstance();

    public Map<String, Object> makeAppUnifiedOrder(String orderNumber, HttpServletRequest request, Integer totalFee) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String str = genProductArgs(totalFee, WeChatTradeType.APP, request, orderNumber, null);

        HttpEntity<String> formEntity = new HttpEntity<>(str, headers);
        String result = restTemplate.postForObject("https://api.mch.weixin.qq.com/pay/unifiedorder", formEntity,
                String.class);
        LOG.debug("pay unifiedorder result: {}", result);
        String prePayId = (String) XmlUtils.xmltoMap(result).get("prepay_id");
        Map<String, Object> map = new HashMap<>();
        map.put("appid", weChatPayConfig.getAppId());
        map.put("package", "Sign=WXPay");
        map.put("noncestr", MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes())); // 获取32位随机码
        map.put("partnerid", weChatPayConfig.getMchId());
        map.put("prepayid", prePayId);
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        String sign = getSign(map);
        map.put("sign", sign);
        map.remove("package");
        map.put("pkg", "Sign=WXPay");
        map.put("retcode", "0");
        map.put("retmsg", "ok");
        return map;
    }

    @Override
    public Map<String, Object> makeWebUnifiedOrder(String orderNumber, HttpServletRequest request, Integer totalFee) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        String str = genProductArgs(totalFee, WeChatTradeType.MWEB, request, orderNumber, null);

        HttpEntity<String> formEntity = new HttpEntity<>(str, headers);
        String result = restTemplate.postForObject("https://api.mch.weixin.qq.com/pay/unifiedorder", formEntity,
                String.class);
        LOG.debug("pay unifiedorder result: {}", result);
        String mwebUrl = (String) XmlUtils.xmltoMap(result).get("mweb_url");
        Map<String, Object> map = new HashMap<>();
        map.put("mwebUrl", mwebUrl);
        return map;
    }

    public Map<String, Object> makeUnifiedOrder(String orderNumber, HttpServletRequest request, String openid, Integer totalFee) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());

        String str = genProductArgs(totalFee, WeChatTradeType.JSAPI, request, orderNumber, openid);
        LOG.debug("pay Product Args: {}", str);
        HttpEntity<String> formEntity = new HttpEntity<>(str, headers);
        String result = restTemplate.postForObject("https://api.mch.weixin.qq.com/pay/unifiedorder", formEntity,
                String.class);
        LOG.debug("pay unifiedorder result: {}", result);
        String prePayId = (String) XmlUtils.xmltoMap(result).get("prepay_id");

        Map<String, Object> map = new HashMap<>();
        map.put("appId", weChatPayConfig.getAppId());
        map.put("timeStamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("nonceStr", MD5Utils.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes())); // 获取32位随机码
        map.put("package", "prepay_id="+prePayId);
        map.put("signType", "MD5");

        String sign = getSign(map);
        map.put("sign", sign);
        return map;
    }

    public UserInfo getUserInfo(String code) throws Exception {
        AccessToken accessToken = getAccessToken(code);
        Map<String, Object> param = new HashMap<>();
        param.put("access_token", accessToken.getAccessToken());
        param.put("openid", accessToken.getOpenid());
        param.put("lang", "zh_CN");
        JSONObject jsonObject = sendGet("https://api.weixin.qq.com/sns/userinfo", param);
        String  openid = (String) jsonObject.get("openid");
        String  nickname = (String) jsonObject.get("nickname");
        Integer  sex = (Integer) jsonObject.get("sex");
        String  province = (String) jsonObject.get("province");
        String  city = (String) jsonObject.get("city");
        String  country = (String) jsonObject.get("country");
        String  headimgurl = (String) jsonObject.get("headimgurl");
        String  unionid = (String) jsonObject.get("unionid");
        UserInfo userInfo = new UserInfo();
        userInfo.setOpenid(openid);
        userInfo.setNickname(nickname);
        userInfo.setSex(sex);
        userInfo.setProvince(province);
        userInfo.setCity(city);
        userInfo.setCountry(country);
        userInfo.setHeadimgurl(headimgurl);
        userInfo.setUnionid(unionid);
        return userInfo;
    }

    /**
     * 创建产品订单参数,构造预支付信息
     *
     * @return
     */
    private String genProductArgs(Integer totalFee, WeChatTradeType tradeType, HttpServletRequest request, String orderNumber, String openid) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("appid", weChatPayConfig.getAppId());
        map.put("body", "weixin");
        if(StringUtils.isNotEmpty(openid)) {
            map.put("openid", openid);
        }
        map.put("mch_id", weChatPayConfig.getMchId());
        map.put("nonce_str", UUID.randomUUID().toString().replaceAll("-", "")); // 获取32位随机码
        map.put("notify_url", weChatPayConfig.getNotifyUrl());
        map.put("out_trade_no", orderNumber);
        map.put("spbill_create_ip", NetworkUtils.getIpAddress(request));// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP
        map.put("total_fee", totalFee);
        map.put("trade_type", tradeType.name());
        String sign = getSign(map);
        map.put("sign", sign);
        return XmlUtils.mapToXml(map);
    }

    /**
     * 对参数信息进行签名
     *
     * @param map 待签名授权信息
     *
     * @return 签名授权信息
     */
    private String getSign(Map<String, Object> map) throws Exception {
        List<String> keys = new ArrayList<>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        keys.forEach(key -> {
            Object value = map.get(key);
            authInfo.append(key);
            authInfo.append("=");
            authInfo.append(value);
            authInfo.append("&");
        });
        authInfo.append("key=");
        authInfo.append(weChatPayConfig.getApiKey());
        return MD5(authInfo.toString()).toUpperCase();
    }

    /**
     * 生成 MD5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    private String MD5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] array = md.digest(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
        }
        return sb.toString().toUpperCase();
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    private JSONObject sendGet(String url, Map<String, Object> param) throws Exception {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + paramMapToString(param);
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOG.error("发送微信请求出现异常", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        JSONObject jsonObject = new JSONObject(result);
        if(jsonObject.has("errcode") && !"0".equals(String.valueOf(jsonObject.get("errcode")))) {
            LOG.error("微信通讯出现异常，errcode:{},errmsg:{}", jsonObject.get("errcode"), jsonObject.get("errmsg"));
            throw new BusinessException("微信通讯出现异常，请联系管理员");
        }
        return jsonObject;
    }

    private String paramMapToString(Map<String, Object> param) throws Exception {
        if(param == null) {
            return "";
        }
        Iterator entryIterator = param.entrySet().iterator();
        Map.Entry entry;
        StringBuilder stringBuilder = new StringBuilder();
        while (entryIterator.hasNext()) {
            entry = (Map.Entry) entryIterator.next();
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(entry.getValue());
            stringBuilder.append("&");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    private AccessToken getAccessToken () throws Exception {
        if(cache.get("weChatToken") != null) {
            AccessToken accessToken = (AccessToken) cache.get("weChatToken");
            Date createdDate = accessToken.getCreatedDate();
            if(new Date().getTime() - createdDate.getTime() < (accessToken.getExpiresIn() * 1000)) {
                return accessToken;
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("grant_type", GrantType.client_credential.name());
        param.put("appid", weChatPayConfig.getAppId());
        param.put("secret", weChatPayConfig.getAppSecret());
        JSONObject jsonObject = sendGet("https://api.weixin.qq.com/cgi-bin/token", param);
        String token = (String) jsonObject.get("access_token");
        Integer  expires = (Integer ) jsonObject.get("expires_in");
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(token);
        accessToken.setExpiresIn(expires);
        accessToken.setCreatedDate(new Date());
        cache.set("weChatToken", accessToken);
        return accessToken;
    }

    public JsapiTicket getSignature() throws Exception {
        if(cache.get("weChatJsapiTicket") != null) {
            JsapiTicket jsapiTicket = (JsapiTicket) cache.get("weChatJsapiTicket");
            Date createdDate = jsapiTicket.getCreatedDate();
            if(new Date().getTime() - createdDate.getTime() < (jsapiTicket.getExpiresIn() * 1000)) {
                return jsapiTicket;
            }
        }
        AccessToken accessToken = getAccessToken();
        String token = accessToken.getAccessToken();
        Map<String, Object> param = new HashMap<>();
        param.put("access_token", token);
        param.put("type", "jsapi");
        JSONObject jsonObject = sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket", param);
        JsapiTicket jsapiTicket = new JsapiTicket();
        jsapiTicket.setTicket((String) jsonObject.get("ticket"));
        jsapiTicket.setExpiresIn((Integer) jsonObject.get("expires_in"));
        jsapiTicket.setCreatedDate(new Date());
        cache.set("weChatJsapiTicket", jsapiTicket);
        return jsapiTicket;
    }

    private AccessToken getAccessToken(String code) throws Exception {
        if(cache.get(code) != null) {
            AccessToken accessToken = (AccessToken) cache.get(code);
            Date createdDate = accessToken.getCreatedDate();
            if(new Date().getTime() - createdDate.getTime() < (accessToken.getExpiresIn() * 1000)) {
                return accessToken;
            }
        }
        Map<String, Object> param = new HashMap<>();
        param.put("appid", weChatPayConfig.getAppId());
        param.put("secret", weChatPayConfig.getAppSecret());
        param.put("code", code);
        param.put("grant_type", GrantType.authorization_code.name());
        JSONObject jsonObject = sendGet("https://api.weixin.qq.com/sns/oauth2/access_token", param);
        String token = (String) jsonObject.get("access_token");
        Integer  expires = (Integer ) jsonObject.get("expires_in");
        String  refreshToken = (String ) jsonObject.get("refresh_token");
        String  openid = (String ) jsonObject.get("openid");
        String  scope = (String ) jsonObject.get("scope");
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(token);
        accessToken.setExpiresIn(expires);
        accessToken.setCreatedDate(new Date());
        accessToken.setRefreshToken(refreshToken);
        accessToken.setOpenid(openid);
        accessToken.setScope(scope);
        cache.set(code, accessToken);
        return accessToken;
    }

    /**
     * Created by He on 2017/7/14.
     */
    public class AccessToken implements Serializable {
        private String accessToken;
        private Integer expiresIn;
        private Date createdDate;
        private String refreshToken;
        private String openid;
        private String scope;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    /**
     * 微信用户信息
     * Created by He on 2017/7/17.
     */
    public class UserInfo {
        private String openid; // 用户的唯一标识
        private String nickname; // 用户昵称
        private Integer sex; // 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
        private String province; // 用户个人资料填写的省份
        private String city; // 普通用户个人资料填写的城市
        private String country; // 国家，如中国为CN
        private String headimgurl; // 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
        private String unionid; // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public Integer getSex() {
            return sex;
        }

        public void setSex(Integer sex) {
            this.sex = sex;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getHeadimgurl() {
            return headimgurl;
        }

        public void setHeadimgurl(String headimgurl) {
            this.headimgurl = headimgurl;
        }

        public String getUnionid() {
            return unionid;
        }

        public void setUnionid(String unionid) {
            this.unionid = unionid;
        }

        public String getSexDisplayName() {
            Map<Integer, String> map = new HashMap<>();
            map.put(0, "未知");
            map.put(1, "男");
            map.put(2, "女");
            return map.get(sex);
        }
    }

    /**
     * Created by He on 2017/5/27.
     */
    public enum WeChatTradeType {
        JSAPI,// 公众号支付
        NATIVE,// 原生扫码支付
        APP,// app支付
        MICROPAY,// 刷卡支付
        MWEB
    }

    /**
     * Created by He on 2017/7/14.
     */
    public class JsapiTicket implements Serializable {
        public String ticket;
        public Integer expiresIn;
        private Date createdDate;

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(Date createdDate) {
            this.createdDate = createdDate;
        }
    }

    /**
     * Created by He on 2017/7/18.
     */
    public enum GrantType {
        authorization_code,
        client_credential
    }

    @Autowired
    public WeChatAPIImpl(WeChatAPIConfigurer weChatAPIConfigurer) {
        weChatAPIConfigurer.configure(weChatPayConfig);
    }
}
