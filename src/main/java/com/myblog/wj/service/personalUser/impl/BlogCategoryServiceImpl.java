package com.myblog.wj.service.personalUser.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myblog.wj.dao.personalUser.BlogCategoryDao;
import com.myblog.wj.dao.personalUser.BlogPost_CategoryDao;
import com.myblog.wj.entity.personalUser.BlogCategory;
import com.myblog.wj.service.personalUser.BlogCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BlogCategoryServiceImpl implements BlogCategoryService {
    @Resource
    BlogCategoryDao blogCategoryDao;
    @Resource
    BlogPost_CategoryDao blogPost_categoryDao;

    @Override
    public BlogCategory addPersonalBlogCategory(BlogCategory blogCategory) {
        if (!(blogCategory == null)) {
            if (blogCategoryDao.insert(blogCategory) > 0) {
                QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("category_id", blogCategory.getCategory_id());
                queryWrapper.eq("user_id", blogCategory.getUser_id());
                return blogCategoryDao.selectOne(queryWrapper);
            }
        }
        return null;
    }

    @Override
    public List<BlogCategory> selectBlogCategoryByuser_id(Integer user_id) {
        if (!(user_id == null)) {
            QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user_id);
            return blogCategoryDao.selectList(queryWrapper);
        }
        return null;
    }

    @Override
    public BlogCategory selectBlogCategoryBycategory_nameAnduser_id(Integer user_id, String category_name) {
        if (user_id == null || category_name == null)
            return null;
        else {
            QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user_id);
            queryWrapper.eq("category_name", category_name);
            return blogCategoryDao.selectOne(queryWrapper);
        }
    }

    @Override
    public Integer addDefaultCategory(BlogCategory blogCategory) {
        if (!(blogCategory == null))
            if (blogCategoryDao.insert(blogCategory) > 0) {
                return 1;
            }
        return 0;
    }

    @Override
    public Integer deletePersonalBlogCategoryByUser_id(Integer user_id) {
        if (!(user_id == null)) {
            UpdateWrapper<BlogCategory> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("user_id", user_id);
            updateWrapper.ne("category_name", "我的文章");
            return blogCategoryDao.delete(updateWrapper);
        }
        return null;
    }

    @Override
    public Integer deletePersnoalBlogCategoryByCategory_id(Integer user_id, Integer category_id) {
        if (user_id == null || category_id == null)
            return null;
        UpdateWrapper<BlogCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", user_id);
        updateWrapper.eq("category_id", category_id);
        return blogCategoryDao.delete(updateWrapper);
    }


    @Override
    public Integer updatePersonalBlogCategoryByUser_idAndCategory_id(BlogCategory blogCategory) {
        if (blogCategory == null)
            return null;
        UpdateWrapper<BlogCategory> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_id", blogCategory.getUser_id());
        updateWrapper.eq("category_id", blogCategory.getCategory_id());
        return blogCategoryDao.update(blogCategory, updateWrapper);
    }

    @Override
    public BlogCategory selectBlogCategoryByUser_idAndCategory_id(Integer user_id, Integer category_id) {
        if (user_id == null || category_id == null)
            return null;
        else {
            QueryWrapper<BlogCategory> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_id", user_id);
            queryWrapper.eq("category_id", category_id);
            return blogCategoryDao.selectOne(queryWrapper);
        }
    }
}
