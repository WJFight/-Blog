package com.myblog.wj.controller.psersonalUser;

import com.alibaba.druid.support.json.JSONParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myblog.wj.entity.personalUser.BlogQQUser;
import com.myblog.wj.entity.personalUser.BlogUser;
import com.myblog.wj.service.personalUser.BlogUserService;
import com.myblog.wj.util.GlobalProperties;
import com.myblog.wj.util.GlobalUtil;
import com.myblog.wj.util.ImagCode;
import com.myblog.wj.util.redisandjwt.EncryptMd5;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
public class BlogUserController {
    @Resource
    public JwtUtil jwtUtil;
    @Resource
    public BlogUserService blogUserService;
    @Resource
    BlogPostController blogPostController;
    @Resource
    RedisUtil redisUtil;

    @Resource
    ImagCode imagCode;
    private BlogUser blogUser = new BlogUser();

  //临时测试
    @GetMapping("/visit/getlock")
    @ResponseBody
    public String setlockkey(){
        redisUtil.setlockKey();
        return redisUtil.getLockKey();
    }
    @GetMapping("/visit/blogusers")
    public ModelAndView blogusers(ModelAndView modelAndView) {
        List<BlogUser> blogUsers = new ArrayList<>();
        BlogUser blogUser = new BlogUser();
        for (int i = 0; i < 20; i++) {
            blogUser.setUsername("zs" + i);
            blogUser.setEnabled(true);
            blogUser.setUser_image("图片" + i);
            blogUser.setUser_id(i);
            blogUser.setUser_realname("zs" + i);
            blogUser.setUser_phone("13568746" + i);
            blogUsers.add(blogUser);
        }

        modelAndView.addObject("code", 0);
        modelAndView.addObject("count", 20);

        return modelAndView.addObject("data", blogUser);
    }

    @GetMapping("/visit/login")
    public String login(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        int flag = 0;
        if (cookies == null) {
            flag = 0;
        } else {
            for (Cookie c : cookies) {
                if (c.getName().equals("imagecode")) {
                    flag = 1;
                }
            }
        }
        if (flag == 0) {
            Cookie cookie = new Cookie("imagecode", UUID.randomUUID().toString());
            response.addCookie(cookie);
        }
        if (jwtUtil.getTokenByHeader(request) != null) {
            return blogPostController.userindex(request, response);
        }
        return "login";
    }

