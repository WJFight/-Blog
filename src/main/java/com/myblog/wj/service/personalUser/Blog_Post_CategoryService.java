package com.myblog.wj.service.personalUser;

import com.myblog.wj.entity.personalUser.BlogPost;
import com.myblog.wj.entity.personalUser.BlogPost_Category;

public interface Blog_Post_CategoryService {
    BlogPost_Category selectBlog_Post_CategoryByPost_idAndCategory_id(Integer post_id, Integer category_id);

    BlogPost addBlog_Post_Category(BlogPost_Category blogPost_category);
//    BlogPost updateBlog_Post_Category()
}
