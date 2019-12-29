package com.myblog.wj.util.security;

import com.myblog.wj.entity.personalUser.BlogUser;
import com.myblog.wj.service.personalUser.BlogUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义身份认证接口
 */
public class MyUserDetailsServce implements UserDetailsService {
    @Resource
    BlogUserService blogUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("来到MyUserDetailsServce++username=" + username);

        BlogUser user = blogUserService.selectBlogUserByUsername(username);
        System.out.println("list之前的user=" + user);
        List<GrantedAuthority> list = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(blogUserService.SelectRoleByUser_id(user.getUser_id()).getRole_Desc());
        list.add(grantedAuthority);
        user.setAuthorities(list);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        user.setEnabled(true);
        System.out.println("用户：" + user);
        return user;
    }
}
