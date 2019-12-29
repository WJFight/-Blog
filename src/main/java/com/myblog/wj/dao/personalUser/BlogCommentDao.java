package com.myblog.wj.dao.personalUser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.wj.entity.personalUser.BlogComment;
import com.myblog.wj.entity.personalUser.BlogReply;

import java.util.List;

public interface BlogCommentDao extends BaseMapper<BlogComment> {
    public Integer addComment(BlogComment blogComment);

    public Integer addReplyComment(BlogReply reply);

    public List<BlogComment> selectCommentByPost_id(Integer post_id);

    public Integer deleteCommentBycomment_id(Integer comment_id);

    public Integer deleteReplyByreply_idAndcomment_id(Integer comment_id, Integer reply_id);

}
