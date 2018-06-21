package com.kratos.common.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CacheUtils {
    private static CacheUtils cacheUtils;
    private static Map<String, Object> hashMap = new HashMap<>();
    private CacheUtils() {

    }

    public static CacheUtils getInstance() {
        if(cacheUtils == null) {
            cacheUtils = new CacheUtils();
        }
        return cacheUtils;
    }

    public void add(String key, Object value) {
        hashMap.put(key, value);
    }

    public void add(String key, Object value, Date expireIn) {
        hashMap.put(key, value);
    }

    public void set(String key, Object value) {
        hashMap.put(key, value);
    }

    public void remove(String key) {
        hashMap.remove(key);
    }

    public Object get(String key) {
        return hashMap.get(key);
    }
}
