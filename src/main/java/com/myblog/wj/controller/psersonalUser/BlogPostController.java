package com.myblog.wj.controller.psersonalUser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.myblog.wj.entity.personalUser.*;
import com.myblog.wj.service.personalUser.*;
import com.myblog.wj.util.GlobalUtil;
import com.myblog.wj.util.redisandjwt.JwtUtil;
import com.myblog.wj.util.redisandjwt.RedisUtil;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.*;
import java.util.concurrent.TimeUnit;

@CrossOrigin
@Controller
public class BlogPostController {
    Map<String, BlogPost> postMap = new HashMap<>();
    @Resource
    private ObjectMapper objectMapper;
    @Resource
    private JwtUtil jwtUtil;
    @Resource
    private BlogUserService blogUserService;
    @Resource
    private BlogTagService blogTagService;
    @Resource
    BlogCommentService blogCommentService;
    @Resource
    GlobalUtil globalUtil;
    @Resource
    private BlogPostService blogPostService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    BlogLinkService blogLinkService;
    @Resource
    BlogCategoryController blogCategoryController;
    @Resource
    private BlogTypeService blogTypeService;
    @Resource
    BlogCategoryService blogCategoryService;
    private Integer id = 0;
    @Resource
    BlogTagPostService blogTagPostService;

    @ResponseBody
    @GetMapping("/visit/selectPostBytag/{tag_id}")
    public String addText(@PathParam("tag_id") Integer tag_id) {
//        System.out.println("成功！");
        return "success";
    }

