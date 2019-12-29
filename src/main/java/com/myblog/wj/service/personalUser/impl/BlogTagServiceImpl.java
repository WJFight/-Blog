package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myblog.wj.dao.personalUser.BlogTagDao;
import com.myblog.wj.entity.personalUser.BlogTag;
import com.myblog.wj.service.personalUser.BlogTagService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@CacheConfig(cacheNames = "blogTags")
public class BlogTagServiceImpl implements BlogTagService {
    @Resource
    BlogTagDao blogTagDao;

    @Override
    @CachePut(key = "#tag_name", unless = "#result == null")
    public BlogTag addBlogTag(String tag_name) {
        System.out.println("来到这里了");
        BlogTag bt = new BlogTag();
        bt.setTag_name(tag_name);
        bt.setTag_count(1);
        blogTagDao.insert(bt);
        deletTags();
        System.out.println("添加标签后返回的tag" + bt);
        return bt;
    }

    @Override
    @CachePut(key = "#blogTag.tag_name")
    public BlogTag updateBlogTag(BlogTag blogTag) {
        System.out.println("taga=" + blogTag);
        UpdateWrapper<BlogTag> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("tag_name", blogTag.getTag_name());
        blogTagDao.update(blogTag, updateWrapper);

        return blogTagDao.selectById(blogTag.getTag_id());
    }

    @Override
    @CacheEvict(cacheNames = "blogTags", key = "#blogTag.tag_name")
    public Integer deletBlogTag(BlogTag blogTag) {
        UpdateWrapper<BlogTag> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("tag_id", blogTag.getTag_id());
        return blogTagDao.delete(updateWrapper);
    }

    @Override
    @Cacheable(key = "#tag_name", unless = "#result == null")
    public BlogTag selectTagBytagName(String tag_name) {
        System.out.println("查询的tag=" + tag_name);
        QueryWrapper<BlogTag> queryWrapper = new QueryWrapper<BlogTag>();
        queryWrapper.eq("tag_name", tag_name);
        return blogTagDao.selectOne(queryWrapper);
    }

    @CacheEvict(cacheNames = "blogTags", key = "'tags'")
    public void deletTags() {

    }

    @Override
    @Cacheable(key = "'tags'", unless = "#result == null")
    public List<BlogTag> selectAllBlogTag() {
        System.out.println("进入标签");
        return blogTagDao.selectList(null);
    }
}
