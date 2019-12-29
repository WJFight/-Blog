package com.myblog.wj.util.security;

import com.myblog.wj.util.redisandjwt.EncryptMd5;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 自定义密码加密和校验
 * 如果rawpassword是null 则是第三方登录省去密码校验；
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
        // System.out.println("rawPassword=" + rawPassword);
        if (rawPassword == null)
            return "qq";
        else
            return EncryptMd5.encode(rawPassword.toString());
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        //  System.out.println("111111111psw=" + encodedPassword + ",11111111111rawpsw=" + rawPassword);
        if (rawPassword.length() > 10)
            return encodedPassword.equals(EncryptMd5.encode(rawPassword.toString()));
        else {

            //  System.out.println("psw=" + encodedPassword + ",rawpsw=" + rawPassword);
//            rawPassword=encodedPassword;
            return true;
        }
    }
}
