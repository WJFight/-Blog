package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogCategory;

import java.util.List;

public interface BlogCategoryService {

    /**
     * 添加个人分类
     *
     * @param blogCategory
     * @return
     */
    BlogCategory addPersonalBlogCategory(BlogCategory blogCategory);

    /**
     * 查询所有个人分类
     *
     * @param user_id
     * @return
     */
    List<BlogCategory> selectBlogCategoryByuser_id(Integer user_id);

    /**
     * 根据用户和分类名查
     *
     * @param user_id
     * @param category_name
     * @return
     */
    BlogCategory selectBlogCategoryBycategory_nameAnduser_id(Integer user_id, String category_name);

    /**
     * 创建用户时创建一个默认分类
     *
     * @param blogCategory
     * @return
     */
    Integer addDefaultCategory(BlogCategory blogCategory);


    /**
     * 删除个人所有分类
     *
     * @param blogCategory
     * @return
     */
    Integer deletePersonalBlogCategoryByUser_id(Integer user_id);

    /**
     * 根据分类id删除分类；
     *
     * @param category_id
     * @return
     */
    Integer deletePersnoalBlogCategoryByCategory_id(Integer user_id, Integer category_id);

    /**
     * 修改个人分类名
     *
     * @param blogCategory
     * @return
     */
    Integer updatePersonalBlogCategoryByUser_idAndCategory_id(BlogCategory blogCategory);

    /**
     * 根据双id查找标签；
     *
     * @param user_id
     * @param category_id
     * @return
     */
    BlogCategory selectBlogCategoryByUser_idAndCategory_id(Integer user_id, Integer category_id);

}
