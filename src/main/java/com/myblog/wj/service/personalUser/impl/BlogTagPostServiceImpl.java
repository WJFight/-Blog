package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myblog.wj.dao.personalUser.BlogTagPostDao;
import com.myblog.wj.entity.personalUser.BlogPost;
import com.myblog.wj.entity.personalUser.BlogTagPost;
import com.myblog.wj.service.personalUser.BlogPostService;
import com.myblog.wj.service.personalUser.BlogTagPostService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = "blogTagPosts")
public class BlogTagPostServiceImpl implements BlogTagPostService {
    @Resource
    BlogTagPostDao blogTagPostDao;
    @Resource
    BlogPostService blogPostService;

    @Override
    @Cacheable(cacheNames = "blogPostByTag_id", key = "#tag_id", unless = "#result == null")
    public List<BlogPost> selectBlogPostByTag_id(Integer tag_id) {
        QueryWrapper<BlogTagPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tag_id", tag_id);
        List<BlogTagPost> blogTagPosts = blogTagPostDao.selectList(queryWrapper);
        if (!blogTagPosts.isEmpty()) {
            List<BlogPost> blogPosts = new ArrayList<>();
            for (BlogTagPost btp : blogTagPosts) {
                BlogPost blogPost = blogPostService.selectBlogPostByPost_id(btp.getPost_id());
                if (blogPost != null)
                    blogPosts.add(blogPost);
            }
            if (!blogPosts.isEmpty())
                return blogPosts;

        }
        return null;
    }

    @Override
    @CachePut(key = "#blogTagPost.post_id", unless = "#result == null")
    public BlogTagPost addBlogTagPostByPost_idAndTag_Name(BlogTagPost blogTagPost) {
        System.out.println("tag_post=" + blogTagPost);
//        blogPostService.deleteblogPostByTag_id(66);
        blogTagPostDao.insert(blogTagPost);
        QueryWrapper<BlogTagPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", blogTagPost.getPost_id());
        queryWrapper.eq("tag_id", blogTagPost.getTag_id());
        return blogTagPostDao.selectOne(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = "blogTagPosts", key = "#post_id")
    public Integer deleteBlogTagPostByPost_id(Integer post_id) {
        UpdateWrapper<BlogTagPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("post_id", post_id);
        return blogTagPostDao.delete(updateWrapper);
    }

    @Override
    @Cacheable(cacheNames = "blogPostTagByPost_id", key = "#post_id", unless = "#result == null")
    public List<BlogTagPost> selectblogPostTagByPost_id(Integer post_id) {
        System.out.println("post_id" + post_id);
        QueryWrapper<BlogTagPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", post_id);
        return blogTagPostDao.selectList(queryWrapper);
    }

    @Override
    @CacheEvict(cacheNames = "blogPostTagByPost_id", key = "#post_id")
    public void deleteblogPostTagByPost_id(Integer post_id) {
    }
}
