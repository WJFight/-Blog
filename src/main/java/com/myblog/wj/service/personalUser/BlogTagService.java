package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogTag;

import java.util.List;

public interface BlogTagService {
    //添加标签
    BlogTag addBlogTag(String tags);

    BlogTag updateBlogTag(BlogTag blogTag);

    //删除标签
    Integer deletBlogTag(BlogTag blogTag);

    //根据标签名查询是否存中；
    BlogTag selectTagBytagName(String tag_name);

    void deletTags();

    //查询所有标签
    List<BlogTag> selectAllBlogTag();
}
