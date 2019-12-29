package com.myblog.wj.controller.psersonalUser;

import com.myblog.wj.service.personalUser.BlogTagService;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class BlogTagController {
    @Resource
    BlogTagService blogTagService;

    public void addTag(String tags) {
        blogTagService.addBlogTag(null);
    }

}
