package com.myblog.wj.util;


import com.myblog.wj.util.redisandjwt.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Component
public class GlobalUtil {
    @Resource
    JwtUtil jwtUtil;

    public Integer getUinqueId() {
//        return UUID.randomUUID();
        return null;
    }

    /**
     * 获取用户id;
     *
     * @param request
     * @return
     */
    public Integer getUset_id(HttpServletRequest request) {
        Integer user_id = jwtUtil.getUserID(request);
        if (user_id == null) {
            try {
                throw new ServletException("身份过期！");
            } catch (ServletException e) {
                e.printStackTrace();
            }
        }
        return user_id;
    }

    public static Cookie getCookie(String cookieName, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
//            System.out.println("cc=" + cookies);
            for (Cookie c : cookies) {
                if (c.getName().equals(cookieName)) {
                    return c;
                }
            }
        }
        return null;
    }

    public static String uploadFile(MultipartFile muploadFile, HttpServletRequest request) {
        System.out.println("进入Uploadfil--uploadFile=" + muploadFile);
//        String realPath = request.getSession().getServletContext().getRealPath("/UploadFile/HeadImages/");
//        服务器的"file:/home/Image/"
        String realPath = "/home/Image/static/HeadImages/";
//        try {
//            realPath = ResourceUtils.getURL("classpath:").getPath();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
        System.out.println("存储的短路径前件名realpath=" + realPath);
        //服务器的
//        File fold=new File(realPath);
        // 本机开发的
        File fold = new File(realPath);
        if (!fold.exists()) {
            fold = new File("");
        }
        File up = new File(fold.getAbsolutePath(), "static/HeadImages/");
        if (!up.exists()) {
            up.mkdirs();
        }

        ///////////////////
        String origName = muploadFile.getOriginalFilename();
        System.out.println("原文件名=" + origName);
        String suffer = origName.substring(origName.lastIndexOf(".", origName.length()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        StringBuffer newName = new StringBuffer();
        String newTime = sdf.format(new Date());
        newName.append(newTime).append(UUID.randomUUID().toString()).append(suffer);
        System.out.println("成功创建文件");
        try {
//            muploadFile.transferTo(new File(up.getAbsolutePath(), String.valueOf(newName)));
            //服务器
            muploadFile.transferTo(new File(fold.getAbsolutePath(), String.valueOf(newName)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uploadUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/static/HeadImages/" + newName;
        System.out.println("存储路径url=" + uploadUrl);
        return uploadUrl;
    }
//
}
