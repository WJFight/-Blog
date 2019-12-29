package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageHelper;
import com.myblog.wj.dao.personalUser.BlogInformDao;
import com.myblog.wj.dao.personalUser.BlogQqUserDao;
import com.myblog.wj.dao.personalUser.BlogRoleUserDao;
import com.myblog.wj.dao.personalUser.BlogUserDao;
import com.myblog.wj.entity.personalUser.*;
import com.myblog.wj.service.personalUser.BlogCategoryService;
import com.myblog.wj.service.personalUser.BlogCommentService;
import com.myblog.wj.service.personalUser.BlogUserService;
import com.myblog.wj.util.redisandjwt.EncryptMd5;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

//import com.myblog.wj.util.redisandjwt.EncryptMd5;

//,cacheManager = "cacheManager1"
@Service
@CacheConfig(cacheNames = "blogUsers")
public class BlogUserServiceImpl implements BlogUserService {
    //    static int flag = 0;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    BlogRoleUserDao blogRoleUserDao;
    @Resource
    BlogCategoryService blogCategoryService;
    @Resource
    BlogCommentService blogCommentService;
    @Resource
    private BlogUserDao blogUserDao;
    @Resource
    private BlogQqUserDao blogQqUserDao;
    @Resource
    private BlogInformDao blogInformDao;

    @Override
    @CacheEvict(cacheNames = "blogUsers", key = "#username")
    public Integer deleteBlogUserByusername(String username) {
        if (username == null) {
            return null;
        }
        UpdateWrapper<BlogUser> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("username", username);
        return blogUserDao.delete(updateWrapper);
    }

    @Override
    public List<BlogUser> selectAllBlogUsers(Integer curr, Integer limit) {
        PageHelper.startPage(curr, limit);
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_isadmin", 0);
        return blogUserDao.selectList(queryWrapper);
    }

    @Override
    public Integer selectCount() {

        return blogUserDao.selectCount(null);
    }

    @Override
    @CachePut(key = "#blogUser.username", unless = "#result == null")
    public BlogUser login(BlogUser blogUser) {

        System.out.println("=====嗡嗡嗡嗡嗡嗡=======login=" + blogUser);
        QueryWrapper<BlogUser> wrapper = new QueryWrapper<>();
        wrapper.eq("Username", blogUser.getUsername());
        wrapper.eq("password", blogUser.getPassword());
        blogUser = blogUserDao.selectOne(wrapper);
        System.out.println("service=" + blogUser);
        return blogUser;
    }

    @Override
    @CachePut(key = "#blogUser.username", unless = "#result == null")
    public BlogUser registe(BlogUser blogUser, Integer flag) {
        blogUser.setPassword(EncryptMd5.encode(blogUser.getPassword()));
        if (flag == 1) blogUser.setUser_isadmin(1);
        if (blogUserDao.insert(blogUser) > 0) {
            blogUser = selectBlogUserByUsername(blogUser.getUsername());
            BlogRoleUser blogRoleUser;
            if (flag == 0) {
                blogRoleUser = new BlogRoleUser(blogUser.getUser_id(), 1);
            } else {
                blogRoleUser = new BlogRoleUser(blogUser.getUser_id(), 2);
            }
            System.out.println("准备调用blogRoleUser=" + blogRoleUser);
            if (blogRoleUserDao.insert(blogRoleUser) > 0) {
                BlogCategory blogCategory = new BlogCategory();
                blogCategory.setUser_id(blogUser.getUser_id());
                blogCategory.setCategory_name("我的文章");
                if (blogCategoryService.addDefaultCategory(blogCategory) > 0) {
                    System.out.println("创建默认分类成功");
                } else {
                    System.out.println("创建默认分类失败");
                }
                return blogUser;
            }
        }
        return null;
    }

    @Override
    @Cacheable(key = "#username", unless = "#result == null")
    public BlogUser selectBlogUserByUsername(String username) {
        System.out.println("来到SelectBlogUserByUsername");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("Username", username);
        BlogUser blogUser = blogUserDao.selectOne(wrapper);
        System.out.println("查询到的结果是：" + blogUser);
        if (blogUser != null)
            return blogUser;
        else return null;
    }

    @Override
    public void SignOut(String token) {
        jwtUtil.destroyToken(token);
    }

    @Override
    @CachePut(key = "#blogUser.username", unless = "#result == null")
    public BlogUser updateBlogUserByUsername(BlogUser blogUser, HttpServletRequest request, int flag) {
        String Username = "";
        if (blogUser.getUsername() == null) {
            Username = jwtUtil.getUsername(jwtUtil.getTokenByHeader(request));
            blogUser.setUsername(Username);
        }
        blogUser.setVersion(SelectVersionByUsername(blogUser.getUsername()));
        System.out.println("====" + blogUser);
        UpdateWrapper<BlogUser> wrapper = new UpdateWrapper<>();
        wrapper.eq("Username", blogUser.getUsername());
//        MultipartFile uploadFile = blogUser.getMultipartFile();
//        System.out.println("Uploadfile" + uploadFile);
        if (flag == 1) {
            Integer user_id = jwtUtil.getUserID(request);
            blogCommentService.updataCommentByfrom_id(user_id, blogUser.getUser_image());
            blogCommentService.updateReplyByfrome_id(user_id, blogUser.getUser_image());
            blogCommentService.deleReplyCommentsRdist();
            blogCommentService.deleBlogCommentsRdistbyuser_id();
            blogCommentService.deleBlogCommentsRdistbypost_id();
//            blogUser.setUser_image(image);
        }
        int bl = blogUserDao.update(blogUser, wrapper);
        if (bl == 1) {
            deleRdist(blogUser.getUsername());
            blogUser = blogUserDao.selectOne(new QueryWrapper<BlogUser>().eq("Username", blogUser.getUsername()));
//            blogUser.setPassword(null);
            return blogUser;
        }
        return null;
    }

