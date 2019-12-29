package com.myblog.wj.service.personalUser.impl;


import com.myblog.wj.dao.personalUser.BlogTypeDao;
import com.myblog.wj.entity.personalUser.BlogType;
import com.myblog.wj.service.personalUser.BlogTypeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BlogTypeServiceImpl implements BlogTypeService {
    @Resource
    BlogTypeDao blogTypeDao;

    @Override
    public Integer addBlogType(BlogType blogType) {
        return null;
    }

    @Override
    public Integer deleteBlogType(BlogType blogType) {
        return null;
    }

    @Override
    public List<BlogType> selectBlogType() {
        return blogTypeDao.selectList(null);
    }

    @Override
    public Integer updateBlogType(BlogType blogType) {
        return null;
    }
}
