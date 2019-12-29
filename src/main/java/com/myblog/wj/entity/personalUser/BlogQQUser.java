package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName(value = "blog_qquser")
public class BlogQQUser implements Serializable {
    private String nickname;
    private String figureurl;
    private String gender;
    @TableId
    private String openid;
    private Integer user_id;

    public BlogQQUser() {
    }

    public BlogQQUser(String nickname, String figureurl, String gender, String openid, Integer user_id) {
        this.nickname = nickname;
        this.figureurl = figureurl;
        this.gender = gender;
        this.openid = openid;
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "BlogQQUser{" +
                "nickname='" + nickname + '\'' +
                ", figureurl='" + figureurl + '\'' +
                ", gender='" + gender + '\'' +
                ", openid='" + openid + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
