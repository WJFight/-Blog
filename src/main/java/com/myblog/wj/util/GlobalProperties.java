package com.myblog.wj.util;

import java.util.UUID;

public class GlobalProperties {
    public static final String JWT_SECRET_KEY = UUID.randomUUID().toString();
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final Long JWT_TOKEN_EXPIRTATION = 600L;
    public static final String ROLE_USER = "USER";
    public static final String ROLE_ADMIN = "ADMIN";

}
