package com.myblog.wj.util.security;

//import com.myblog.wj.util.redisandjwt.EncryptMd5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 在认证UsernamePasswordFilter之前进行过滤
 */

public class MyOnceFilter extends OncePerRequestFilter {
    @Resource
    RedisUtil redisUtil;
    PasswordEncoder passwordEncoder = new SCryptPasswordEncoder();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        boolean flag = false;
        String imagcode = null;
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null) {
//            System.out.println("cc=" + cookies);
            for (Cookie c : cookies) {
                if (c.getName().equals("imagecode")) {
                    imagcode = redisUtil.getKey(c.getValue());
                }
                if ("Authorization".equals(c.getName())) {
                    token = c.getValue();
                }
            }
        }

        //  System.out.println("请求url:" + request.getRequestURI());
//        System.out.println("来到MyOnceFilter imagCode==" + imagcode);
        String captcha = request.getParameter("captcha");
        if (captcha == null) captcha = "";
        if (request.getRequestURI().equals("/visit/loginBlogUser")) {

            if (imagcode.equals(captcha) || token != null) {
//                if (!(request.getParameter("password")==null)){
//                    request.setAttribute("password",null);
//                }
                doFilter(request, response, filterChain);
            } else {
                Map result = new HashMap<>();
                result.put("msg", "codeError");
                result.put("code", "1");
                response.setCharacterEncoding("UTF-8");
                response.setContentType("text/json;charset=utf-8");
                response.getWriter().write(new ObjectMapper().writeValueAsString(result));
            }
        } else {
            if (request.getRequestURI().equals("/visit/registe")) {
//                System.out.println("visit/registe");
                if (imagcode.equals(captcha)) {
//                    System.out.println("验证成功！");
                    doFilter(request, response, filterChain);
                } else {
//                    System.out.println("验证失败！");
                    Map result = new HashMap<>();
                    result.put("msg", "codeError");
                    result.put("code", "1");
                    response.setCharacterEncoding("UTF-8");
                    response.setContentType("text/json;charset=utf-8");

                    response.getWriter().write(new ObjectMapper().writeValueAsString(result));
                }
            } else {

                doFilter(request, response, filterChain);
            }

        }

    }
}
