package com.myblog.wj.controller.psersonalUser;

import com.github.pagehelper.PageInfo;
import com.myblog.wj.entity.personalUser.BlogComment;
import com.myblog.wj.entity.personalUser.BlogPost;
import com.myblog.wj.entity.personalUser.BlogReply;
import com.myblog.wj.service.personalUser.BlogCommentService;
import com.myblog.wj.service.personalUser.BlogPostService;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
public class BlogCommentController {
    @Resource
    BlogCommentService blogCommentService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    BlogPostService blogPostService;

    @GetMapping("/user/selectAllReplyByfrom_id")
    public Map selectAllReplyByfrom_id(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new LinkedHashMap<>();
        Integer user_id = jwtUtil.getUserID(request);
        int flag = 1;
        if (user_id != null) {
            List<BlogReply> blogReplies = new LinkedList<>();
            blogReplies = blogCommentService.selectAllReplyByfrom_id(curr, limit, user_id, 0);
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

    @GetMapping("/user/selectAllCommentByuser_id")
    public Map selectAllCommentByuser_id(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new LinkedHashMap<>();
        Integer user_id = jwtUtil.getUserID(request);
        int flag = 1;
        if (user_id != null) {
            List<BlogComment> blogComments = new LinkedList<>();
            blogComments = blogCommentService.selectAllCommentByuser_id(curr, limit, user_id, 0);

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

    @GetMapping("/visit/selectComment/{post_id}")
    public Map selectCommentByPost_id(@PathVariable("post_id") Integer post_id, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        System.out.println("post_id" + post_id);
        List<BlogComment> blogComments = new LinkedList<>();
        blogComments = blogCommentService.selectCommentByPost_id(post_id);
        if (blogComments != null && blogComments.size() > 0) {

//            Integer comment_num=Integer.parseInt(redisUtil.getKey(post_id.toString()));
            Integer comment_num = saveAndGet(post_id, 0);
            System.out.println("comment_num=" + comment_num + "blogComments" + blogComments);
            result.put("comment_num", comment_num);
            result.put("blogComments", blogComments);
            result.put("code", 0);
        } else {
            result.put("code", 1);
            result.put("msg", "没有评论！");
        }
        return result;
    }

    @PostMapping("/user/addComment")
    public Map addComent(BlogComment blogComment, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("blogcomment=============" + blogComment);
        blogComment = blogCommentService.addComment(blogComment, request);

        Map result = new HashMap();
        if (!(blogComment == null)) {
            deleteCache();
            Integer comment_num = saveAndGet(blogComment.getPost_id(), 1);
            result.put("code", 0);
            result.put("comment_num", comment_num);
            result.put("blogComment", blogComment);
            return result;
        }
        result.put("code", 1);
        return result;
    }

    @PostMapping("/user/replyComment")
    public Map replyComment(BlogReply blog_reply, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("前端传来的:" + blog_reply);
        Map result = new HashMap();
        int post_id = blog_reply.getPost_id();
        blog_reply = blogCommentService.addReplyComment(blog_reply, request);
        System.out.println("查询前:" + blog_reply);
        if (blog_reply != null) {
            deleteCache();
            Integer comment_num = saveAndGet(post_id, 1);
            result.put("code", 0);
            result.put("comment_num", comment_num);
            result.put("blog_reply", blog_reply);
            return result;
        }
        result.put("code", 1);
        result.put("msg", "回复失败！");
        return result;
    }

    @DeleteMapping("/user/deleteComment")
    public Map deleteComment(BlogComment blogComment, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        Integer flag = blogCommentService.deleteCommentBycomment_id(blogComment.getComment_id(), blogComment.getUser_id(), request, 0);
        if (flag > 0) {
            deleteCache();
            flag = blogCommentService.selectCommentOrReplyByid(blogComment.getComment_id(), 1);
            Integer comment_num = saveAndGet(blogComment.getPost_id(), -flag);
            result.put("code", 0);
            result.put("comment_num", comment_num);
            result.put("msg", "删除成功！");
        } else {
            if (flag == -1) result.put("msg", "你权限不足！");
            else result.put("msg", "删除失败！");
            result.put("code", 1);

        }
        return result;
    }

    public void deleteCache() {
        blogCommentService.deleBlogCommentsRdistbypost_id();
        blogCommentService.deleBlogCommentsRdistbyuser_id();
        blogCommentService.deleReplyCommentsRdist();
    }

    @DeleteMapping("/user/deleteReply")
    public Map deleteReply(BlogReply blogReply, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        Integer flag = blogCommentService.deleteReplyByreply_idAndcomment_id(blogReply.getComment_id(), blogReply.getFrom_id(), blogReply.getReply_id(), request, 0);
        if (flag > 0) {
            deleteCache();
            Integer post_id = blogCommentService.selectComment_idByComment_id(blogReply.getComment_id()).getPost_id();
            Integer comment_num = saveAndGet(post_id, -flag);
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

    public Integer saveAndGet(Integer post_id, int flag) {
        Integer comment_num = Integer.parseInt(redisUtil.getKey(post_id.toString()));
        System.out.println("flag=" + flag);
        comment_num += flag;
        if (comment_num % 5 == 0) {
            BlogPost blogPost = new BlogPost();
            blogPost.setPost_id(post_id);
            blogPost.setComment_number(comment_num);
            blogPostService.updateBlogPostById(blogPost);
        }
        redisUtil.setKey(post_id.toString().toString(), comment_num);
        return comment_num;
    }
}
