package com.myblog.wj.controller.psersonalUser;

import com.myblog.wj.entity.personalUser.BlogPost;
import com.myblog.wj.service.personalUser.BlogTagPostService;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BlogTagPostController {
    @Resource
    RedisUtil redisUtil;
    @Resource
    BlogTagPostService blogTagPostService;

    @GetMapping("/visit/selectPostByTag_id/{tag_id}")
    public Map selectPostByTag_id(@PathVariable("tag_id") Integer tag_id, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        List<BlogPost> blogPosts = blogTagPostService.selectBlogPostByTag_id(tag_id);
        if (blogPosts != null && !blogPosts.isEmpty()) {
            result.put("code", 0);
            result.put("blogPosts", blogPosts);
            result.put("msg", "成功查询");
        } else {
            result.put("code", 1);
            result.put("msg", "该标签没有文章,请查看其他标签！");
        }
        return result;
    }


}
