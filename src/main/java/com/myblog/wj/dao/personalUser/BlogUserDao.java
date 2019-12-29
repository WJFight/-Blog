package com.myblog.wj.dao.personalUser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.wj.entity.personalUser.BlogRole;
import com.myblog.wj.entity.personalUser.BlogRoleUser;
import com.myblog.wj.entity.personalUser.BlogUser;


public interface BlogUserDao extends BaseMapper<BlogUser> {
    /**
     * @param blogUser
     * @return 更新用户信息
     */
    Integer updateBlogUserByUsername(BlogUser blogUser);

    /**
     * @param username
     * @return 查版本
     */
    Integer selectVersionByUsername(String username);

    BlogUser selectBlogUserByUsername(String username);

    /**
     * @param user_id
     * @return user_id查用户权限
     */
    BlogRole selectBlogRoleByUser_id(Integer user_id);

    /**
     * @param blogRoleUser
     * @return 注册用户时添加用户的权限
     */
    Integer insertRoleUser(BlogRoleUser blogRoleUser);

}
