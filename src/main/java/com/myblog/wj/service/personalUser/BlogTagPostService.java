package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogPost;
import com.myblog.wj.entity.personalUser.BlogTagPost;

import java.util.List;

public interface BlogTagPostService {


    /**
     * 根据标签查询有关标签的博文
     *
     * @param tag_id
     * @return
     */
    List<BlogPost> selectBlogPostByTag_id(Integer tag_id);

    /**
     * 更新博文的标签
     *
     * @param blogTagPost
     * @return
     */
    BlogTagPost addBlogTagPostByPost_idAndTag_Name(BlogTagPost blogTagPost);


    /**
     * 根据博文id删除相关的标签
     *
     * @param post_id
     * @return
     */
    Integer deleteBlogTagPostByPost_id(Integer post_id);


    /**
     * 根据文章idcch查询所有标签
     *
     * @param post_id
     * @return
     */
    List<BlogTagPost> selectblogPostTagByPost_id(Integer post_id);

    /**
     * 删除博文的标签根据文章id
     *
     * @param post_id
     */
    void deleteblogPostTagByPost_id(Integer post_id);
}
