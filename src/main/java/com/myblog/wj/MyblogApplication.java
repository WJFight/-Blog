package com.myblog.wj;

import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import javax.annotation.Resource;

//开启缓存
@EnableCaching
@SpringBootApplication
@MapperScan("com.myblog.wj.dao")
public class MyblogApplication {
    @Resource
    RedisUtil redisUtil;
    public static void main(String[] args) {
        SpringApplication.run(MyblogApplication.class, args);
    }




}
