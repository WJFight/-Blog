package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogInform;
import com.myblog.wj.entity.personalUser.BlogQQUser;
import com.myblog.wj.entity.personalUser.BlogRole;
import com.myblog.wj.entity.personalUser.BlogUser;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface BlogUserService {
    Integer deleteBlogUserByusername(String username);

    /**
     * 查询所有用户
     * @return
     */
    List<BlogUser>selectAllBlogUsers(Integer curr,Integer limit);
    Integer selectCount();
    /**
     * @param blogUser
     * @return 登陆用户或管理员
     */
    BlogUser login(BlogUser blogUser);

    /**
     * @param blogUser
     * @return 注册用户信息
     */
    BlogUser registe(BlogUser blogUser,Integer flag);

    /**
     * @param username
     * @return 根据username查询用户信息
     */
    BlogUser selectBlogUserByUsername(String username);

    /**
     * @param 退出登陆
     */
    void SignOut(String token);

    /**
     * @param blogUser
     * @param request
     * @return 根据username修改个人信息
     */
    BlogUser updateBlogUserByUsername(BlogUser blogUser, HttpServletRequest request, int flag);

    /**
     * @param username
     * @return 根据username查版本号
     */
    Integer SelectVersionByUsername(String username);

    /**
     * @param blogUser
     * @param request
     * @return 根据username修改密码
     */
    boolean updateBlogUserPasswordByUsername(BlogUser blogUser, HttpServletRequest request,Integer flag);

    BlogUser selectUserByUser_id(Integer user_id);

    /**
     * @param username
     * @return 查询用户的权限
     */
    BlogRole SelectRoleByUser_id(Integer user_id);
    void deleteRoleByUser_id(Integer user_id);
    /**
     * 通过id禁止用户
     * @param user_id
     * @return
     */
    Integer updateBlogUserRole(Integer user_id,int flag);
    /**
     * 去缓存
     */
    boolean deleRdist(String username);

    /**
     * 添加第三方用户信息
     *
     * @param blogQQUser
     * @return
     */
    int inserBlogQqUser(BlogQQUser blogQQUser);

    /**
     * 根据Oppid判断第三方用户存不存在；
     *
     * @param Oppid
     * @return
     */
    BlogQQUser selectBlogQqUserByOppid(String oppid);

    Integer updateBlogQqUserByOppid(Integer user_id, String oppid);

    BlogInform selectBlogInform(int Id);

    /**
     * 修改博客首页信息
     * @param blogInform
     * @return
     */
    BlogInform updateBlogInform(BlogInform blogInform);

    /**
     * 添加博客首页信息
     * @param blogInform
     * @return
     */
    BlogInform insertBlogInform(BlogInform blogInform);


}