    @Override
    public Integer SelectVersionByUsername(String username) {
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Username", username);
        return blogUserDao.selectOne(queryWrapper).getVersion();
    }

    @Override
    public boolean updateBlogUserPasswordByUsername(BlogUser blogUser, HttpServletRequest request, Integer flag) {
        deleRdist(blogUser.getUsername());
        System.out.println("加密前=" + blogUser);
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        blogUser.setPassword(EncryptMd5.encode(blogUser.getPassword()));
        System.out.println("来到这里原密码加密后updateBlogUserPasswordByUsername：" + blogUser);
        queryWrapper.eq("password", blogUser.getPassword());
        queryWrapper.eq("Username", blogUser.getUsername());
        if ((blogUserDao.selectOne(queryWrapper) != null) || flag == 0) {
            System.out.println("修改密码未加密之前：" + blogUser);
            if (flag != 0)
                blogUser.setPassword(EncryptMd5.encode(blogUser.getUser_newpassword()));
            System.out.println("修改密码加密后" + blogUser);
            UpdateWrapper<BlogUser> wrapper = new UpdateWrapper<>();
            wrapper.eq("Username", blogUser.getUsername());
            if (blogUserDao.update(blogUser, wrapper) > 0) {
                deleRdist(blogUser.getUsername());

                return true;
            }
        }
        System.out.println("查找不到修改密码的的账号密码匹配不到");
        return false;
    }

    @Override
    @Cacheable(key = "#user_id", unless = "#result == null")
    public BlogUser selectUserByUser_id(Integer user_id) {
        System.out.println("user_id=" + user_id);
        QueryWrapper<BlogUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user_id);
        return blogUserDao.selectOne(queryWrapper);
    }

    @Override
    @Cacheable(cacheNames = "BlogRole", key = "#user_id", unless = "#result == null")
    public BlogRole SelectRoleByUser_id(Integer user_id) {
        return blogUserDao.selectBlogRoleByUser_id(user_id);
    }

    @Override
    @CacheEvict(cacheNames = "BlogRole", key = "#user_id")
    public void deleteRoleByUser_id(Integer user_id) {
    }

    @Override
    public Integer updateBlogUserRole(Integer user_id, int flag) {
        if (user_id != null) {
            UpdateWrapper<BlogRoleUser> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", user_id);
            BlogRoleUser blogRoleUser = new BlogRoleUser();
            blogRoleUser.setRole_id(flag);
            blogRoleUser.setUser_id(user_id);
            return blogRoleUserDao.update(blogRoleUser, updateWrapper);
        }

        return null;
    }

    @Override
    @CacheEvict(cacheNames = "blogUsers", key = "#username")
    public boolean deleRdist(String username) {
        return true;
    }

    @Override
    public int inserBlogQqUser(BlogQQUser blogQQUser) {

        return blogQqUserDao.insert(blogQQUser);
    }

    @Override
    public BlogQQUser selectBlogQqUserByOppid(String openid) {
        QueryWrapper<BlogQQUser> qqUserQueryWrapper = new QueryWrapper<>();
        qqUserQueryWrapper.eq("openid", openid);
        return blogQqUserDao.selectOne(qqUserQueryWrapper);
    }

    @Override
    public Integer updateBlogQqUserByOppid(Integer user_id, String oppid) {
        BlogQQUser blogQQUser = new BlogQQUser();
        blogQQUser.setOpenid(oppid);
        blogQQUser.setUser_id(user_id);

        return blogQqUserDao.updateById(blogQQUser);
    }

    @Override
    @Cacheable(cacheNames = "BlogInform", key = "#id", unless = "#result == null")
    public BlogInform selectBlogInform(int id) {
        return blogInformDao.selectById(id);
    }

    @Override
    @CachePut(cacheNames = "BlogInform", key = "#blogInform.id", unless = "#result == null")
    public BlogInform updateBlogInform(BlogInform blogInform) {
        UpdateWrapper<BlogInform> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", blogInform.getId());
        if (blogInformDao.update(blogInform, updateWrapper) > 0) {
            return blogInformDao.selectById(blogInform.getId());
        }
        return null;
    }

    @Override
    @CachePut(cacheNames = "BlogInform", key = "#blogInform.id", unless = "#result == null")
    public BlogInform insertBlogInform(BlogInform blogInform) {
        if (blogInformDao.insert(blogInform) > 0) {
            return blogInformDao.selectById(blogInform.getId());
        }
        return null;
    }


}
