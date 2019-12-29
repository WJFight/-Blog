package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("blog_tag")
public class BlogTag implements Serializable {
    @TableId(value = "tag_id", type = IdType.AUTO)
    private Integer tag_id;
    private String tag_name;
    private Integer tag_count;


    public BlogTag() {
    }

    public BlogTag(Integer tag_id, String tag_name, Integer tag_count) {
        this.tag_id = tag_id;
        this.tag_name = tag_name;
        this.tag_count = tag_count;
    }

    @Override
    public String toString() {
        return "BlogTag{" +
                "tag_id=" + tag_id +
                ", tag_name='" + tag_name + '\'' +
                ", tag_count=" + tag_count +
                '}';
    }

    public Integer getTag_id() {
        return tag_id;
    }

    public void setTag_id(Integer tag_id) {
        this.tag_id = tag_id;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public Integer getTag_count() {
        return tag_count;
    }

    public void setTag_count(Integer tag_count) {
        this.tag_count = tag_count;
    }
}
