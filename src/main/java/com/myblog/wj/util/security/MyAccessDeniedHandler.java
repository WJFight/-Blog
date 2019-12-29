package com.myblog.wj.util.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限不足处理类
 */
public class MyAccessDeniedHandler implements AccessDeniedHandler {

    private ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        判断是否是异步请求
        System.out.println("handle===================");
        String asyncRequest = request.getHeader("x-requested-with");
        Map result = new HashMap();
        response.setCharacterEncoding("UTF-8");
        if (!StringUtils.isEmpty(asyncRequest) && "XMLHttpRequest".equalsIgnoreCase(asyncRequest)) {
            result.put("key", "权限不足");
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.setStatus(403);

        } else {
            response.setContentType("text/json;charset=utf-8");
            response.getWriter().write(objectMapper.writeValueAsString(result));
            response.setStatus(403);
        }
    }
}
