package com.myblog.wj.util;

import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

@Component
public class ImagCode {
    @Autowired
    RedisUtil redisUtil;

    public BufferedImage credateImage(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie cookie = null;
        if (cookies.length > 0) {
            for (Cookie c : cookies) {
                if (c.getName().equals("imagecode")) {
                    cookie = c;
                }
            }
        }


        int width = 80;
        int height = 30;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();
//       // 填充指定的矩形
        graphics.setColor(new Color(0x5780A5));
        graphics.fillRect(0, 0, width - 1, height);
        //设置一个矩形边框的颜色为黑色
        graphics.setColor(Color.black);
        graphics.drawRect(0, 0, width - 1, height);
        Random random = new Random();
        String str = Integer.toHexString(random.nextInt() + 1);
        for (int i = 0; i < 50; i++) {
            graphics.drawOval(random.nextInt(width), random.nextInt(height), 0, 0);
        }
        graphics.drawLine(random.nextInt(80), random.nextInt(30), random.nextInt(80), random.nextInt(30));
        graphics.drawLine(random.nextInt(30), random.nextInt(45), random.nextInt(30), random.nextInt(24));
        graphics.drawLine(random.nextInt(50), random.nextInt(80), random.nextInt(40), random.nextInt(31));
        graphics.drawLine(random.nextInt(40), random.nextInt(50), random.nextInt(80), random.nextInt(60));

        int start = random.nextInt(3);
        String imagcode = str.substring(start, start + 4);

        if (cookie != null) {
            redisUtil.setKey(cookie.getValue(), imagcode);
        }
        graphics.setFont(new Font("China", Font.ITALIC, 24));
        graphics.drawString(imagcode, 8, 24);
        graphics.dispose();
        return image;
    }
}
