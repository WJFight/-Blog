package com.myblog.wj.entity.personalUser;

import java.io.Serializable;

public class topPost implements Serializable {
    private Integer post_id;
    private Integer value;

    public topPost() {
    }

    @Override
    public String toString() {
        return "topPost{" +
                "post_id=" + post_id +
                ", value=" + value +
                '}';
    }

    public topPost(Integer post_id, Integer value) {
        this.post_id = post_id;
        this.value = value;
    }

    public Integer getPost_id() {
        return post_id;
    }

    public void setPost_id(Integer post_id) {
        this.post_id = post_id;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
