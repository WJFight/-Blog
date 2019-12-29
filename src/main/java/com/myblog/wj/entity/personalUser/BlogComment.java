package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName("blog_comment")
public class BlogComment implements Serializable {
    @TableId(value = "comment_id", type = IdType.AUTO)
    private Integer comment_id;
    @TableId(value = "post_id")
    private Integer post_id;
    private String comment_content;
    private Integer user_id;
    @TableField(exist = false)
    private List<BlogReply> replyList;
    /**
     * 评论时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date comment_date;
    private Integer comment_support;
    @Version
    private Integer version;
    //    @TableField(exist = false)
    private String from_name;
    //    @TableField(exist = false)
    private String from_image;

    public BlogComment() {
    }

    @Override
    public String toString() {
        return "BlogComment{" +
                "comment_id=" + comment_id +
                ", post_id=" + post_id +
                ", comment_content='" + comment_content + '\'' +
                ", user_id=" + user_id +
                ", replyList=" + replyList +
                ", comment_date=" + comment_date +
                ", comment_support=" + comment_support +
                ", version=" + version +
                ", from_name='" + from_name + '\'' +
                ", from_image='" + from_image + '\'' +
                '}';
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getComment_content() {
        return comment_content;
    }

    public void setComment_content(String comment_content) {
        this.comment_content = comment_content;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public List<BlogReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<BlogReply> replyList) {
        this.replyList = replyList;
    }

    public Date getComment_date() {
        return comment_date;
    }

    public void setComment_date(Date comment_date) {
        this.comment_date = comment_date;
    }

    public Integer getComment_support() {
        return comment_support;
    }

    public void setComment_support(Integer comment_support) {
        this.comment_support = comment_support;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getFrom_image() {
        return from_image;
    }

    public void setFrom_image(String from_image) {
        this.from_image = from_image;
    }

    public BlogComment(Integer comment_id, Integer post_id, String comment_content, Integer user_id, List<BlogReply> replyList, Date comment_date, Integer comment_support, Integer version, String from_name, String from_image) {
        this.comment_id = comment_id;
        this.post_id = post_id;
        this.comment_content = comment_content;
        this.user_id = user_id;
        this.replyList = replyList;
        this.comment_date = comment_date;
        this.comment_support = comment_support;
        this.version = version;
        this.from_name = from_name;
        this.from_image = from_image;
    }
}
