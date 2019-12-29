package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName("blog_inform")
public class BlogInform implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String icp;
    private String music;
    private String famous;
    private Integer vistNumber;
    @TableField(exist = false)
    private Integer onlineNumber;

    @Override
    public String toString() {
        return "BlogInform{" +
                "id=" + id +
                ", icp='" + icp + '\'' +
                ", music='" + music + '\'' +
                ", famous='" + famous + '\'' +
                ", vistNumber=" + vistNumber +
                ", onlineNumber=" + onlineNumber +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVistNumber() {
        return vistNumber;
    }

    public void setVistNumber(Integer vistNumber) {
        this.vistNumber = vistNumber;
    }

    public Integer getOnlineNumber() {
        return onlineNumber;
    }

    public void setOnlineNumber(Integer onlineNumber) {
        this.onlineNumber = onlineNumber;
    }

    public String getIcp() {
        return icp;
    }

    public void setIcp(String icp) {
        this.icp = icp;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getFamous() {
        return famous;
    }

    public void setFamous(String famous) {
        this.famous = famous;
    }
}
