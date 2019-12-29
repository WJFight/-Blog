package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("blog_link")
public class BlogLink implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String url;
    private String linkName;

    @Override
    public String toString() {
        return "BlogLink{" +
                "url='" + url + '\'' +
                ", linkName='" + linkName + '\'' +
                '}';
    }

    public BlogLink() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLinkName() {
        return linkName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }
}
