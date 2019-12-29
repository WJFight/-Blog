package com.myblog.wj.config.security;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.myblog.wj.util.GlobalProperties;
import com.myblog.wj.util.security.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsServce()).passwordEncoder(myPasswordEncoder());
    }

    @Autowired
    private DataSource dataSource;

//    /**
//     * 记住我功能
//     *
//     * @return
//     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        //自动创建数据库表，使用一次后注释掉，不然会报错
        //  jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/images/**", "/plugins/**", "/templates/**", "/static/js/**", "/config/**", "/visit/**", "/editor.md-master/**", "/static/**").permitAll()
                .antMatchers("/admin/**")
//                .permitAll()

                .hasRole(GlobalProperties.ROLE_ADMIN)
                .antMatchers("/user/**")
//                .permitAll()
                .hasAnyRole(GlobalProperties.ROLE_USER, GlobalProperties.ROLE_ADMIN)
                .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())//设置操作表的Repository
                .tokenValiditySeconds(60 * 60 * 24 * 7)//设置记住我的时间为7天
                .userDetailsService(myUserDetailsServce())//设置userDetailsService
                //设置表单登陆并设置登陆成功处理器，和失败处理器
                .and()
                .formLogin()
                .loginPage("/visit/login").loginProcessingUrl("/visit/loginBlogUser").successHandler(myAuthenticationSuccessHandler()).failureHandler(myAuthenticationFailureHandler()).permitAll()
                //设置会话
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // 拒绝访问处理
                .and()
                .exceptionHandling().accessDeniedHandler(myAccessDeniedHandler()).accessDeniedPage("/visit/login")
                //退出成功处理器
                .and()//.logoutSuccessHandler(myLogoutSuccessHandler())
                .logout().permitAll().logoutUrl("/user/signOut").logoutSuccessUrl("/").invalidateHttpSession(true).permitAll().deleteCookies("JSESSIONID", "Authorization")

                //取出csrf
                .and()
                .csrf().disable()

                //在UsernamepasswordPasswordAuthenticaionFilter前添加jwt过滤器
                .addFilterBefore(myOnceFilter(), UsernamePasswordAuthenticationFilter.class);
        http.headers().frameOptions().sameOrigin();
    }


    @Bean
    public MyPasswordEncoder myPasswordEncoder() {
        return new MyPasswordEncoder();
    }

    @Bean
    public MyLogoutSuccessHandler myLogoutSuccessHandler() {
        return new MyLogoutSuccessHandler();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MyOnceFilter myOnceFilter() {
        return new MyOnceFilter();
    }

    @Bean
    public MyUserDetailsServce myUserDetailsServce() {
        return new MyUserDetailsServce();
    }

    @Bean
    public MyAuthenticationSuccessHandler myAuthenticationSuccessHandler() {
        return new MyAuthenticationSuccessHandler();
    }

    @Bean
    public MyAuthenticationFailureHandler myAuthenticationFailureHandler() {
        return new MyAuthenticationFailureHandler();
    }

    @Bean
    public MyAccessDeniedHandler myAccessDeniedHandler() {
        return new MyAccessDeniedHandler();
    }

    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }

}
