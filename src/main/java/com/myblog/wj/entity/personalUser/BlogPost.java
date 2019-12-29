package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@TableName("blog_post")
public class BlogPost implements Serializable {
    @TableId(value = "post_id", type = IdType.AUTO)
    private Integer post_id;
    private String post_title;
    private String post_content;
    private Integer user_id;

    private Integer category_id;
    private Integer post_browseNumber;
    private Integer post_praise;
    private Integer type_id;
    private Integer comment_number;
    //    @TableField(exist = false)
    private Integer post_switch;

    @TableField(exist = false)
    private String blogTags;
    @TableField(exist = false)
    private String user_realname;
    //标签分类
    @TableField(exist = false)
    private List<BlogTagPost> blogTagPostList;
    //个人分类
    @TableField(exist = false)
    private List<BlogPost_Category> blogCategoryList;
    //博客分类:是否是原创还是转载 属于互联网还是医疗等等
    @TableField(exist = false)
    private BlogType blogType;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date post_createDate;
    @Version
    private Integer version;

    public BlogPost() {
    }

    @Override
    public String toString() {
        return "BlogPost{" +
                "post_id=" + post_id +
                ", post_title='" + post_title + '\'' +
                ", post_content='" + post_content + '\'' +
                ", comment_number=" + comment_number +
                ", user_id=" + user_id +
                ", category_id=" + category_id +
                ", post_browseNumber=" + post_browseNumber +
                ", post_praise=" + post_praise +
                ", type_id=" + type_id +
                ", post_switch=" + post_switch +
                ", blogTags='" + blogTags + '\'' +
                ", user_realname='" + user_realname + '\'' +
                ", blogTagPostList=" + blogTagPostList +
                ", blogCategoryList=" + blogCategoryList +
                ", blogType=" + blogType +
                ", post_createDate=" + post_createDate +
                ", version=" + version +
                '}';
    }

    public Integer getPost_praise() {
        return post_praise;
    }

    public void setPost_praise(Integer post_praise) {
        this.post_praise = post_praise;
    }

    public String getUser_realname() {
        return user_realname;
    }

    public void setUser_realname(String user_realname) {
        this.user_realname = user_realname;
    }

    public BlogPost(Integer post_id, String post_title, String post_content, Integer comment_number, Integer user_id, Integer category_id, Integer post_browseNumber, List<BlogTagPost> blogTagPostList, BlogType blogType, Date post_createDate, Integer version) {
        this.post_id = post_id;
        this.post_title = post_title;
        this.post_content = post_content;
        this.comment_number = comment_number;
        this.user_id = user_id;
        this.category_id = category_id;
        this.post_browseNumber = post_browseNumber;
        this.blogTagPostList = blogTagPostList;
        this.blogType = blogType;
        this.post_createDate = post_createDate;
        this.version = version;
    }

    public Integer getPost_switch() {
        return post_switch;
    }

    public void setPost_switch(Integer post_switch) {
        this.post_switch = post_switch;
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getBlogTags() {
        return blogTags;
    }

    public void setBlogTags(String blogTags) {
        this.blogTags = blogTags;
    }

    public List<BlogTagPost> getBlogTagPostList() {
        return blogTagPostList;
    }

    public void setBlogTagPostList(List<BlogTagPost> blogTagPostList) {
        this.blogTagPostList = blogTagPostList;
    }

    public BlogType getBlogType() {
        return blogType;
    }

    public void setBlogType(BlogType blogType) {
        this.blogType = blogType;
    }

    public Integer getPost_browseNumber() {
        return post_browseNumber;
    }

    public void setPost_browseNumber(Integer post_browseNumber) {
        this.post_browseNumber = post_browseNumber;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public Integer getComment_number() {
        return comment_number;
    }

    public void setComment_number(Integer comment_number) {
        this.comment_number = comment_number;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Date getPost_createDate() {
        return post_createDate;
    }

    public void setPost_createDate(Date post_createDate) {
        this.post_createDate = post_createDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
