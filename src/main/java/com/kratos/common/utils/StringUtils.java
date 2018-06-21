package com.kratos.common.utils;

/**
 * Created by He on 2017/4/28.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    public static String toFirstCharUpperCase(String source) {
        char[] chars = source.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return String.valueOf(chars);
    }
}
