package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.myblog.wj.dao.personalUser.BlogPostDao;
import com.myblog.wj.entity.personalUser.BlogPost;
import com.myblog.wj.entity.personalUser.BlogTag;
import com.myblog.wj.entity.personalUser.BlogTagPost;
import com.myblog.wj.entity.personalUser.BlogUser;
import com.myblog.wj.service.personalUser.BlogPostService;
import com.myblog.wj.service.personalUser.BlogTagPostService;
import com.myblog.wj.service.personalUser.BlogTagService;
import com.myblog.wj.service.personalUser.BlogUserService;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@CacheConfig(cacheNames = "blogPosts")
@Service
public class BlogPostServiceImpl implements BlogPostService {
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    BlogPostDao blogPostDao;
    @Resource
    BlogTagService blogTagService;
    @Resource
    BlogTagPostService blogTagPostService;
    @Resource
    BlogUserService blogUserService;


    /**
     * @param blogPost
     * @return 0/1
     */


    @Override
    @Cacheable(key = "'time'", unless = "#result == null")
    public List<BlogPost> selectBlogPostByTime() {
        return blogPostDao.selectBlogPostByTime();
    }

    @Override
    @Cacheable(key = "#post_id", unless = "#result == null")
    public BlogPost selectBlogPostByPost_id(Integer post_id) {
        BlogPost blogPost = blogPostDao.selectById(post_id);
//        //System.out.println("来到service" + post_id);
        if (blogPost != null && blogPost.getPost_id() != null) {
            return blogPost;
        }
        return null;
    }

    @Override
    public List<BlogPost> selectAllBlogPostByTag_name(String tag_name) {
        return null;
    }

    @Override
    @Cacheable(key = "#pagesize+'user'+#user_id+'user'+#curr", unless = "#result == null")
    public List<BlogPost> selectAllBlogPostByuser_id(Integer curr, Integer pagesize, Integer user_id, Integer flag) {
        PageHelper.startPage(curr, pagesize);
        QueryWrapper<BlogPost> queryWrapper = new QueryWrapper<>();
        if (flag == 0) {
            queryWrapper.eq("user_id", user_id);
        }
        List<BlogPost> list = blogPostDao.selectList(queryWrapper);
        if (list != null && !list.isEmpty()) {
            return list;
        }
        return null;
    }

    @Override
    @Cacheable(key = "'blogPostType'+#type_id", unless = "#result == null")
    public List<BlogPost> selectAllBlogPostByType_id(Integer type_id) {
        List<BlogPost> blogPosts = blogPostDao.selectAllBlogPostByType_id(type_id);
        if (!blogPosts.isEmpty() && blogPosts != null) {
            return blogPosts;
        }
        return null;
    }

    @Override
    @Cacheable(key = "#curr+'futures'+#limit", unless = "#result == null")
    public List<BlogPost> selectAllPostByfutures(Integer curr, Integer limit, HttpServletRequest request) {
        //System.out.println("先进入方法！！！！");
        PageHelper.startPage(curr, limit);
        QueryWrapper<BlogPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("post_praise");
        List<BlogPost> blogPosts = blogPostDao.selectList(queryWrapper);
        if (blogPosts != null && !blogPosts.isEmpty()) {
            return blogPosts;
        }
        return null;
    }

    @Override
    @Cacheable(key = "#user_id+'category'+#category_id", unless = "#result == null")
    public List<BlogPost> selectPostByUser_idAndCategory_id(Integer user_id, Integer category_id) {
        QueryWrapper<BlogPost> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        queryWrapper.eq("category_id", category_id);
        return blogPostDao.selectList(queryWrapper);
    }
    @Override
    @CachePut(key = "#blogPost.post_id", unless = "#result==null")
    public BlogPost addBlogPost(BlogPost blogPost) {
        blogPost.setPost_createDate(new Date());
        if (blogPostDao.insert(blogPost) > 0) {
            if (blogPost.getBlogTags() != null && !"".equals(blogPost.getBlogTags())) {
                addblogPostTags(blogPost);
            }
            //把添加的文章的阅读数存进redis中
            redisUtil.add("top", blogPost.getPost_id(), 0);
            //点赞数
            redisUtil.setKey1(blogPost.getPost_id(), 0);
            //评论数
            redisUtil.setKey(blogPost.getPost_id().toString(), 0);
            return selectBlogPostByPost_id(blogPost.getPost_id());
        }
        return null;
    }

