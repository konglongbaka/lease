package com.atguigu.lease.common.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.SaTokenException;
import com.atguigu.lease.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e){
        e.printStackTrace();
        return Result.fail();
    }

    @ExceptionHandler(LeaseException.class)
    @ResponseBody
    public Result deleteException(LeaseException e){
        return Result.fail(e.getCode(),e.getMessage());
    }
    @ExceptionHandler
    @ResponseBody
    public Result notLogin(NotLoginException e){
        return Result.fail(e.getCode(),"请先进行登录");
    }
    @ExceptionHandler
    @ResponseBody
    public Result notPermission(SaTokenException e){
        return Result.fail(e.getCode(),"无权限");
    }
}
