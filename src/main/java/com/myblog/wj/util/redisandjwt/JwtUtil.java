package com.myblog.wj.util.redisandjwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.wj.entity.personalUser.BlogUser;
import com.myblog.wj.service.personalUser.BlogUserService;
import com.myblog.wj.util.GlobalProperties;
import com.myblog.wj.util.JWTException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Component
public class JwtUtil {
    public static boolean flag = false;//判断redis中token是否过期；
    public static Date expireDate = null;
    public static Date startDate = null;
    public static String username = null;
    public static Claims claims = null;
    private static String token = null;
    public final String JWT_SECRET_KEY = "wangJian";
    public final String JWT_TOKEN_HEADER = "Authorization";
    public final int JWT_TOKEN_EXPIRTATION = 3600 * 168;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private BlogUserService blogUserService;

    //生产token的cookie
    public Cookie createCookie(String token) {
        Cookie cookie = new Cookie(GlobalProperties.JWT_TOKEN_HEADER, token);
        cookie.setHttpOnly(false);
        cookie.setMaxAge(JWT_TOKEN_EXPIRTATION);
        cookie.setPath("/");
        return cookie;
    }

    //获取请求头中的token
    public String getTokenByHeader(HttpServletRequest request) {

        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                System.out.println("cookie=" + c.getName());
                if (GlobalProperties.JWT_TOKEN_HEADER.equals(c.getName().trim())) {
                    token = c.getValue();
                    System.out.println("token=" + token);
                    return token;
                }
            }
        }
//        try {
//            throw new ServletException("身份过期");
//        } catch (ServletException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    public String createToken(BlogUser user) {
        ObjectMapper objectMapper = new ObjectMapper();
        //设置token头部
        Map<String, Object> map = new HashMap<>();
        map.put("type", "JWT");
        map.put("alg", "HS256");
        //设置私有声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("user_id", user.getUser_id().toString());
        claims.put("user_realname", user.getUser_realname());
        claims.put("username", user.getUsername());
        //设置响应头token中有效时间为7天//如果连续访问有效时间过半这重新生成token
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 7);
        expireDate = calendar.getTime();
        String token = Jwts.builder()
                .setClaims(claims)
                .setHeader(map)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expireDate)
                .setSubject(user.getUser_id().toString())
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET_KEY)
                .compact();
        System.out.println("token=" + token);
//        System.out.println("user:" + user);
        //设置redis中token有效时间为2h,同时设置响应头中token是否重新生成(不用用户输入账号密码偷换token从而达到响应头token有效时间刷新)的判断时间超过4天就重新生成token
        redisUtil.setKey(token, user.getUser_realname());
        redisUtil.setExpire(token, 7, TimeUnit.DAYS);
        //作为是否刷新响应头中token的有效时间的依据
        redisUtil.setKey(user.getUsername(), token);
        redisUtil.setExpire(user.getUsername(), JWT_TOKEN_EXPIRTATION, TimeUnit.HOURS);

        System.out.println("取出token=" + redisUtil.getKey(token));
        return token;
    }

    //解析token
    public String analysisToken(String token) throws JWTException {
        claims = null;
        if (token != null) {
            try {
                claims = claims(token);
                System.out.println("来到这里了此时token=" + token);
                //刷新redis中token的过期时间同时判断是否重新生成新token
                return refreshToken(token, JWT_TOKEN_EXPIRTATION, TimeUnit.HOURS, claims);
            } catch (SignatureException e) {
                throw new JWTException("身份验证失败！" + e.getMessage());

            } catch (ExpiredJwtException e) {
                redisUtil.deleteKey(token);
                throw new JWTException("身份过期" + e.getMessage());
            }
        } else {
            return null;
        }
    }

    public Claims claims(String token) {
        claims = null;
        if (token != null) {
            claims = Jwts.parser()
                    .setSigningKey(JWT_SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } else return null;
    }

    //判断redis中token是否过期
    public boolean isExpiret(String token) {
        String user_name = redisUtil.getKey(token);
        if (user_name == null || "".equals(user_name.trim())) {
            return false;
        }
        return true;
    }

    //刷新redis中的token，同时判断响应头中的token
    public String refreshToken(String token, long expiret, TimeUnit timeType, Claims claims) throws JWTException {
        username = null;
        //判断redis中token是否过期
        flag = isExpiret(token);
        if (flag) {
            //如果在redis中的token没过期
            // 判断响应头中的token有效时间 如果响应头中的token有效时间过半则重新生成token
            username = claims.get("username").toString();
            if (redisUtil.getKey(username) == null) {
                token = createToken(blogUserService.selectBlogUserByUsername(username));
                System.out.println("重新生成token=" + token);
            }
            redisUtil.setExpire(token, JWT_TOKEN_EXPIRTATION, TimeUnit.HOURS);
            return token;
        } else {
            throw new JWTException("身份过期！");
        }
    }

    //获取登陆状态的用户信息
    public String getUsername(String token) {
        String username = (String) claims(token).get("username");
        return username;
    }

    //获取用户id
    public Integer getUserID(HttpServletRequest request) {
        token = getTokenByHeader(request);
        if (token != null) {
            return Integer.parseInt(claims(token).get("user_id").toString());
        } else return null;
    }

    //销毁token和缓存
    public void destroyToken(String token) {
//        claims = null;
//        claims = claims(token);
        redisUtil.deleteKey(token);
        redisUtil.deleteKey(getUsername(token));
    }

}
