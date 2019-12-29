package com.myblog.wj.controller.admin;

import com.github.pagehelper.PageInfo;
import com.myblog.wj.controller.psersonalUser.BlogCommentController;
import com.myblog.wj.controller.psersonalUser.BlogPostController;
import com.myblog.wj.entity.personalUser.*;
import com.myblog.wj.service.personalUser.*;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Controller
public class BlogAdminController {
    @Resource
    BlogLinkService blogLinkService;
    @Resource
    BlogPostService blogPostService;
    @Resource
    BlogCommentService blogCommentService;
    @Resource
    BlogTypeService blogTypeService;
    @Resource
    BlogUserService blogUserService;
    @Resource
    BlogPostController blogPostController;
    @Resource
    BlogTagPostService blogTagPostService;
    @Resource
    BlogCommentController blogCommentController;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    RedisUtil redisUtil;

    @ResponseBody
    @GetMapping("/admin/selectBlogInform")
    public Map selectBlogInform(HttpServletRequest request){
        Map result=new HashMap();
        BlogInform blogInform=blogUserService.selectBlogInform(1);
        if (blogInform!=null){
            result.put("code",0);
            result.put("blogInform",blogInform);
        }else{
            result.put("code",1);
            result.put("msg","暂时还没添加首页数据！");
        }
        return result;
    }
    @ResponseBody
    @PutMapping("/admin/updateBlogInform")
    public Map updateBlogInform(BlogInform blogInform, HttpServletRequest request, HttpServletResponse response){
        Map result=new HashMap();
        if (blogInform!=null){
            blogInform.setId(1);
      blogInform=blogUserService.updateBlogInform(blogInform);
      if (blogInform!=null){
          result.put("code",0);
          result.put("blogInform",blogInform);
      }else{
          result.put("code",1);
          result.put("msg","暂时还没有首页数据!");
      }
        }else{
            result.put("code",1);
            result.put("msg","失败");
        }

        return result;
    }
    @GetMapping("/visit/toAdminRegiste")
    public String toRegiste(HttpServletRequest request) {
        return "admin_registe";
    }
    @ResponseBody
    @PostMapping("/visit/adminregiste")
    public Map registe(BlogUser blogUser, HttpServletRequest request) {
        System.out.println("来到registe");
        String openid = blogUser.getOpenid();
        System.out.println("Openid=" + openid);
        if (blogUser.getUser_image() == null)
            blogUser.setUser_image("/images/h6.jpeg");
        Map result = new LinkedHashMap();
        if (blogUserService.selectBlogUserByUsername(blogUser.getUsername()) == null)
            blogUser = blogUserService.registe(blogUser,1);
        else {
            blogUser = blogUserService.updateBlogUserByUsername(blogUser, request, 0);
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

    @GetMapping("/admin/index")
    public String userindex(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = blogPostController.create(request);
        if (cookie != null)
            response.addCookie(cookie);
        request.getParameter("user_id");
        request.getAttribute("user_id");
        System.out.println("来到indexController");
        String username = jwtUtil.getUsername(jwtUtil.getTokenByHeader(request));
        System.out.println("adminName=" + username);
        request.setAttribute("blogUser", blogUserService.selectBlogUserByUsername(username));
        return "admin/admin_index";
    }

    @ResponseBody
    @GetMapping("/admin/selectAllPost")
    public Map selectAllPost(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        Integer user_id = jwtUtil.getUserID(request);
        if (!(user_id == null)) {
            List<BlogPost> blogPosts = blogPostService.selectAllBlogPostByuser_id(curr, limit, user_id, 1);
            if (!(blogPosts == null) && blogPosts.size() > 0) {
                System.out.println("数量：" + blogPosts.size());
                List<BlogPost> blogPosts1 = new ArrayList<>();
                blogPosts1 = blogPostController.getNewBlogPost(blogPosts);
                System.out.println("1数量：" + blogPosts1.size());
                PageInfo<BlogPost> pageInfo = new PageInfo<>(blogPosts1);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("count", blogPostService.selectPostCount(jwtUtil.getUserID(request)));
                result.put("data", pageInfo.getList());
            } else {
                result.put("code", 1);
                result.put("msg", "服务器繁忙！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "服务器繁忙！");
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/admin/delete/{post_id}")
    public Map deletePost(@PathVariable("post_id") Integer post_id, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("id=" + post_id);
        Map map = new HashMap();
        if (!(blogPostService.selectBlogPostByPost_id(post_id) == null)) {
            if (blogPostService.deleteBlogPostById(post_id) > 0) {
                blogTagPostService.deleteblogPostTagByPost_id(post_id);
                blogTagPostService.deleteBlogTagPostByPost_id(post_id);
//                blogPostService.deleteblogPostTypeBytype_id(6666);
//                blogPostService.deleteblogPostByTag_id(555);
//                blogPostService.deletblogPostRedis(666);
//                blogPostService.deleteBlogPostFuturesByuser_id(666);
                blogPostService.deleteBlogPostByuser_id(666);
                map.put("code", 0);
                map.put("msg", "成功");
                return map;
            }
            map.put("code", 1);
            map.put("msg", "失败！");
            return map;
        } else {
            map.put("code", 1);
            map.put("msg", "文章已经被删除");
            return map;
        }
    }

    @ResponseBody
    @GetMapping("/admin/selectAllComment")
    public Map selectAllComment(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new LinkedHashMap<>();
        Integer user_id = jwtUtil.getUserID(request);
        int flag = 1;
        if (user_id != null) {
            List<BlogComment> blogComments = new LinkedList<>();
            blogComments = blogCommentService.selectAllCommentByuser_id(curr, limit, user_id, 1);
            if (blogComments != null && blogComments.size() > 0) {
                System.out.println("评论集合=" + blogComments.size());
                PageInfo<BlogComment> pageInfo = new PageInfo<>(blogComments);
//             result.put("blogComments",blogComments);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("count", blogCommentService.selectCommentOrReplyByid(user_id, 1));
                result.put("data", pageInfo.getList());
                flag = 1;
            } else {
                flag = 0;
            }
        }
        if (user_id == null || flag == 0) {
            result.put("code", 1);
            result.put("count", 0);
            result.put("data", null);
            result.put("msg", "你暂时还没有评论！");
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/admin/selectAllReply")
    public Map selectAllReplyByfrom_id(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new LinkedHashMap<>();
        Integer user_id = jwtUtil.getUserID(request);
        int flag = 1;
        if (user_id != null) {
            List<BlogReply> blogReplies = new LinkedList<>();
            blogReplies = blogCommentService.selectAllReplyByfrom_id(curr, limit, user_id, 1);
            if (blogReplies != null && blogReplies.size() > 0) {
                System.out.println("评论集合=" + blogReplies.size());
                PageInfo<BlogReply> pageInfo = new PageInfo<>(blogReplies);
//             result.put("blogComments",blogComments);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("count", blogCommentService.selectCommentOrReplyByid(user_id, 0));
                result.put("data", pageInfo.getList());
                flag = 1;
            } else {
                flag = 0;
            }
        }
        if (user_id == null || flag == 0) {
            result.put("code", 1);
            result.put("count", 0);
            result.put("data", null);
            result.put("msg", "你暂时还没有子评论！");
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/admin/deleteReply")
    public Map deleteReply(BlogReply blogReply, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        Integer flag = blogCommentService.deleteReplyByreply_idAndcomment_id(blogReply.getComment_id(), blogReply.getFrom_id(), blogReply.getReply_id(), request, 1);
        if (flag > 0) {
            blogCommentController.deleteCache();
            Integer post_id = blogCommentService.selectComment_idByComment_id(blogReply.getComment_id()).getPost_id();
            Integer comment_num = blogCommentController.saveAndGet(post_id, -flag);
            result.put("code", 0);
            result.put("comment_num", comment_num);
            result.put("msg", "删除成功");
        } else {
            if (flag == -1) result.put("msg", "你权限不足！");
            else result.put("msg", "删除失败！");
            result.put("code", 1);

        }
        return result;
    }

    @ResponseBody
    @GetMapping("/admin/selectAllLink")
    public Map selectAllLink(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        Integer user_id = jwtUtil.getUserID(request);
        if (!(user_id == null)) {
            List<BlogLink> blogLinks = blogLinkService.selectBlogLink(curr, limit);
            if (!(blogLinks == null) && blogLinks.size() > 0) {
                System.out.println("数量：" + blogLinks.size());
                PageInfo<BlogLink> pageInfo = new PageInfo<>(blogLinks);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("count", blogLinkService.selectBlogLinkCount());
                result.put("data", pageInfo.getList());
            } else {
                result.put("code", 1);
                result.put("msg", "服务器繁忙！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "服务器繁忙！");
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/admin/deleteLinkByid/{id}")
    public Map deleteLinkByid(@PathVariable("id") Integer id, HttpServletRequest request) {
        Map result = new HashMap();
        if (!(id == null)) {
            if (blogLinkService.deleteBlogLink(id) > 0) {
                result.put("code", 0);
                result.put("msg", "删除成功！");
            } else {
                result.put("code", 1);
                result.put("msg", "删除失败！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "删除失败！");
        }

        return result;
    }

    @ResponseBody
    @PostMapping("/admin/addBlogLink")
    public Map addBlogLink(BlogLink blogLink, HttpServletRequest request) {
        Map result = new HashMap();
        if (!(blogLink == null)) {
            if (!(blogLinkService.insertBlogLink(blogLink) == null)) {
                result.put("code", 0);
                result.put("msg", "添加成功！");
            } else {
                result.put("code", 1);
                result.put("msg", "添加失败！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "添加失败！");
        }
        return result;
    }

    @ResponseBody
    @PutMapping("/admin/updateBlogLinkById")
    public Map updateBlogLinkById(BlogLink blogLink, HttpServletRequest request) {
        Map result = new HashMap();
        if (!(blogLink == null)) {
            if (!(blogLinkService.updateBlogLink(blogLink) == null)) {
                blogLinkService.deleteBlogLink();
                result.put("code", 0);
                result.put("msg", "更新成功！");
            } else {
                result.put("code", 1);
                result.put("msg", "更新失败！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "更新失败！");
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/admin/selectBlogUsers")
    public Map selectBlogUsers(Integer curr, Integer limit, HttpServletRequest request) {
        Map result = new HashMap();
        List<BlogUser> blogUsers = blogUserService.selectAllBlogUsers(curr, limit);
        if (blogUsers != null) {
            PageInfo<BlogUser> pageInfo = new PageInfo<>(blogUsers);
            result.put("code", 0);
            result.put("data", pageInfo.getList());
            result.put("count", blogUserService.selectCount());
        } else {
            result.put("code", 1);
            result.put("msg", "暂时还没有用户注册");
            result.put("count", blogUserService.selectCount());
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/admin/deleteBlogUserByusername")
    public Map deleteBlogUserByusername(String username, HttpServletRequest request) throws UnsupportedEncodingException {
        Map result = new HashMap();
        if (username != null) {
            BlogUser blogUser=blogUserService.selectBlogUserByUsername(username);
            if (blogUserService.deleteBlogUserByusername(username) > 0) {
                if (blogUser!=null)
                blogUserService.deleteRoleByUser_id(blogUser.getUser_id());
                result.put("code", 0);
                result.put("msg", "成功删除");
            } else {
                result.put("code", 1);
                result.put("msg", "删除失败");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "删除失败");
        }
        return result;
    }

    @ResponseBody
    @PutMapping("/admin/updateBlogUserRole")
    public Map updateBlogUserRole(Integer user_id, String isenable, HttpServletRequest request) {
        Map result = new HashMap();
        if (user_id != null) {
            int flag;
            if (isenable.equals("end")) {
                flag = 0;
            } else {
                flag = 1;
            }
            if (blogUserService.updateBlogUserRole(user_id, flag) > 0) {
                BlogUser blogUser = blogUserService.selectUserByUser_id(user_id);
                blogUser.setIsenable(flag);
                blogUserService.updateBlogUserByUsername(blogUser, request, 0);
                result.put("code", 0);
                result.put("msg", "成功修改");
                blogUserService.deleteRoleByUser_id(user_id);
            }
        }
        return result;
    }

    @ResponseBody
    @PutMapping("/admin/updateBlogUserPassword")
    public Map updateBlogUserPassword(BlogUser blogUser, HttpServletRequest request) {
        Map result = new HashMap();
        if (blogUser != null) {
            if (blogUserService.updateBlogUserPasswordByUsername(blogUser, request, 0)) {
                result.put("code", 0);
                result.put("msg", "成功");
            } else {
                result.put("code", 1);
                result.put("msg", "失败");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "失败");
        }
        return result;
    }

}
