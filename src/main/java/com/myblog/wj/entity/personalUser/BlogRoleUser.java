package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName(value = "blog_role_user")
public class BlogRoleUser implements Serializable {
    private Integer user_id;
    private Integer role_id;

    public BlogRoleUser(Integer user_id, Integer role_id) {
        this.user_id = user_id;
        this.role_id = role_id;
    }

    public BlogRoleUser() {
    }

    @Override
    public String toString() {
        return "BlogRoleUser{" +
                "user_id=" + user_id +
                ", role_id=" + role_id +
                '}';
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }
}
