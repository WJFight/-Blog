package com.myblog.wj.entity.personalUser;

import java.io.Serializable;

public class BlogRole implements Serializable {
    private Integer role_id;
    private String role_name;
    private String role_Desc;

    public BlogRole() {
    }

    public BlogRole(Integer role_id, String role_name, String role_Desc) {
        this.role_id = role_id;
        this.role_name = role_name;
        this.role_Desc = role_Desc;
    }

    @Override
    public String toString() {
        return "blog_role{" +
                "role_id=" + role_id +
                ", role_name='" + role_name + '\'' +
                ", role_Desc='" + role_Desc + '\'' +
                '}';
    }

    public Integer getRole_id() {
        return role_id;
    }

    public void setRole_id(Integer role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getRole_Desc() {
        return role_Desc;
    }

    public void setRole_Desc(String role_Desc) {
        this.role_Desc = role_Desc;
    }
}