    @GetMapping("/visit/qq")
    public void qq_login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "https://graph.qq.com/oauth2.0/authorize";
        String response_type = "code";
        String client_id = "101800956";
        String state = new Date().toString();
        //  request.getSession().setAttribute("state", state);
        response.addHeader("state", state);
        String redirect_uri = "http://www.yunhaiguil.wang/visit/qq/login";
        url += "?response_type=" + response_type + "&client_id=" + client_id + "&redirect_uri=" + redirect_uri + "&state=" + state;
        response.sendRedirect(url);
    }

    @GetMapping("/visit/qq/login")
    public String qq_login_callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("8888888888");
        response.setContentType("text/html; charset=utf-8");
        String authorization_code = request.getParameter("code");
        if (authorization_code != null && !authorization_code.trim().isEmpty()) {
            //client端的状态值。用于第三方应用防止CSRF攻击。
//            String state = request.getHeader("state");
//            if (!state.equals(request.getParameter("state"))) {
//                throw new RuntimeException("client端的状态值不匹配！");
//            }
            String code = authorization_code;
            String grant_type = "authorization_code";
            String client_id = "101800956";
            String client_secret = "5abccb1f8ef5bd84f9d0578969ac051a";

            String redirect_uri = "http://www.yunhaiguil.wang/visit/qq/login";
            String url = "https://graph.qq.com/oauth2.0/token";
            url += "?grant_type=" + grant_type + "&client_id=" + client_id + "&code=" + code + "&client_secret=" + client_secret + "&redirect_uri=" + redirect_uri;

            URL ul = new URL(url);
            HttpURLConnection con = null;
            con = (HttpURLConnection) ul.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setRequestMethod("GET");
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = null;
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String res = "";
            String result = null;
            int len = -1;
            while ((result = reader.readLine()) != null) {
                res += result;
            }
            System.out.println("Authority_code" + res);
            Pattern p = Pattern.compile("access_token=(\\w*)&");
            Matcher m = p.matcher(res);
            m.find();
            String access_token = m.group(1);
            if (access_token == null) {
                throw new RuntimeException("授权失败！");
            }
            //得到access_token
            System.out.println("access_token1=" + access_token);
            String url2 = "https://graph.qq.com/oauth2.0/me?access_token=" + access_token;
            URL ul1 = new URL(url2);
            System.out.println("url2=" + url2);
            con = null;
            con = (HttpURLConnection) ul1.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setRequestMethod("GET");
            inputStream = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            len = -1;
            res = "";
            result = null;
            while ((result = reader.readLine()) != null) {
                res += result;
            }
            System.out.println("获取用户id信息等待=" + res);
            p = Pattern.compile("client_id\":\"(\\w*)\",");
            m = p.matcher(res);
            m.find();
            String appid = m.group(1);
            System.out.println("client_id=" + appid);
            p = Pattern.compile("openid\":\"(\\w*)\"");
            m = p.matcher(res);
            m.find();
            //得到openid
            String openid = m.group(1);
            System.out.println("openid=" + openid);
            String ul3 = "https://graph.qq.com/user/get_user_info?" + "access_token=" + access_token + "&oauth_consumer_key=" + appid + "&openid=" + openid;
            ul1 = new URL(ul3);
            con = null;
            con = (HttpURLConnection) ul1.openConnection();
            con.setConnectTimeout(5 * 1000);
            con.setRequestMethod("GET");
            inputStream = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            len = -1;
            res = "";
            result = null;
            while ((result = reader.readLine()) != null) {
                res += result;
            }
            System.out.println("请求获取nickname=" + res);
            ObjectMapper objectMapper = new ObjectMapper();
            JSONParser jsonParser = new JSONParser(res);
            Map<String, Object> map = jsonParser.parseMap();
            //得到nickname
            String nickname = map.get("nickname").toString();
            System.out.println("nickname=" + nickname);
            //得到openid
            String gender = map.get("gender").toString();
            System.out.println("gender=" + gender);
            //得到openid
            String figureurl = map.get("figureurl_2").toString();
            if (nickname == null) {
                throw new RuntimeException("获取用户信息失败！");
            }
            System.out.println("figureurl=" + figureurl);
            request.setAttribute("user_realname", nickname);
            request.setAttribute("user_image", figureurl);
            request.setAttribute("openid", openid);
            BlogQQUser blogQQUser = new BlogQQUser(nickname, figureurl, gender, openid, null);
            BlogQQUser blogQQUser1 = blogUserService.selectBlogQqUserByOppid(openid);
            if (!(blogQQUser1 == null)) {
                if (!(blogQQUser1.getUser_id() == null)) {
                    //不是第一次登录则跳转到登录界面直接登录
                    String username = blogUserService.selectUserByUser_id(blogQQUser1.getUser_id()).getUsername();
                    Cookie cookie = GlobalUtil.getCookie("imagecode", request);
                    if (cookie != null) {
                        String imageCode = redisUtil.getKey(cookie.getValue());
                        request.setAttribute("username", username);
                        request.setAttribute("imageCode", imageCode);
                        return "qq_login";
                    }
                } else {
                    //第一次使用第三方登录跳转到绑定页面;
                    return "bing_qq_login";
                }
            } else {
                int flag = blogUserService.inserBlogQqUser(blogQQUser);
                if (flag > 0) {
                    //第一次使用第三方登录跳转到绑定页面;
                    return "bing_qq_login";
                } else {
                    //账号异常；
                    return null;
                }
            }
        }
        return null;
    }

    @GetMapping("/visit/qqbingUser")
    public String qq_bingd_user(HttpServletRequest request, @PathParam("user_realname") String user_realname, @PathParam("user_image") String user_image, @PathParam("openid") String openid) {
        request.setAttribute("user_realname", user_realname);
        request.setAttribute("user_image", user_image);
        request.setAttribute("openid", openid);
        return "bing_qq_blog_user";
    }

    @GetMapping("/visit/qq_login")
    public String qq_login(HttpServletRequest request, String username) {
        System.out.println("/visit/qq_loginusername=" + username);
        Cookie cookie = GlobalUtil.getCookie("imagecode", request);
        System.out.println("判断cookie是否是空" + cookie);
        if (cookie != null) {
            String imageCode = redisUtil.getKey(cookie.getValue());
            request.setAttribute("username", username);
            request.setAttribute("imageCode", imageCode);
            return "qq_login";
        } else {
            System.out.println("进入else 返回login");
            return "login";
        }
    }

    @ResponseBody
    @GetMapping("/visit/bingqq")
    public Map bingqq(BlogUser blogUser, HttpServletRequest request) {
        System.out.println("Username=" + blogUser.getUsername() + ", passw=" + blogUser.getPassword());
        blogUser.setVersion(blogUserService.SelectVersionByUsername(blogUser.getUsername()));
        blogUser.setPassword(EncryptMd5.encode(blogUser.getPassword()));
        System.out.println("密码加默后=" + blogUser.getPassword());
        System.out.println("传入的=" + blogUser);
        blogUser = blogUserService.login(blogUser);
        System.out.println("查数据库后的：" + blogUser);
        Map result = new HashMap();
        System.out.println("数据库密码=" + blogUser.getPassword());
        if (!(blogUser == null)) {
            result.put("code", 0);
            result.put("msg", "账号密码正确");
            return result;
        } else {
            result.put("msg", "账号密码不可用");
            result.put("code", 1);
            return result;
        }
    }

    @ResponseBody
    @PostMapping("/visit/loginBlogUser")
    public String login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("Username=" + username + ", passw=" + password);
        System.out.println("控制器里面");
        BlogUser blogUser = new BlogUser();
        blogUser.setUsername(username);
