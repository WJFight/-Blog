package com.myblog.wj.dao.personalUser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.wj.entity.personalUser.BlogPost;

import java.util.List;

public interface BlogPostDao extends BaseMapper<BlogPost> {
    //添加博客文章
    BlogPost addBlogPost(BlogPost blogPost);

    //博客文章管理+加分页-查询个人或者是博客主页显示所有博客文章//根据浏览次数排序
    List<BlogPost> selectAllBlogPostByNoOrAll(String user_id);

    //查询前五条最新的文章
    List<BlogPost> selectBlogPostByTime();

    //根据标签查询博文
    List<BlogPost> selectAllBlogPostByTag_name(String tag_name);

    //根据博客分类查询
    List<BlogPost> selectAllBlogPostByType_id(Integer type_id);

    //博客文章修改
    BlogPost updateBlogPostById(BlogPost blogPost);

    //博客文章删除
    Integer deleteBlogPostById(BlogPost blogPost);

    //查询版本号
    Integer selectBlogPostVersionById(Integer post_id);

}
