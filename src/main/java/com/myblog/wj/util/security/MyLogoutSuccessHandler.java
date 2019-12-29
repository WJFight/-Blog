package com.myblog.wj.util.security;

import com.myblog.wj.util.redisandjwt.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注销成功处理类
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Resource
    JwtUtil jwtUtil;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String token = request.getHeader(jwtUtil.JWT_TOKEN_HEADER);
//        jwtUtil.destroyToken(token);
//        response.addHeader(jwtUtil.JWT_TOKEN_HEADER, null);
    }
}
