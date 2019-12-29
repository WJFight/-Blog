package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.myblog.wj.dao.personalUser.BlogCommentDao;
import com.myblog.wj.dao.personalUser.BlogReplyDao;
import com.myblog.wj.entity.personalUser.BlogComment;
import com.myblog.wj.entity.personalUser.BlogReply;
import com.myblog.wj.entity.personalUser.BlogUser;
import com.myblog.wj.service.personalUser.BlogCommentService;
import com.myblog.wj.service.personalUser.BlogUserService;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "blogCommentsAndReplys")
public class BlogCommentServiceImpl implements BlogCommentService {
    @Resource
    BlogCommentDao blogCommentDao;
    @Resource
    BlogReplyDao blogReplyDao;
    @Resource
    BlogUserService blogUserService;
    @Resource
    JwtUtil jwtUtil;

    @Override
    @CachePut(cacheNames = "blogComments", key = "#blogComment.comment_id", unless = "#result == null")
    public BlogComment addComment(BlogComment blogComment, HttpServletRequest request) {
        blogComment.setComment_date(new Date());
        blogComment.setUser_id(jwtUtil.getUserID(request));
        BlogUser blogUser = blogUserService.selectUserByUser_id(blogComment.getUser_id());
        blogComment.setFrom_image(blogUser.getUser_image());
        blogComment.setFrom_name(blogUser.getUser_realname());
        System.out.println("addcomment_user_id=" + blogComment);
        if (blogCommentDao.insert(blogComment) > 0) {
            deleBlogCommentsRdistbypost_id();
            deleReplyCommentsRdist();
            deleBlogCommentsRdistbyuser_id();
            return blogCommentDao.selectById(blogComment.getComment_id());
        }
        return null;
    }

