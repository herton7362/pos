package com.framework.module.klt.util;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @ClassName: SignUtils
 * @Description 签名工具类
 * @Author liujinjian
 * @Date 2018/8/6 13:39
 * @Version 1.0
 */
public class SignUtils {

    /**
     * @param orignStr 签名原串
     * @param md5Key   商户md5Key
     * @return 加签串
     */
    public static String addSign(String orignStr, String md5Key) {
        JSONObject jsonObject = JSONObject.parseObject(orignStr);
        JSONObject headJson = jsonObject.getJSONObject("head");
        JSONObject contentJson = jsonObject.getJSONObject("content");

        //放入TreeMap后,按照Key值自然排序！
        Map signMap = new TreeMap();
        //报文头
        for (Map.Entry entry : headJson.entrySet()) {
            signMap.put(entry.getKey(), entry.getValue());
        }
        //报文体
        for (Map.Entry entry : contentJson.entrySet()) {
            signMap.put(entry.getKey(), entry.getValue());
        }

        StringBuilder stringBuilder = new StringBuilder();
        Set<Map.Entry<String, String>> entrySet = signMap.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null && !((String) entry.getValue()).trim().equals(""))
                stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }

        //删除最后一个&号
        String signOrgin = stringBuilder.substring(0, stringBuilder.length() - 1);
        //签名值
        String tSign = MD5Utils.getSign(signOrgin, md5Key);
        //添加签名
        jsonObject.getJSONObject("head").put("sign", tSign);
        return jsonObject.toJSONString();
    }

}
