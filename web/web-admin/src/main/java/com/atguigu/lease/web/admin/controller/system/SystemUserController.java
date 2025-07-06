package com.atguigu.lease.web.admin.controller.system;


import com.alibaba.fastjson.JSON;
import com.atguigu.lease.common.result.Result;
import com.atguigu.lease.model.entity.SystemUser;
import com.atguigu.lease.model.enums.BaseStatus;
import com.atguigu.lease.web.admin.service.SystemUserService;
import com.atguigu.lease.web.admin.vo.system.user.SystemUserItemVo;
import com.atguigu.lease.web.admin.vo.system.user.SystemUserQueryVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.minidev.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


@Tag(name = "后台用户信息管理")
@RestController
@RequestMapping("/admin/system/user")
public class SystemUserController {
    @Autowired
    private SystemUserService systemUserService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Operation(summary = "根据条件分页查询后台用户列表")
    @GetMapping("page")
    public Result<IPage<SystemUserItemVo>> page(@RequestParam long current, @RequestParam long size, SystemUserQueryVo queryVo) {
        IPage<SystemUser> page = new Page<>(current, size);
        IPage<SystemUserItemVo> systemUserPage = systemUserService.pageSystemUserByQuery(page, queryVo);
        return Result.ok(systemUserPage);
    }

//    @Operation(summary = "根据ID查询后台用户信息")
//    @GetMapping("getById")
//    public Result<SystemUserItemVo> getById(@RequestParam Long id) {
//        if (id == null) {
//            return Result.fail();
//        }
//        String s = stringRedisTemplate.opsForValue().get("user:" + id);
//        if (ObjectUtils.isNotEmpty(s)){
//            return Result.ok(JSON.parseObject(s, SystemUserItemVo.class));
//        }
//        else if (Objects.equals(s, "")){
//            return Result.fail();
//        }
//        else {
//            SystemUserItemVo systemUser = systemUserService.getSystemUserById(id);
//            if (systemUser == null) {
//                stringRedisTemplate.opsForValue().set("user:" + id,"",30, TimeUnit.SECONDS);
//                return Result.fail();
//            }
//            String userStr = JSON.toJSONString(systemUser);
//            stringRedisTemplate.opsForValue().set("user:" + id,userStr);
//            return Result.ok(systemUser);
//        }
//    }
@Operation(summary = "根据ID查询后台用户信息")
@GetMapping("getById")
@Cacheable(cacheNames = "user:detail", key = "'user'+#id", unless = "#result == null")
public Result<SystemUserItemVo> getById(@RequestParam Long id) {
    SystemUser systemUser = systemUserService.getById(id);
    if (systemUser == null) {
        // 如果需要缓存null值，可以在这里返回一个默认结果
        return Result.ok(new SystemUserItemVo()); // 或者其他表示未找到的默认对象
    }
    SystemUserItemVo systemUserItemVo = new SystemUserItemVo();
    systemUserItemVo.setUsername(systemUser.getUsername());
    systemUserItemVo.setName(systemUser.getName());
    return Result.ok(systemUserItemVo);
}

    @Operation(summary = "保存或更新后台用户信息")
    @PostMapping("saveOrUpdate")
    public Result saveOrUpdate(@RequestBody SystemUser systemUser) {
        if (systemUser.getPassword()!=null){
            systemUser.setPassword(DigestUtils.md5Hex(systemUser.getPassword()));
        }
        systemUserService.saveOrUpdate(systemUser);
        return Result.ok();
    }

    @Operation(summary = "判断后台用户名是否可用")
    @GetMapping("isUserNameAvailable")
    public Result<Boolean> isUsernameExists(@RequestParam String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        long count = systemUserService.count(queryWrapper);
        return Result.ok(count == 0);
    }

    @DeleteMapping("deleteById")
    @Operation(summary = "根据ID删除后台用户信息")
    public Result removeById(@RequestParam Long id) {
        systemUserService.removeById(id);
        return Result.ok();
    }

    @Operation(summary = "根据ID修改后台用户状态")
    @PostMapping("updateStatusByUserId")
    public Result updateStatusByUserId(@RequestParam Long id, @RequestParam BaseStatus status) {
        LambdaUpdateWrapper<SystemUser> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SystemUser::getId, id);
        updateWrapper.set(SystemUser::getStatus, status);
        systemUserService.update(updateWrapper);
        return Result.ok();
    }
}
