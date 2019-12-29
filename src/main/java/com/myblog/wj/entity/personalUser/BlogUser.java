package com.myblog.wj.entity.personalUser;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
@TableName(value = "blog_user")
public class BlogUser implements Serializable, UserDetails {

    //    @TableId(value = "user_id",type =IdType.UUID)
    private Integer user_id;
    //用户显示名
    private String user_realname;
    //用户登陆名
    private String username;
    //用户密码
    private String password;
    //用户手机号
    private String user_phone;
    //重置密码的新密码
    @TableField(exist = false)
    private String user_newpassword;
    private  Integer user_isadmin;
    //用户头像地址
    private String user_image;
    private Integer user_sex;
    private Integer isenable;
    //上传头像的属性
    @TableField(exist = false)
    private MultipartFile multipartFile;
    //版本号
    @Version
    private Integer version;
    @TableField(exist = false)
    private String openid;
    @TableField(exist = false)
    //security字段  权限
    private List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    //以下字段暂时没用；
    @TableField(exist = false)
    private boolean enabled;
    @TableField(exist = false)
    private boolean accountNonExpired;
    @TableField(exist = false)
    private boolean credentialsNonExpired;
    @TableField(exist = false)
    private boolean accountNonLocked;

    public BlogUser() {

    }

    public Integer getIsenable() {
        return isenable;
    }

    public Integer getUser_isadmin() {
        return user_isadmin;
    }

    public void setUser_isadmin(Integer user_isadmin) {
        this.user_isadmin = user_isadmin;
    }

    public void setIsenable(Integer isenable) {
        this.isenable = isenable;
    }

    @Override
    public String toString() {
        return "BlogUser{" +
                "user_id=" + user_id +
                ", user_realname='" + user_realname + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", user_phone='" + user_phone + '\'' +
                ", user_newpassword='" + user_newpassword + '\'' +
                ", user_image='" + user_image + '\'' +
                ", user_sex=" + user_sex +
                ", multipartFile=" + multipartFile +
                ", version=" + version +
                ", openid='" + openid + '\'' +
                ", authorities=" + authorities +
                ", enabled=" + enabled +
                ", accountNonExpired=" + accountNonExpired +
                ", credentialsNonExpired=" + credentialsNonExpired +
                ", accountNonLocked=" + accountNonLocked +
                '}';
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Integer getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(Integer user_sex) {
        this.user_sex = user_sex;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getUser_realname() {
        return user_realname;
    }

    public void setUser_realname(String user_realname) {
        this.user_realname = user_realname;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public String getUser_newpassword() {
        return user_newpassword;
    }

    public void setUser_newpassword(String user_newpassword) {
        this.user_newpassword = user_newpassword;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    public void setMultipartFile(MultipartFile multipartFile) {
        this.multipartFile = multipartFile;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
}
