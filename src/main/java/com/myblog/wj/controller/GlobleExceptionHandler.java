package com.myblog.wj.controller;

import com.myblog.wj.controller.psersonalUser.BlogUserController;
import com.myblog.wj.util.JWTException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobleExceptionHandler  {
    @Resource
    BlogUserController blogUserController;
    @ExceptionHandler(Exception.class)
    public String exceptionHandle(HttpServletRequest request, Exception e){
        if (e.getClass().getName().equals("io.jsonwebtoken.JwtException"))
        {
            request.setAttribute("msg",e.getMessage());
            request.setAttribute("code",e.getCause());
            //将异常进行分类，不同的异常进行不同的处理
            return "JwtError";
        }else{
            request.setAttribute("msg","服务器繁忙！");
            request.setAttribute("code",e.getCause());
            return "Error";
        }

}
    @ExceptionHandler(JWTException.class)
public ModelAndView JwtException(JWTException e, HttpServletRequest request, HttpServletResponse response){
        blogUserController.signOut(request,response);
        request.setAttribute("msg",e.getMessage());
        request.setAttribute("code",e.getCause());
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.setViewName("JwtError");
    return modelAndView;
}
@ExceptionHandler(NullPointerException.class)
    public String NullException(NullPointerException e ,HttpServletRequest request,HttpServletResponse response){
    blogUserController.signOut(request,response);
    request.setAttribute("msg",e.getMessage());
    request.setAttribute("code",e.getCause());
    return "JwtError";

}
}
