package com.myblog.wj.interceptor;


import com.myblog.wj.util.redisandjwt.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.myblog.wj.util.GlobalProperties.JWT_TOKEN_HEADER;

public class JwtInterceptor extends HandlerInterceptorAdapter {
    @Resource
    JwtUtil jwtUtil;

    //    @Resource
//    RedisUtil redisUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        System.out.println("request.getServletPath()==" + request.getServletPath());
        System.out.println("不拦截登陆2" + request.getRequestURI());
        String token = jwtUtil.getTokenByHeader(request);
        System.out.println("token=" + token);
        token = jwtUtil.analysisToken(token);
        if (token != null) {

            if (token != null) {
                response.addCookie(jwtUtil.createCookie(token));
                response.addHeader(JWT_TOKEN_HEADER, token);
                return true;
            } else {
                
                throw new JwtException("身份已经过期！");
            }
        } else {
            throw new JwtException("身份已经过期！");
        }
    }

}
