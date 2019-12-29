package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.myblog.wj.dao.personalUser.BlogLinkDao;
import com.myblog.wj.entity.personalUser.BlogLink;
import com.myblog.wj.service.personalUser.BlogLinkService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@CacheConfig(cacheNames = "blogLinks")
@Service
public class BlogLinkServiceImpl implements BlogLinkService {
    @Resource
    BlogLinkDao blogLinkDao;

    @Override
    @CachePut(key = "#blogLink.id", unless = "#result==null")
    public BlogLink insertBlogLink(BlogLink blogLink) {
        if (!(blogLink == null)) {
            if (blogLinkDao.insert(blogLink) > 0) {
                return blogLinkDao.selectById(blogLink.getId());
            }
        }
        return null;
    }

    @Override
    @CachePut(key = "#blogLink.id", unless = "#result==null")
    public BlogLink updateBlogLink(BlogLink blogLink) {
        if (!(blogLink == null)) {
            UpdateWrapper<BlogLink> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("id", blogLink.getId());
            if (blogLinkDao.update(blogLink, updateWrapper) > 0) {
                return blogLinkDao.selectById(blogLink.getId());
            }
        }
        return null;
    }

    @Override
    @CacheEvict(cacheNames = "blogLinks", allEntries = true)
    public Integer deleteBlogLink(Integer id) {
        return blogLinkDao.deleteById(id);
    }
    @CacheEvict(cacheNames = "blogLinks", allEntries = true)
    public void deleteBlogLink() {
        return ;
    }
    @Override
    @Cacheable(key = "#curr+'lists'+#limit", unless = "#result==null")
    public List<BlogLink> selectBlogLink(Integer curr, Integer limit) {
        PageHelper.startPage(curr, limit);
        return blogLinkDao.selectList(null);
    }

    @Override
    @Cacheable(key = "'lists'", unless = "#result==null")
    public List<BlogLink> selectBlogLinkForuser() {
        return blogLinkDao.selectList(null);
    }

    @Override
    @Cacheable(key = "#id", unless = "#result==null")
    public BlogLink selectBlogLinkByid(Integer id) {
        if (!(id == null)) {
            return blogLinkDao.selectById(id);
        }
        return null;
    }

    public Integer selectBlogLinkCount() {
        return blogLinkDao.selectCount(null);
    }
}