    @GetMapping("/visit/fullEditor")
    public String edit() {
        return "user/editorPost";
    }

    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response) {
        if (jwtUtil.getTokenByHeader(request) != null) {
            String username = jwtUtil.getUsername(jwtUtil.getTokenByHeader(request));
            request.setAttribute("blogUser", blogUserService.selectBlogUserByUsername(username));
        }


        //   System.out.println("====================点击排行榜================");
        Set<ZSetOperations.TypedTuple<Object>> top = redisUtil.getTop("top", 0, 4);
        Iterator<ZSetOperations.TypedTuple<Object>> it = top.iterator();
        List<BlogPost> tops = new ArrayList<>();
        while (it.hasNext()) {
            // System.out.println("p6666666666666666666666666666666666666666666");
            ZSetOperations.TypedTuple<Object> a = it.next();
            double post_num = a.getScore();
            //  System.out.println("poist_num" + (int) post_num);
            int p_id = (int) a.getValue();
            //  System.out.println("p_id=" + p_id);
            BlogPost blogPost = blogPostService.selectBlogPostByPost_id(p_id);
            //  System.out.println("查询文章是否存中：" + blogPost);
            blogPost = blogPostService.getAndSetParise_Browsenumber_Comment_number(blogPost);
            System.out.println("poist_num" + (int) post_num);
            //  System.out.println("blogPost" + blogPost);
            blogPost.setPost_browseNumber((int) post_num);
            tops.add(blogPost);
//            System.out.println("文章id=" + a.getValue() + ".阅读数：" + a.getScore());
        }
        BlogInform blogInform;
        //每个访问主页给一个cookid作为后面点赞防止重复;
        Cookie cookie = create(request);

        if (cookie != null) {
            blogInform = selectBlogInform(1);
            response.addCookie(cookie);
        }else{
            blogInform = selectBlogInform(0);
        }
        //查询所以标签
        List<BlogTag> tags = blogTagService.selectAllBlogTag();
        //根据时间查询最新的5篇文章
        List<BlogPost> blogPostsTime = blogPostService.selectBlogPostByTime();
        List<BlogPost> blogPostlist = blogPostlist = new ArrayList<>();
        if (!(blogPostsTime == null)) {
            blogPostlist = getNewBlogPost(blogPostsTime);
//            }
        }
        List<BlogPost> commentList = new ArrayList<>();
        commentList.addAll(tops);
        Collections.sort(commentList, new Comparator<BlogPost>() {
            @Override
            public int compare(BlogPost o1, BlogPost o2) {
                return o1.getComment_number() - o2.getComment_number();
            }
        });
        if (commentList != null)
            request.setAttribute("commentLists", commentList);
        List<BlogLink> blogLinks = blogLinkService.selectBlogLinkForuser();
        if (!(blogLinks == null)) {
            request.setAttribute("bloglinks", blogLinks);
        }



        request.getSession().setAttribute("blogInfom", blogInform);
        request.setAttribute("blogInfom", blogInform);
        request.setAttribute("tags", tags);
        request.setAttribute("postlist", blogPostlist);
        request.setAttribute("tops", tops);
//        System.out.println("排行榜=" + request.getAttribute("tops"));
        System.out.println("热门标签=" + request.getAttribute("tags"));
//        System.out.println("最新博客=" + request.getAttribute("postlist"));

        return "index";
    }

    public BlogInform selectBlogInform(int flag) {
        BlogInform blogInform = blogUserService.selectBlogInform(1);
        if (blogInform == null) {
            blogInform.setIcp("粤ICP备19089644号-1");
            blogInform.setFamous("人生中出现的一切，都无法拥有只能经历。深知这一点的人，就会懂得：无所谓失去，而只是经过而已；亦无所谓失败，而只是经验而已。用一颗浏览的心，去看待人生，一切的得与失、隐与显，都是风景与风情");
            blogInform.setMusic("/static/HeadImages/Henry.mp3");
            blogInform.setVistNumber(10);
            blogInform.setOnlineNumber(0);
        }
        blogInform = updateNumber(blogInform, flag);
        return blogInform;
    }


    @GetMapping("/user/index")
    public String userindex(HttpServletRequest request, HttpServletResponse response) {
        Cookie cookie = create(request);

        if (cookie != null)
            response.addCookie(cookie);
        System.out.println("来到indexController");
        String username = jwtUtil.getUsername(jwtUtil.getTokenByHeader(request));
        request.setAttribute("blogUser", blogUserService.selectBlogUserByUsername(username));
        BlogCategory blogCategory = new BlogCategory();
        blogCategory.setCategory_id(0);
        blogCategory.setCategory_name("spring");
        BlogCategory blogCategory1 = new BlogCategory();
        blogCategory1.setCategory_id(1);
        blogCategory1.setCategory_name("springBoot");
        List<BlogCategory> list = new ArrayList<>();
        list.add(blogCategory);
        list.add(blogCategory1);
        request.setAttribute("categoryTypes", list);
        return "user/index";
    }

    @ResponseBody
    @PostMapping("/user/uploadFile")
    public Map<String, Object> uploadFile(@RequestParam(value = "editormd-image-file", required = true) MultipartFile uploadFile, @RequestParam(required = false) String realname, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> result = new HashMap<>();
        String url = GlobalUtil.uploadFile(uploadFile, request);
        System.out.println("realname====" + realname);
        if (url != null) {
            if (realname != null) {
                BlogUser blogUser = new BlogUser();
                blogUser.setUser_image(url);
                blogUserService.updateBlogUserByUsername(blogUser, request, 1);
//                blogCommentService.updataCommentByfrom_id(blogUser.getUser_id(), blogUser.getUser_image());
//                blogCommentService.updateReplyByfrome_id(blogUser.getUser_id(), blogUser.getUser_image());

            }

            System.out.println("url:" + url);
            result.put("url", url);
            result.put("success", 1);
            result.put("message", "上传成功");
        } else {
            result.put("code", 1);
            result.put("success", 0);
            result.put("message", "上传失败");
        }
        response.setHeader("X-Frame-Options", "SAMEORIGIN");
        return result;
    }

    @ResponseBody
    @PostMapping("/user/addPost")
    public Map<String, Object> addPost(BlogPost blogPost, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
//        System.out.println("添加文章BlogPost=" + blogPost);
        Integer id = globalUtil.getUset_id(request);
        System.out.println("用户id" + id);
        blogPost.setUser_id(id);
//        blogPost.setPost_parttext(blogPost.getPost_content().substring(200));
        //添加文章的浏览量
        Map map = new HashMap();
        if (!(blogPostService.addBlogPost(blogPost) == null)) {
            BlogCategory blogCategory = blogCategoryService.selectBlogCategoryByUser_idAndCategory_id(id, blogPost.getCategory_id());
            if (!(blogCategory == null))
                blogCategory.setCategory_count(blogCategory.getCategory_count() + 1);
            blogCategoryController.updateCategoryByCategory_id(blogCategory, request);
//            blogPostService.deleteblogPostTypeBytype_id(6666);
//            blogPostService.deleteblogPostByTag_id(555);
//            blogPostService.deletblogPostRedis(666);
//            blogPostService.deleteBlogPostFuturesByuser_id(666);
            blogPostService.deleteBlogPostByuser_id(666);
            blogTagService.deletTags();
            map.put("status", "0");
            map.put("message", "成功");
        } else {
            map.put("status", "1");
            map.put("message", "失败");
        }
        return map;
    }

    @ResponseBody
    @PostMapping("/user/updatePostByPost_id")
    public Map updatePostByPost_id(BlogPost blogPost, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println("blogPost=" + blogPost);
        Integer user_id = globalUtil.getUset_id(request);
        Map result = new LinkedHashMap();
        if (!(user_id == null)) {

            BlogPost b = blogPostService.selectBlogPostByPost_id(blogPost.getPost_id());
            if (!(b == null)) {
                if (!b.getCategory_id().equals(blogPost.getCategory_id())) {

                    BlogCategory blogCategory = blogCategoryService.selectBlogCategoryByUser_idAndCategory_id(user_id, b.getCategory_id());
                    if (!(blogCategory == null)) {
                        blogCategory.setCategory_count(blogCategory.getCategory_count() - 1);
                        blogCategoryService.updatePersonalBlogCategoryByUser_idAndCategory_id(blogCategory);
                    }
                    blogCategory = blogCategoryService.selectBlogCategoryByUser_idAndCategory_id(user_id, blogPost.getCategory_id());
                    if (!(blogCategory == null)) {
                        blogCategory.setCategory_count(blogCategory.getCategory_count() + 1);
                        blogCategoryService.updatePersonalBlogCategoryByUser_idAndCategory_id(blogCategory);
                    }
                }
            }
            blogPost = blogPostService.updateBlogPostById(blogPost);
            if (blogPost != null) {
                blogTagPostService.deleteblogPostTagByPost_id(blogPost.getPost_id());
                result.put("code", 0);
                result.put("msg", "修改成功！");
            } else {
                result.put("code", 1);
                result.put("msg", "修改失败!");
            }
        }
        return result;
    }

    /**
     * @param post_id
     * @param response
     * @param request
     * @return 根据id查询博文
     */
    @GetMapping("/visit/selectPost/{post_id}")
    public String selectPost(@PathVariable("post_id") Integer post_id, HttpServletResponse response, HttpServletRequest request) {
        System.out.println("查看博文:" + "\n");
        BlogPost blogPost = new BlogPost();

        blogPost = blogPostService.selectBlogPostByPost_id(post_id);
        blogPost = blogPostService.getAndSetParise_Browsenumber_Comment_number(blogPost);
        redisUtil.increateScore("top", post_id, 1);
        Integer user_id = jwtUtil.getUserID(request);
        request.setAttribute("blogpostUser", blogUserService.selectUserByUser_id(blogPost.getUser_id()));
        if (user_id != null) {
            request.setAttribute("bu", blogUserService.selectUserByUser_id(user_id));
        } else {
            request.setAttribute("bu.user_image", "/images/h6.jpeg");

        }
        BlogInform blogInform = selectBlogInform(0);
        request.setAttribute("blogInform", blogInform);
        if (blogPost != null) {
//            System.out.println(blogPost);
            List<BlogCategory> blogCategories = blogCategoryService.selectBlogCategoryByuser_id(blogPost.getUser_id());
            if (!(blogCategories == null)) {
                request.setAttribute("categorys", blogCategories);
            }
            request.setAttribute("blogPost", blogPost);
            return "user/postDetail";
        } else {
            return "user/error";
        }
    }

    /**
     * @return 查询所有博客文章个人id
     */
    @ResponseBody
    @GetMapping("/user/selectAllPost")
    public Map selectAllPost(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        Integer user_id = jwtUtil.getUserID(request);
        if (!(user_id == null)) {
            List<BlogPost> blogPosts = blogPostService.selectAllBlogPostByuser_id(curr, limit, user_id, 0);
            if (!(blogPosts == null) && blogPosts.size() > 0) {
                Integer comment_num = 0;
                System.out.println("数量：" + blogPosts.size());
                List<BlogPost> blogPosts1 = new ArrayList<>();
                blogPosts1 = getNewBlogPost(blogPosts);
                System.out.println("1数量：" + blogPosts1.size());
                PageInfo<BlogPost> pageInfo = new PageInfo<>(blogPosts1);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("count", blogPostService.selectPostCount(jwtUtil.getUserID(request)));
                result.put("data", pageInfo.getList());
            } else {
                result.put("code", 1);
                result.put("msg", "你还没发布过文章！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "服务器繁忙！");
        }
        return result;
    }

    @ResponseBody
    @DeleteMapping("/user/delete/{post_id}")
    public Map deletePost(@PathVariable("post_id") Integer post_id, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("id=" + post_id);
        Map map = new HashMap();
        Integer user_id = jwtUtil.getUserID(request);
        BlogPost blogPost = blogPostService.selectBlogPostByPost_id(post_id);
        if (!(blogPost == null) && user_id.equals(blogPost.getUser_id())) {
            if (blogPostService.deleteBlogPostById(post_id) > 0) {
                blogTagPostService.deleteblogPostTagByPost_id(post_id);
                blogTagPostService.deleteBlogTagPostByPost_id(post_id);
//                blogPostService.deleteblogPostTypeBytype_id(6666);
//                blogPostService.deleteblogPostByTag_id(555);
//                blogPostService.deletblogPostRedis(666);
//                blogPostService.deleteBlogPostFuturesByuser_id(666);
                blogPostService.deleteBlogPostByuser_id(666);
                map.put("code", 0);
                map.put("msg", "成功");
                return map;
            }
            map.put("code", 1);
            map.put("msg", "失败！");
            return map;
        } else {
            map.put("code", 1);
            map.put("msg", "文章已经被删除");
            return map;
        }
    }

    @ResponseBody
    @GetMapping("/user/editor/{post_id}")
    public Map editer(@PathVariable("post_id") Integer post_id, HttpServletResponse response, HttpServletRequest request) {

        BlogPost blogPost = blogPostService.selectBlogPostByPost_id(post_id);

        Map map = new HashMap();
        if (!(blogPost == null)) {
            map.put("status", 0);
            map.put("msg", "成功");
            List<BlogTagPost> blogTagPosts = blogTagPostService.selectblogPostTagByPost_id(blogPost.getPost_id());
            String tags = "";
            if (!(blogTagPosts == null)) {
                for (BlogTagPost blogTagPost : blogTagPosts) {

                    tags += blogTagPost.getTag_name() + "/";

                }
            }
            if (!(tags == "")) blogPost.setBlogTags(tags);
            map.put("blogPost", blogPost);
//            System.out.println(blogPost.getBlogTags());
            List<BlogType> blogTypes = blogTypeService.selectBlogType();
            map.put("postType", blogTypes);
            List<BlogCategory> blogCategories = blogCategoryService.selectBlogCategoryByuser_id(jwtUtil.getUserID(request));
            map.put("categoryType", blogCategories);
        } else {
            map.put("status", 1);
            map.put("msg", "文章不存在！");
        }
        return map;
    }

    /**
     * @param request
     * @param response
     * @return 查询所有博客类型
     */
    @ResponseBody
    @GetMapping("/user/postTypeAndcategoryType")
    public Map selectAllPostType(HttpServletRequest request, HttpServletResponse response) {
        Integer user_id = jwtUtil.getUserID(request);
        Map map = new HashMap();
        map.put("status", 0);
        map.put("msg", "成功");
        //博客类型
        List<BlogType> blogTypes = blogTypeService.selectBlogType();
        if (blogTypes != null && blogTypes.size() > 0)
            map.put("postType", blogTypes);
        else map.put("postType", null);
        //个人分类
        List<BlogCategory> list2 = new ArrayList<>();
        if (user_id != null)
            list2 = blogCategoryService.selectBlogCategoryByuser_id(user_id);
        map.put("categoryType", list2);
        return map;
    }

    @ResponseBody
    @GetMapping("/visit/selectPostByType_id/{type_id}")
    public Map selectPostByType_id(@PathVariable("type_id") Integer type_id, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        List<BlogPost> blogPosts = blogPostService.selectAllBlogPostByType_id(type_id);

        if (blogPosts != null && blogPosts.size() > 0) {
            List<BlogPost> blogPostList = new ArrayList<>();
            blogPostList = getNewBlogPost(blogPosts);
            if (blogPostList != null && !blogPostList.isEmpty()) {
                System.out.println("666666666");
                result.put("code", 0);
                result.put("blogPosts", blogPosts);
                result.put("msg", "查询成功");
            } else {
                result.put("code", 1);
                result.put("msg", "暂时还没有该分类的文章请查看其他分类！");
            }
//
        } else {
            result.put("code", 1);
            result.put("msg", "暂时还没有该分类的文章请查看其他分类！");
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/visit/selectPostByCategory_idAndUser_id")
    public Map selectPostByCategory_idAndUser_id(Integer user_id, Integer category_id, HttpServletRequest request, HttpServletResponse response) {
        Map result = new HashMap();
        if (user_id == null || category_id == null) {
            result.put("code", 1);
            result.put("msg", "该分类不存在!");
        } else {
            List<BlogPost> blogPosts = blogPostService.selectPostByUser_idAndCategory_id(user_id, category_id);
            if (!(blogPosts == null)) {
                blogPosts = getNewBlogPost(blogPosts);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("data", blogPosts);
            } else {
                result.put("code", 1);
                result.put("msg", "该分类暂时还没有添加文章");
            }
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/visit/selectAllPostByfutures")
    public Map selectPostByfutures(Integer curr, Integer limit, HttpServletRequest request, HttpServletResponse response) {
//        System.out.println(curr + "," + limit);
        Map result = new HashMap();
        List<BlogPost> list = blogPostService.selectAllPostByfutures(curr, limit, request);
        if (list != null && list.size() > 0) {
            List<BlogPost> blogPostList = new ArrayList<>();
            blogPostList = getNewBlogPost(list);
            if (!blogPostList.isEmpty()) {
                PageInfo<BlogPost> pageInfo = new PageInfo<>(list);
                result.put("code", 0);
                result.put("msg", "成功");
                result.put("count", blogPostService.selectPostCount(null));
                result.put("blogPosts", pageInfo.getList());
            } else {
                result.put("code", 1);
                result.put("msg", "服务器繁忙！");
            }

//            System.out.println("大小：" + pageInfo.getList().size());
        } else {
            result.put("code", 1);
            result.put("msg", "服务器繁忙！");
        }
        return result;
    }

    @ResponseBody
    @GetMapping("/visit/addPraise/{post_id}")
    public Map addParise(@PathVariable("post_id") Integer post_id, HttpServletRequest request, HttpServletResponse response) throws NullPointerException {

        Cookie information = getCookie(request);
        Map result = new HashMap();
        if (!(information == null)) {
            if (redisUtil.getKey(information.getValue() + post_id) == null) {
                Integer parise = (Integer) redisUtil.getKey1(post_id);
                System.out.println("post-id=" + post_id);
                System.out.println("点赞数：" + parise);
                System.out.println("parise=" + parise);
                redisUtil.setKey(information.getValue() + post_id, true);
                redisUtil.setExpire(information.getValue() + post_id, 12, TimeUnit.HOURS);
                parise += 1;
                redisUtil.setKey1(post_id, parise);
                result.put("code", 0);
                result.put("praise", parise);

            } else {
                result.put("code", 1);
                result.put("msg", "已经点赞过！");
            }
        } else {
            result.put("code", 1);
            result.put("msg", "已经点赞过！");
        }
        return result;
    }

    public Cookie create(HttpServletRequest request) {
        if (getCookie(request) == null) {
            Cookie cookie = new Cookie("INFORMATION", UUID.randomUUID().toString());

            cookie.setPath("/");
            return cookie;
        }
        return null;
    }

    public Cookie getCookie(HttpServletRequest request) {
        Cookie[] cookie = request.getCookies();
        if (!(cookie == null))
            for (Cookie c : cookie) {
                if ("INFORMATION".equals(c.getName())) {

                    return c;
                }
            }
        return null;
    }

    public List<BlogPost> getNewBlogPost(List<BlogPost> list) {
        List<BlogPost> blogPostList = new ArrayList<>();
        //   System.out.println("list=" + list.size());
        for (BlogPost b : list) {
//            System.out.println("post_id=" + b.getPost_id());
            b = blogPostService.getAndSetParise_Browsenumber_Comment_number(b);
//            System.out.println("b=" + b);
            if (b != null) {

                blogPostList.add(b);
            }
        }
//        System.out.println("getnewBlogts=" + blogPostList.size());
        return blogPostList;
    }

    /**
     * 设置在线人数和浏览人数
     *
     * @return
     */
    public BlogInform updateNumber(BlogInform blogInform, int flag) {
        Integer visiNumber = 0;
        Integer onlineNumber = 0;
        if (redisUtil.getNumber("visitNumber") != null)
            visiNumber = (Integer) redisUtil.getNumber("visitNumber");

        if (redisUtil.getNumber("onlineNumber") != null)
            onlineNumber = (Integer) redisUtil.getNumber("onlineNumber");

        if (onlineNumber == null) {
            redisUtil.setNumber("onlineNumber", 1);
        } else {
            if (flag == 1) {
                onlineNumber++;
                redisUtil.setNumber("onlineNumber", onlineNumber);
            }
            blogInform.setOnlineNumber(onlineNumber);
        }
        if (visiNumber == 0 || visiNumber == null) {
            System.out.println(blogInform);
            redisUtil.setNumber("visitNumber", blogInform.getVistNumber() + 1);
            blogInform.setVistNumber(blogInform.getVistNumber() + 1);
        } else {
            visiNumber++;
            blogInform.setVistNumber(visiNumber);
            if (visiNumber % 100 == 0) {
                blogUserService.updateBlogInform(blogInform);
            }
            redisUtil.setNumber("visitNumber", visiNumber);

        }
        return blogInform;
    }

}