//        PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        blogUser.setVersion(blogUserService.SelectVersionByUsername(username));
        blogUser.setPassword(EncryptMd5.encode(password));
        System.out.println("传入的=" + blogUser);
        blogUser = blogUserService.login(blogUser);
        System.out.println("查数据库后的：" + blogUser);
        if (username.equals(blogUser.getUsername()) && EncryptMd5.encode(password).equals(blogUser.getPassword())) {
            String token = jwtUtil.createToken(blogUser);
            response.addHeader(GlobalProperties.JWT_TOKEN_HEADER, token);
            response.setHeader(GlobalProperties.JWT_TOKEN_HEADER, token);
            response.setHeader("status", "200");
            return "index";
        } else {
            System.out.println(blogUser);
            return "wrong";
        }

    }

    @GetMapping("/visit/toRegiste")
    public String toRegiste(HttpServletRequest request) {
        return "registe";
    }

    @ResponseBody
    @PostMapping("/visit/registe")
    public Map registe(BlogUser blogUser, HttpServletRequest request) {
        System.out.println("来到registe");
        String openid = blogUser.getOpenid();
        System.out.println("Openid=" + openid);
        if (blogUser.getUser_image() == null)
            blogUser.setUser_image("/images/h6.jpeg");
        Map result = new LinkedHashMap();
        if (blogUserService.selectBlogUserByUsername(blogUser.getUsername()) == null)
            blogUser = blogUserService.registe(blogUser,0);
        else {
            blogUser = blogUserService.updateBlogUserByUsername(blogUser, request, 1);
        }

        if (blogUser == null) {

            result.put("code", 1);
            result.put("info", "wrong");


        } else {
            if (!(blogUser == null) && !(openid == null))
                blogUserService.updateBlogQqUserByOppid(blogUser.getUser_id(), openid);

            result.put("code", 0);
            result.put("info", "注册成功！");

        }
        return result;
    }

    @GetMapping("/user/signOut")
    public String signOut(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader(jwtUtil.JWT_TOKEN_HEADER);
        blogUserService.deleRdist(jwtUtil.getUsername(token));
        jwtUtil.destroyToken(token);
        response.addHeader(jwtUtil.JWT_TOKEN_HEADER, null);
        return blogPostController.index(request, response);
    }

    @ResponseBody
    @PutMapping("/visit/updateInformByUsername")
    public Map updateInformByUsername_openid(BlogUser blogUser, HttpServletRequest request) {
        String openid = blogUser.getOpenid();
        System.out.println("/visit/updateInformByUsername----blogUser=" + blogUser);
        blogUser = blogUserService.updateBlogUserByUsername(blogUser, request, 0);
        Map map = new HashMap();
        if (blogUser == null) {
            map.put("code", "1");
            map.put("message", "失败");
            return map;
        } else {
            if (!(openid == null))
                blogUserService.updateBlogQqUserByOppid(blogUser.getUser_id(), openid);
            map.put("code", "0");
            map.put("msg", "修改成功");
            map.put("data", "666");
            map.put("blogUser", blogUser);
            request.setAttribute("blogUser", blogUser);
            return map;
        }
    }

    @ResponseBody
    @PutMapping("/user/updateInformByUsername")
    public Map updateInformByUsername(BlogUser blogUser, HttpServletRequest request) {
        String openid = blogUser.getOpenid();
        System.out.println("/user/updateInformByUsername----blogUser=" + blogUser);
        blogUser = blogUserService.updateBlogUserByUsername(blogUser, request, 0);
        Map map = new HashMap();
        if (blogUser == null) {
            map.put("status", "1");
            map.put("message", "失败");
            return map;
        } else {
            map.put("status", "0");
            map.put("msg", "修改成功");
            map.put("data", "666");
            map.put("blogUser", blogUser);
            request.setAttribute("blogUser", blogUser);
            return map;
        }

    }

    @ResponseBody
    @PutMapping("/user/updatePasswordByUsername")
    public Map updatePasswordByUsername(BlogUser blogUser, HttpServletRequest request, HttpServletResponse response) {
        String username = jwtUtil.getUsername(jwtUtil.getTokenByHeader(request));
        blogUser.setUsername(username);
        Map result = new HashMap();
        blogUser.setVersion(blogUserService.SelectVersionByUsername(username));
        if (blogUserService.updateBlogUserPasswordByUsername(blogUser, request,1)) {
            String token = request.getHeader(jwtUtil.JWT_TOKEN_HEADER);
            if (!(token == null))
                jwtUtil.destroyToken(token);
            response.addHeader(jwtUtil.JWT_TOKEN_HEADER, null);
            result.put("code", 0);
            result.put("msg", "修改成功！");
//            return "index";
        } else {
            result.put("code", 1);
            result.put("msg", "wrong");
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/visit/selectInformByUsername/{username}")
    public Map selectUserByUsername(@PathVariable("username") String username, HttpServletRequest request) {
        Map result = new HashMap();
        System.out.println("username=" + username);
        if (username != null)
//            System.out.println(blogUserService.selectBlogUserByUsername(username));
            if (blogUserService.selectBlogUserByUsername(username) == null) {
                result.put("code", 0);
                result.put("msg", "该账号可以使用！");
            } else {
                result.put("code", 1);
                result.put("msg", "该账号已经被注册！");
            }
        return result;
    }

    @ResponseBody
    @GetMapping("/user/selectInformByUsername")
    public Map seleectInformByUsername(HttpServletRequest request, HttpServletResponse response) {
        String username = jwtUtil.getUsername(jwtUtil.getTokenByHeader(request));
        System.out.println(username + "bLoguser-----" + blogUserService.selectBlogUserByUsername(username));
        request.setAttribute("blogUser", blogUserService.selectBlogUserByUsername(username));
        Map map = new HashMap();
        map.put("blogUser", blogUserService.selectBlogUserByUsername(username));
        map.put("code", 0);
        return map;
    }

    @GetMapping("/visit/imagcode")
    public void createImagCode(HttpServletResponse response, HttpServletRequest request) throws IOException {
//        redisUtil.setKey("key2", "mmp");
        BufferedImage image = imagCode.credateImage(request);
        response.setContentType("image/jpeg");
        ImageIO.write(image, "jpeg", response.getOutputStream());
    }
}
