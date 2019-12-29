package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogPost;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BlogPostService {


    /**
     * 添加博客文章
     *
     * @param blogPost
     * @return
     */
    BlogPost addBlogPost(BlogPost blogPost);


    /**
     * 查询最新的五篇文章
     *
     * @return
     */
    List<BlogPost> selectBlogPostByTime();


    /**
     * 根据博客id查询文章详情
     *
     * @param post_id
     * @return
     */
    BlogPost selectBlogPostByPost_id(Integer post_id);

    /**
     * 从redis里面取出文章的点赞/评论数等最新数据设置在每篇文章里
     *
     * @param blogPost
     * @return
     */
    BlogPost getAndSetParise_Browsenumber_Comment_number(BlogPost blogPost);

    /**
     * 博客文章管理+加分页-查询个人或者是博客主页显示所有博客文章
     * 根据flag判断是个人还是管理员
     * flag:1 管理员    flag:0 个人
     *
     * @param curr
     * @param limit
     * @param user_id
     * @return
     */
    List<BlogPost> selectAllBlogPostByuser_id(Integer curr, Integer limit, Integer user_id, Integer flag);

    /**
     * 根据标签查询博文
     *
     * @param tag_name
     * @return
     */
    List<BlogPost> selectAllBlogPostByTag_name(String tag_name);


    /**
     * 根据博客分类查询
     *
     * @param type_id
     * @return
     */
    List<BlogPost> selectAllBlogPostByType_id(Integer type_id);

    /**
     * 分页查询以往文章
     *
     * @param curr
     * @param limit
     * @param request
     * @return
     */
    List<BlogPost> selectAllPostByfutures(Integer curr, Integer limit, HttpServletRequest request);


    /**
     * 博客文章修改
     *
     * @param blogPost
     * @return
     */
    BlogPost updateBlogPostById(BlogPost blogPost);

    /**
     * 删除文章分类时把文章分类该为默认分类；
     *
     * @param category_id
     * @param category_id2
     * @return
     */
    Integer updateBlogPost(Integer category_id, Integer category_id2, Integer user_id);

    /**
     * 博客文章删除
     *
     * @param post_id
     * @return
     */
    Integer deleteBlogPostById(Integer post_id);

    Integer selectPostCount(Integer user_id);


    /**
     * 简单做了个乐观锁用的版本号
     *
     * @param post_id
     * @return
     */
    BlogPost selectBlogPostVersionById(Integer post_id);


    /**
     * 根据文章id删除所有有关的标签
     *
     * @param post_id
     */
//    void deleteblogPostByTag_id(Integer post_id);

    /**
     * 根据个人id和个人分类id查询文章;
     *
     * @param user_id
     * @param category_id
     * @return
     */
    List<BlogPost> selectPostByUser_idAndCategory_id(Integer user_id, Integer category_id);

    /**
     * 清除redis缓存
     *
     * @param id
     */
//    public void deletblogPostRedis(Integer id);

    /**
     * 根据用户id删除用户所有文章
     *
     * @param id
     */
    public void deleteBlogPostByuser_id(Integer id);

    /**
     * 清除缓存
     *
     * @param id
     */
//    public void deleteblogPostTypeBytype_id(Integer id);

    /**
     * 清除缓存
     *
     * @param id
     */
//    public void deleteBlogPostFuturesByuser_id(Integer id);

    void deletblogPost();

}
