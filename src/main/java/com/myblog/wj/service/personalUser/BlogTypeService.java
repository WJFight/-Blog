package com.myblog.wj.service.personalUser;


import com.myblog.wj.entity.personalUser.BlogType;

import java.util.List;

public interface BlogTypeService {
    //添加博客分类
    Integer addBlogType(BlogType blogType);

    //删除博客分类
    Integer deleteBlogType(BlogType blogType);

    //查询所有博客类型
    List<BlogType> selectBlogType();

    //更新博客分类只能跟新名字
    Integer updateBlogType(BlogType blogType);
}
