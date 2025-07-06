# 在线租赁平台 - 公寓租赁管理系统

## 项目简介
本项目是一个基于SpringBoot的现代化租赁平台，集成了**移动端用户服务**与**后台管理系统**。平台实现了从房源浏览、预约看房到租约管理的全流程数字化，为租客提供便捷的找房体验，为管理人员提供高效的公寓管理工具。系统采用分布式架构设计，通过Redis缓存和Spring Cache优化性能，Sa-Token实现安全认证，大幅提升了平台的响应速度与管理效率。

## 技术栈
| 分类        | 技术组件                 |
|-------------|--------------------------|
| 核心框架    | SpringBoot 3.x           |
| 数据存储    | MySQL 8.0 + Redis 7.x    |
| 数据访问    | MyBatis-Plus             |
| 安全认证    | Sa-Token                 |
| 缓存管理    | Redis                    |
| 其他技术    | Spring AOP + Lombok      |



## ✨ 部分亮点技术实现

### 1.Redis 缓存登录验证码
```java
SpecCaptcha specCaptcha = new SpecCaptcha(130, 48, 4);
        specCaptcha.setCharType(Captcha.TYPE_DEFAULT);
        String code = specCaptcha.text().toLowerCase();
        String key = RedisConstant.ADMIN_LOGIN_PREFIX + UUID.randomUUID();
        String image = specCaptcha.toBase64();
        redisTemplate.opsForValue().set(key, code, RedisConstant.APP_LOGIN_CODE_TTL_SEC, TimeUnit.SECONDS);
        return new CaptchaVo(image, key);
```

### 2. Sa-Token鉴权
```java
public class SaTokenConfiguration extends WebMvcConfigurationSupport {
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handle -> {
                   SaRouter.match("/admin/**",r->StpUtil.checkLogin());
                   SaRouter.match("/admin/room/**",r->StpUtil.checkRole("admin"));
                }))
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login/**");

        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/user/**")
                .excludePathPatterns("/user/user/login", "/user/shop/status");
    }
}
//LoginController
    @Operation(summary = "登录")
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginVo loginVo) {
        String token = service.login(loginVo);
        StpUtil.login(token);
        return Result.ok(token);
    }
```
> *注*：其余实现已结合至苍穹外卖mp版
## 🛠️ 部署指南

### 环境要求
- JDK 17+
- MySQL 8.0+
- Redis 7.x+
