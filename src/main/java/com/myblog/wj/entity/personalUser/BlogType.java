package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("blog_type")
public class BlogType implements Serializable {
    @TableId("type_id")
    private Integer type_id;
    private String type_name;

    public BlogType() {
    }

    public BlogType(Integer type_id, String type_name) {
        this.type_id = type_id;
        this.type_name = type_name;

    }

    @Override
    public String toString() {
        return "BlogType{" +
                "type_id=" + type_id +
                ", type_name='" + type_name + '\'' +
                '}';
    }

    public Integer getType_id() {
        return type_id;
    }

    public void setType_id(Integer type_id) {
        this.type_id = type_id;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

}
