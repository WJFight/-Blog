package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("blog_tag_post")
public class BlogTagPost implements Serializable {
    @TableId(value = "tag_id")
    private Integer tag_id;
    @TableId(value = "post_id")
    private Integer post_id;
    private String tag_name;

    public BlogTagPost(Integer tag_id, Integer post_id, String tag_name) {
        this.tag_id = tag_id;
        this.post_id = post_id;
        this.tag_name = tag_name;
    }

    public BlogTagPost() {
    }

    @Override
    public String toString() {
        return "BlogTag_Post{" +
                "tag_id=" + tag_id +
                ", post_id=" + post_id +
                ", tag_name='" + tag_name + '\'' +
                '}';
    }

    public Integer getTag_id() {
        return tag_id;
    }

    public void setTag_id(Integer tag_id) {
        this.tag_id = tag_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }
}
