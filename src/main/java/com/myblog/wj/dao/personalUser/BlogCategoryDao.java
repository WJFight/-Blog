package com.myblog.wj.dao.personalUser;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.myblog.wj.entity.personalUser.BlogCategory;
import org.apache.ibatis.annotations.CacheNamespace;

@CacheNamespace()
public interface BlogCategoryDao extends BaseMapper<BlogCategory> {

}
