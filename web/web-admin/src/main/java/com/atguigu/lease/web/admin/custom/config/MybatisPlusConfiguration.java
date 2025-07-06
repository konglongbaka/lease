package com.atguigu.lease.web.admin.custom.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.atguigu.lease.web.admin.mapper")
public class MybatisPlusConfiguration {

}