    @Override
    @CachePut(cacheNames = "replyComments", key = "#blog_reply.reply_id", unless = "#result == null")
    public BlogReply addReplyComment(BlogReply blog_reply, HttpServletRequest request) {
        blog_reply.setFrom_id(jwtUtil.getUserID(request));
        blog_reply.setReply_date(new Date());
        BlogUser blogUser = new BlogUser();
        blogUser = blogUserService.selectUserByUser_id(blog_reply.getFrom_id());
        blog_reply.setFr_name(blogUser.getUser_realname());
        blog_reply.setFr_image(blogUser.getUser_image());
        blog_reply.setTo_name(blogUserService.selectUserByUser_id(blog_reply.getTo_id()).getUser_realname());
        System.out.println("addreply-blog_reply=" + blog_reply);
        if (blogReplyDao.insert(blog_reply) > 0) {
            deleReplyCommentsRdist();
            deleBlogCommentsRdistbyuser_id();
            deleBlogCommentsRdistbypost_id();
            return blogReplyDao.selectById(blog_reply.getReply_id());
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = "CommentsList", key = "#post_id", unless = "#result == null")
    public List<BlogComment> selectCommentByPost_id(Integer post_id) {
        return blogCommentDao.selectCommentByPost_id(post_id);
    }

    @Override
    public Integer updataCommentByfrom_id(Integer from_id, String image) {
        UpdateWrapper<BlogComment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", from_id);
        BlogComment blogComment = new BlogComment();
        blogComment.setFrom_image(image);
        System.out.println("更新评论头像" + blogComment);
//        Integer f =
//        if (f > 0) {
//            deleBlogCommentsRdistbyuser_id();
//            deleBlogCommentsRdistbypost_id();
//            deleReplyCommentsRdist();
//            delete(from_id, 0);
//        }
        return blogCommentDao.update(blogComment, updateWrapper);
    }

//    public void delete(Integer from_id, Integer flag) {
//        if (flag == 0) {
//            QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("user_id", from_id);
//            List<BlogComment> blogComments = blogCommentDao.selectList(queryWrapper);
//            Map<Integer, Integer> map = new LinkedHashMap();
//            if (!(blogComments == null)) {
//                for (BlogComment bc : blogComments) {
//                    if (map.get(bc.getPost_id()) == 0) {
//                        map.put(bc.getPost_id(), 1);
//                        deleBlogCommentsRdistbypost_id();
//                        deleBlogCommentsRdistbyuser_id();
//                        deleReplyCommentsRdist();
//                    }
//                }
//            }
//        } else if (flag == 1) {
//
//        }
//    }

    @Override
    public Integer updateReplyByfrome_id(Integer from_id, String image) {
        UpdateWrapper<BlogReply> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("from_id", from_id);
        BlogReply blogReply = new BlogReply();
        blogReply.setFr_image(image);
        System.out.println("更新回复评论头像" + blogReply);
        return blogReplyDao.update(blogReply, updateWrapper);
    }

    @Override
    @CacheEvict(cacheNames = "blogComments", key = "#comment_id")
    public Integer deleteCommentBycomment_id(Integer comment_id, Integer from_id, HttpServletRequest request, Integer flag) {
        Integer u_id = jwtUtil.getUserID(request);
        if ((from_id == u_id) || flag == 1) {
            return blogCommentDao.deleteById(comment_id);
        } else {
            return -1;
        }

    }

    @Override
    @CacheEvict(cacheNames = "replyComments", key = "#reply_id")
    public Integer deleteReplyByreply_idAndcomment_id(Integer comment_id, Integer from_id, Integer reply_id, HttpServletRequest request, Integer flag) {
        Integer u_id = jwtUtil.getUserID(request);
        System.out.println("u_id" + u_id + ",comment_id" + comment_id);
        if ((from_id == u_id) || flag == 1) {
            System.out.println("相等");
            UpdateWrapper<BlogReply> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("comment_id", comment_id);
            updateWrapper.eq("reply_id", reply_id);
            if (!(blogReplyDao.delete(updateWrapper) == 0)) {
                return 1;
            }
            return -1;
        } else {
            return -1;
        }
    }

    @Override
    @Cacheable(cacheNames = "blogUserComments", key = "#user_id+#curr", unless = "#result == null")
    public List<BlogComment> selectAllCommentByuser_id(Integer curr, Integer pagesize, Integer user_id, Integer flag) {
        if (user_id == null) {
            return null;
        } else {
            PageHelper.startPage(curr, pagesize);
            QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
            if (flag == 0)
                queryWrapper.eq("user_id", user_id);

            List<BlogComment> blogComments = new LinkedList<>();
            blogComments = blogCommentDao.selectList(queryWrapper);
            if (blogComments != null && blogComments.size() > 0) {
                return blogComments;
            }

        }
        return null;
    }

    @CacheEvict(cacheNames = "blogUserComments", allEntries = true)
    public Integer deleBlogCommentsRdistbyuser_id() {
        return null;
    }

    @CacheEvict(cacheNames = "CommentsList", allEntries = true)
    public Integer deleBlogCommentsRdistbypost_id() {
        return null;
    }

    @CacheEvict(cacheNames = "replylists", allEntries = true)
    public Integer deleReplyCommentsRdist() {
        return null;
    }

    @Override
    @Cacheable(cacheNames = "replylists", key = "#from_id+#curr", unless = "#result == null")
    public List<BlogReply> selectAllReplyByfrom_id(Integer curr, Integer limit, Integer from_id, Integer flag) {
        if (from_id == null) {
            return null;
        } else {
            PageHelper.startPage(curr, limit);
            QueryWrapper<BlogReply> queryWrapper = new QueryWrapper<>();
            if (flag == 0)
                queryWrapper.eq("from_id", from_id);
            List<BlogReply> blogReplies = new LinkedList<>();
            blogReplies = blogReplyDao.selectList(queryWrapper);
            if (blogReplies != null && blogReplies.size() > 0) {
                return blogReplies;
            }
        }
        return null;
    }

    public Integer selectCommentOrReplyByid(Integer id, int flag) {
        if (id == null) {
            return 0;
        } else {
            if (flag == 1) {
                QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("user_id", id);
                return blogCommentDao.selectCount(queryWrapper);
            } else if (flag == 0) {
                QueryWrapper<BlogReply> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("from_id", id);
                return blogReplyDao.selectCount(queryWrapper);
            }
        }
        return 0;
    }

    @Override
    public BlogComment selectComment_idByComment_id(Integer comment_id) {
        QueryWrapper<BlogComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("comment_id", comment_id);
        return blogCommentDao.selectOne(queryWrapper);
    }
}
