package com.kratos.common.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 digest.
 *
 * @author Yang XuePing
 */
public class MD5Utils {
    private MD5Utils() {
    }
    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
    public static byte[] digestBytes(String... infos) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            for (String info : infos) {
                messageDigest.update(info.getBytes());
            }
        } catch (NoSuchAlgorithmException e) {
            // can't occur
        }

        return messageDigest.digest();
    }

    public static String digestHexString(String... infos) {
        return EncodeUtils.encodeHex(digestBytes(infos));
    }

    public static void main(String[] args) {
        System.out.println(MD5Utils.digestHexString("admin"));
    }
}
