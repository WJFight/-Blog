package com.myblog.wj.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.wj.service.personalUser.BlogUserService;
import com.myblog.wj.util.GlobalProperties;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证成功处理类
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Resource
    JwtUtil jwtUtil;
    @Resource
    BlogUserService blogUserService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = null;
        if (principal != null) {
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                String username = userDetails.getUsername();
                //    System.out.println("用户username=" + username);
                token = jwtUtil.createToken(blogUserService.selectBlogUserByUsername(username));
            }
        }
        // System.out.println("来到onAuthenticationSuccess============================");
        //  System.out.println("token===" + token);
        Map result = new HashMap<>();
        result.put("code", 0);

        result.put(GlobalProperties.JWT_TOKEN_HEADER, token);
        response.setCharacterEncoding("UTF-8");

//        Cookie cookie = new Cookie(GlobalProperties.JWT_TOKEN_HEADER, token);
//        cookie.setHttpOnly(false);
//        cookie.setMaxAge(60 * 60);
//        cookie.setPath("/");
        response.addCookie(jwtUtil.createCookie(token));
        response.setHeader(GlobalProperties.JWT_TOKEN_HEADER, token);

//        response.setHeader(GlobalProperties.JWT_TOKEN_HEADER, token);
        response.setStatus(200);
        response.getWriter().write(objectMapper.writeValueAsString(result));
//        request.getRequestDispatcher("/user/index").forward(request,response);

    }
}
