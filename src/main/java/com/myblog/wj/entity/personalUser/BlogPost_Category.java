package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("blog_category")
public class BlogPost_Category implements Serializable {
    @TableId(value = "category_id")
    private Integer category_id;
    @TableId(value = "post_id")
    private Integer post_id;
    private String category_name;

    public BlogPost_Category() {
    }

    public BlogPost_Category(Integer category_id, Integer post_id, String category_name) {
        this.category_id = category_id;
        this.post_id = post_id;
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "BlogCategoryDao{" +
                "category_id=" + category_id +
                ", post_id=" + post_id +
                ", category_name='" + category_name + '\'' +
                '}';
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }
}