    @Override
    @CachePut(key = "#blogPost.post_id")
    public BlogPost updateBlogPostById(BlogPost blogPost) {
        if (blogPost.getPost_id() != null) {
            //   //System.out.println("updatera==============="+blogPost);
            UpdateWrapper<BlogPost> wrapper = new UpdateWrapper<>();
            wrapper.eq("post_id", blogPost.getPost_id());
            blogPost.setVersion(selectBlogPostVersionById(blogPost.getPost_id()).getVersion());
//            //System.out.println("version="+selectBlogPostVersionById(blogPost.getPost_id()).getVersion());
//            wrapper.eq("version", selectBlogPostVersionById(blogPost.getPost_id()).getVersion());
            if (blogPostDao.update(blogPost, wrapper) > 0) {
                if (blogPost.getBlogTags() != null && !"".equals(blogPost.getBlogTags())) {
                    updateBlogTagCountBypost_id(blogPost.getPost_id());
                    addblogPostTags(blogPost);
                }
                return blogPostDao.selectById(blogPost.getPost_id());
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public Integer updateBlogPost(Integer category_id, Integer category_id2, Integer user_id) {
        UpdateWrapper<BlogPost> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("category_id", category_id);
        updateWrapper.eq("user_id", user_id);
        BlogPost blogPost = new BlogPost();
        blogPost.setCategory_id(category_id2);

        return blogPostDao.update(blogPost, updateWrapper);
    }

    public void updateBlogTagCountBypost_id(Integer post_id) {
        List<BlogTagPost> blogTagPosts = blogTagPostService.selectblogPostTagByPost_id(post_id);
        if (!(blogTagPosts == null)) {
            BlogTag blogTag = new BlogTag();
            for (BlogTagPost btg : blogTagPosts) {
                blogTag = blogTagService.selectTagBytagName(btg.getTag_name());
                blogTag.setTag_count(blogTag.getTag_count() - 1);
                blogTagService.updateBlogTag(blogTag);
            }
            blogTagPostService.deleteBlogTagPostByPost_id(post_id);
        }
    }

    @Override
    @CacheEvict(cacheNames = {"blogPosts"}, key = "#post_id")
    public Integer deleteBlogPostById(Integer post_id) {
        //System.out.println("id=" + post_id);
        redisUtil.remove("top", post_id);
        redisUtil.remove(post_id);
        redisUtil.deleteKey(post_id.toString());
        updateBlogTagCountBypost_id(post_id);
        BlogPost blogPost = new BlogPost();
        blogPost.setPost_id(post_id);
        return blogPostDao.deleteById(post_id);
    }


    @Override
    public BlogPost selectBlogPostVersionById(Integer post_id) {
        return blogPostDao.selectById(post_id);
    }

    //删除缓存
//    @CacheEvict(value = "blogPosts", key = "'time'", allEntries = true, beforeInvocation = true)
//    public void deletblogPostRedis(Integer id) {
//        //System.out.println("deletblogPostRedis666666666666666");
//    }

    @CacheEvict(value = "blogPosts", allEntries = true, beforeInvocation = true)
    public void deletblogPost() {
        //System.out.println("deletblogPostRedis666666666666666");
    }

    @CacheEvict(value = "blogPosts", allEntries = true, beforeInvocation = true)
    public void deleteBlogPostByuser_id(Integer id) {
    }

//    @CacheEvict(value = "blogPostType", allEntries = true, beforeInvocation = true)
//    public void deleteblogPostTypeBytype_id(Integer id) {
//    }
//
//    @CacheEvict(value = "futures", allEntries = true, beforeInvocation = true)
//    public void deleteBlogPostFuturesByuser_id(Integer id) {
//    }
//
//    @CacheEvict(value = "blogPostByTag_id", allEntries = true, beforeInvocation = true)
//    public void deleteblogPostByTag_id(Integer id) {
//    }
    //为每篇文章添加redis里的点赞数，评论数，阅读数，和博主名字
    @Override
    public BlogPost getAndSetParise_Browsenumber_Comment_number(BlogPost blogPost) {
        if (blogPost == null) {
            //System.out.println("空空如也");
            return null;
        }
        //System.out.println("post_id=" + blogPost.getPost_id());
        Integer parise = (Integer) redisUtil.getKey1(blogPost.getPost_id());
        //System.out.println("点赞数：" + parise);
        if (parise != null)
            blogPost.setPost_praise(parise);
        else blogPost.setPost_praise(0);
        Integer post_browseNumber = redisUtil.getScore(blogPost.getPost_id());
        //System.out.println("浏览数:" + post_browseNumber);
        blogPost.setPost_browseNumber(post_browseNumber);
        if (!(blogPost.getPost_browseNumber() == null))
            blogPost.setComment_number(Integer.parseInt(redisUtil.getKey(blogPost.getPost_id().toString())));
        //System.out.println("评论数:" + blogPost.getComment_number());
        if (post_browseNumber != 0 && !(post_browseNumber == null) && post_browseNumber % 20 == 0) {
            blogPost = updateBlogPostById(blogPost);
            // //System.out.println("getAndSetParise_Browsenumber_Comment_number="+blogPost);
        }
        BlogUser blogUser = new BlogUser();
        if (!(blogPost == null) && !(blogPost.getUser_id() == null)) {
            blogUser = blogUserService.selectUserByUser_id(blogPost.getUser_id());
            blogPost.setUser_realname(blogUser.getUser_realname());
        }

//        //System.out.println("文章博主=" + blogUser);

        return blogPost;
    }


    @Override
    public Integer selectPostCount(Integer user_id) {
        QueryWrapper<BlogPost> queryWrapper = new QueryWrapper<>();
        if (user_id != null) {
            queryWrapper.eq("user_id", user_id);
        }
        return blogPostDao.selectCount(queryWrapper);
    }


    public void addblogPostTags(BlogPost blogPost) {

        BlogTagPost blogTagPost = new BlogTagPost();
        blogTagPost.setPost_id(blogPost.getPost_id());
        String[] tags = blogPost.getBlogTags().split("/");
//                //System.out.println("tags=" + tags.toString());
        Integer tag_id = null;
        BlogTag bt;
        //把文章的标签添加到数据库中
        for (int i = 0; i < tags.length; i++) {
            bt = new BlogTag();
            //查询该标签在标签库是否存中不存中这添加存中则统计数+1
            blogTagPost.setTag_name(tags[i]);
            //System.out.println(tags[i]);
            bt = blogTagService.selectTagBytagName(tags[i]);
            if (bt == null) {
                String tagname = tags[i];
                //System.out.println("if中的tagname=" + tagname);
                bt = blogTagService.addBlogTag(tags[i]);
            } else {
                bt.setTag_count(bt.getTag_count() + 1);
                blogTagService.updateBlogTag(bt);
            }
            blogTagPost.setTag_id(bt.getTag_id());
            blogTagPostService.addBlogTagPostByPost_idAndTag_Name(blogTagPost);
        }
    }

}
