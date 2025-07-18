package com.atguigu.lease.common.context;

import com.atguigu.lease.common.context.LoginUser;

public class LoginUserContext {

    private static final ThreadLocal<LoginUser> userThreadLocal = new ThreadLocal<>();

    public static void setLoginUser(LoginUser loginUser) {
        userThreadLocal.set(loginUser);
    }

    public static LoginUser getLoginUser() {
        return userThreadLocal.get();
    }

    public static void clear() {
        userThreadLocal.remove();
    }
}