package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogLink;

import java.util.List;

public interface BlogLinkService {
    /**
     * 添加友情链接
     *
     * @param blogLink
     * @return
     */
    BlogLink insertBlogLink(BlogLink blogLink);

    /**
     * 更新友情链接
     *
     * @param blogLink
     * @return
     */
    BlogLink updateBlogLink(BlogLink blogLink);

    /**
     * 删除友情链接
     *
     * @param id
     * @return
     */
    Integer deleteBlogLink(Integer id);

    /**
     * @param curr
     * @param limit
     * @param flag  1代表管理员  0代表成员
     * @return
     */
    List<BlogLink> selectBlogLink(Integer curr, Integer limit);

    List<BlogLink> selectBlogLinkForuser();

    /**
     * 根据id查询友情链接；
     *
     * @param id
     * @return
     */
    BlogLink selectBlogLinkByid(Integer id);

    /**
     * 获取总数
     *
     * @return
     */
    Integer selectBlogLinkCount();

    void deleteBlogLink();
}
