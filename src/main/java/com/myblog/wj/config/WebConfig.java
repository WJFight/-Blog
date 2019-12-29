package com.myblog.wj.config;

import com.myblog.wj.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .excludePathPatterns("/index.html")
                .excludePathPatterns("/", "/static/**", "/ueditor/**", "/templates/**", "/static/js/**", "/css/**", "/plugins/**", "/lib/**", "/images/**", "/fonts/**", "/layui/**")
                .excludePathPatterns("/visit/**", "/static/html/**", "/editor.md-master/**")
        ;
//
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //处理静态资源
        registry.addResourceHandler("/static/HeadImages/**", "/static/HeadImages/**", "/static/js/**", "/static/**", "/editor.md-master/**", "/static/layui/**", "/images/**")
                .addResourceLocations("file:/home/Image/static/HeadImages/", "file:D:/code/Idea/myblog/target/classes/static/HeadImages/", "classpath:/static/", "classpath:/static/images/");
    }

//    @Bean
//    public OptimisticLockerInterceptor optimisticLockerInterceptor(){
//        return new OptimisticLockerInterceptor();
//    }


}
