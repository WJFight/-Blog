package com.myblog.wj.util.redisandjwt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 密码+盐加密
 */
public class EncryptMd5 {
    private final static String solt = "WJ";
    private static String str;

    public static String encode(String password) {
        password += solt;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(password.getBytes("UTF-8"));
            byte[] bytes = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                str = Integer.toHexString(bytes[i] & 0xFF);
                if (str.length() < 2) {
                    sb.append(0);
                }
                sb.append(str);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
