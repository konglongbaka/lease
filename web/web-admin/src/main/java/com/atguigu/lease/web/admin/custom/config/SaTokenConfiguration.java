package com.atguigu.lease.web.admin.custom.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
@Slf4j
public class SaTokenConfiguration extends WebMvcConfigurationSupport {
    protected void addInterceptors(InterceptorRegistry registry) {
        log.info("开始注册自定义拦截器...");
//        registry.addInterceptor(new SaInterceptor(handle -> {
//                   SaRouter.match("/admin/**",r->StpUtil.checkLogin());
//                   SaRouter.match("/admin/room/**",r->StpUtil.checkRole("admin"));
//                }))
//                .addPathPatterns("/admin/**")
//                .excludePathPatterns("/admin/login/**");

//        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
//                .addPathPatterns("/user/**")
//                .excludePathPatterns("/user/user/login", "/user/shop/status");
    }
}
