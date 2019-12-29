package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogComment;
import com.myblog.wj.entity.personalUser.BlogReply;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BlogCommentService {

    public BlogComment addComment(BlogComment blogComment, HttpServletRequest request);

    public BlogReply addReplyComment(BlogReply blog_reply, HttpServletRequest request);

    public List<BlogComment> selectCommentByPost_id(Integer post_id);

    public Integer updataCommentByfrom_id(Integer frome_id, String image);

    public Integer updateReplyByfrome_id(Integer from_id, String image);

    /**
     * 根据用户id删除根评论||管理员删除根评论
     *
     * @param comment_id
     * @param from_id
     * @param request
     * @param flag       1:管理员   0代表成员
     * @return
     */
    public Integer deleteCommentBycomment_id(Integer comment_id, Integer from_id, HttpServletRequest request, Integer flag);

    /**
     * 根据用户id删除回复||管理员删除回复
     *
     * @param comment_id
     * @param from_id
     * @param reply_id
     * @param request
     * @param flag       1:管理员   0代表成员
     * @return
     */
    public Integer deleteReplyByreply_idAndcomment_id(Integer comment_id, Integer from_id, Integer reply_id, HttpServletRequest request, Integer flag);

    /**
     * 根据 用户id查询发表过根评论||管理员查询所有根评论
     *
     * @param curr
     * @param pagesize
     * @param user_id
     * @param flag     1代表管理员，0代表是成员
     * @return
     */
    public List<BlogComment> selectAllCommentByuser_id(Integer curr, Integer pagesize, Integer user_id, Integer flag);

    /**
     * 根据 用户id查询发表过的回复||管理员查询所有回复
     *
     * @param curr
     * @param limit
     * @param from_id
     * @param flag    1代表管理员，0代表是成员
     * @return
     */
    public List<BlogReply> selectAllReplyByfrom_id(Integer curr, Integer limit, Integer from_id, Integer flag);

    public Integer selectCommentOrReplyByid(Integer id, int flag);

    BlogComment selectComment_idByComment_id(Integer comment_id);

    public Integer deleBlogCommentsRdistbyuser_id();


    public Integer deleBlogCommentsRdistbypost_id();


    public Integer deleReplyCommentsRdist();

}
