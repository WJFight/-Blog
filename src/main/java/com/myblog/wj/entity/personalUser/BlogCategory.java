package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

public class BlogCategory implements Serializable {
    private String category_name;
    @TableId(value = "category_id", type = IdType.AUTO)
    private Integer category_id;
    @TableId(value = "user_id")
    private Integer user_id;
    private Integer category_count;

    public BlogCategory() {
    }

    public BlogCategory(String category_name, Integer category_id, Integer user_id, Integer category_count) {
        this.category_name = category_name;
        this.category_id = category_id;
        this.user_id = user_id;
        this.category_count = category_count;
    }

    public Integer getCategory_count() {
        return category_count;
    }

    public void setCategory_count(Integer category_count) {
        this.category_count = category_count;
    }

    public BlogCategory(String category_name, Integer category_id, Integer user_id) {
        this.category_name = category_name;
        this.category_id = category_id;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "BlogCategory{" +
                "category_name='" + category_name + '\'' +
                ", category_id=" + category_id +
                ", user_id=" + user_id +
                '}';
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }
}
