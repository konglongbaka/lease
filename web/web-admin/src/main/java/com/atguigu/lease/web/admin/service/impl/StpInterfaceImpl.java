package com.atguigu.lease.web.admin.service.impl;

import cn.dev33.satoken.stp.StpInterface;
import com.atguigu.lease.model.entity.Role;
import com.atguigu.lease.model.entity.SystemUser;
import com.atguigu.lease.web.admin.service.RoleService;
import com.atguigu.lease.web.admin.service.SystemUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.Permission;
import java.util.ArrayList;
import java.util.List;

@Component
public class StpInterfaceImpl implements StpInterface {
    @Autowired
    private RoleService roleService;
    @Autowired
    private SystemUserService systemUserService;

    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        return List.of();
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        SystemUser employee = systemUserService.getById((Serializable) loginId);
        List<String> list = new ArrayList<>();
        Role role = roleService.getById(employee.getRoleId());
        list.add(role.getRoleName());
        return list;
    }
}
