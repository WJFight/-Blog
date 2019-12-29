package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@TableName("blog_reply")
public class BlogReply implements Serializable {
    @TableId(value = "reply_id", type = IdType.AUTO)
    private Integer reply_id;
    private Integer from_id;
    private Integer comment_id;
    private Integer to_id;
    @TableField(exist = false)
    private Integer post_id;
    private String reply_content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date reply_date;
    //    @TableField(exist = false)
    private String fr_name;
    //    @TableField(exist = false)
    private String to_name;
    //    @TableField(exist = false)
    private String fr_image;

    public Integer getReply_id() {
        return reply_id;
    }

    public BlogReply() {
    }

    @Override
    public String toString() {
        return "BlogReply{" +
                "reply_id=" + reply_id +
                ", from_id=" + from_id +
                ", comment_id=" + comment_id +
                ", to_id=" + to_id +
                ", reply_content='" + reply_content + '\'' +
                ", reply_date=" + reply_date +
                ", fr_name='" + fr_name + '\'' +
                ", to_name='" + to_name + '\'' +
                ", fr_image='" + fr_image + '\'' +
                '}';
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public void setReply_id(Integer reply_id) {
        this.reply_id = reply_id;
    }

    public Integer getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Integer from_id) {
        this.from_id = from_id;
    }

    public Integer getComment_id() {
        return comment_id;
    }

    public void setComment_id(Integer comment_id) {
        this.comment_id = comment_id;
    }

    public Integer getTo_id() {
        return to_id;
    }

    public void setTo_id(Integer to_id) {
        this.to_id = to_id;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public Date getReply_date() {
        return reply_date;
    }

    public void setReply_date(Date reply_date) {
        this.reply_date = reply_date;
    }

    public String getFr_name() {
        return fr_name;
    }

    public void setFr_name(String fr_name) {
        this.fr_name = fr_name;
    }

    public String getFr_image() {
        return fr_image;
    }

    public void setFr_image(String fr_image) {
        this.fr_image = fr_image;
    }

    public String getTo_name() {
        return to_name;
    }

    public void setTo_name(String to_name) {
        this.to_name = to_name;
    }


    public BlogReply(Integer reply_id, Integer from_id, Integer comment_id, Integer to_id, Integer post_id, String reply_content, Date reply_date, String fr_name, String to_name, String fr_image) {
        this.reply_id = reply_id;
        this.from_id = from_id;
        this.comment_id = comment_id;
        this.to_id = to_id;
        this.post_id = post_id;
        this.reply_content = reply_content;
        this.reply_date = reply_date;
        this.fr_name = fr_name;
        this.to_name = to_name;
        this.fr_image = fr_image;
    }
}
