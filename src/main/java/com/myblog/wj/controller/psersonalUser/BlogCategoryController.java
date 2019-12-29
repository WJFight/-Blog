package com.myblog.wj.controller.psersonalUser;


import com.myblog.wj.entity.personalUser.BlogCategory;
import com.myblog.wj.service.personalUser.BlogCategoryService;
import com.myblog.wj.service.personalUser.BlogPostService;
import com.myblog.wj.util.GlobalUtil;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BlogCategoryController {
    @Resource
    BlogCategoryService blogCategoryService;
    @Resource
    BlogPostService blogPostService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    JwtUtil jwtUtil;
    @Resource
    GlobalUtil globalUtil;

    @PostMapping("/user/addCategory")
    public Map addCategory(BlogCategory blogCategory, HttpServletRequest request) {
        Map result = new HashMap();
        Integer user_id = globalUtil.getUset_id(request);
        blogCategory.setUser_id(user_id);
        blogCategory = blogCategoryService.addPersonalBlogCategory(blogCategory);
        if (!(blogCategory == null)) {
            result.put("code", 0);
            result.put("msg", "成功添加！");
        } else {
            result.put("code", 1);
            result.put("msg", "添加失败！");
        }
        return null;
    }

    @GetMapping("/user/selectAllCategory")
    public Map selectCategory(HttpServletRequest request) {
        Map result = new HashMap();
        List<BlogCategory> list = new ArrayList<>();
        Integer user_id = globalUtil.getUset_id(request);
        if (!(user_id == null)) {
            list = blogCategoryService.selectBlogCategoryByuser_id(user_id);
            if (!(list == null)) {
                result.put("code", 0);
                result.put("data", list);
                result.put("msg", "成功");
            } else {
                result.put("code", 1);
                result.put("msg", "暂时还没有分类");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "暂时还没有分类");
        }
        return result;
    }

    @DeleteMapping("/user/deleteCategory/{category_id}")
    public Map deleteCategoryBycategory_id(@PathVariable("category_id") Integer category_id, HttpServletRequest request) {
        Integer user_id = globalUtil.getUset_id(request);
        Map result = new HashMap();
        if (!(user_id == null)) {
            BlogCategory blogCategory = blogCategoryService.selectBlogCategoryBycategory_nameAnduser_id(user_id, "我的文章");
            if (!(blogCategory == null)) {
                if (blogPostService.updateBlogPost(category_id, blogCategory.getCategory_id(), user_id) > 0) {
                    blogCategory.setCategory_count(blogCategory.getCategory_count() + 1);
                    blogCategoryService.updatePersonalBlogCategoryByUser_idAndCategory_id(blogCategory);
//                    blogPostService.deletblogPostRedis(0);
                    blogPostService.deletblogPost();
                    blogPostService.deleteBlogPostByuser_id(0);
//                    blogPostService.deleteblogPostTypeBytype_id(0);
//                    blogPostService.deleteBlogPostFuturesByuser_id(0);
//                    blogPostService.deleteblogPostByTag_id(0);
                }
            }
            if (blogCategoryService.deletePersnoalBlogCategoryByCategory_id(user_id, category_id) > 0) {
                result.put("code", 0);
                result.put("msg", "成功删除");
            } else {
                result.put("code", 1);
                result.put("msg", "删除失败");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "删除失败");
        }
        return result;
    }

    @PutMapping("/user/updateCategoryByCategory_id")
    public Map updateCategoryByCategory_id(BlogCategory blogCategory, HttpServletRequest request) {
        Integer user_id = globalUtil.getUset_id(request);
        Map result = new HashMap();
        if (!(user_id == null)) {
            blogCategory.setUser_id(user_id);
            if (blogCategoryService.updatePersonalBlogCategoryByUser_idAndCategory_id(blogCategory) > 0) {
                result.put("code", 0);
                result.put("msg", "修改成功");
            } else {
                result.put("code", 1);
                result.put("msg", "修改失败");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "修改失败");
        }
        return result;
    }

}
