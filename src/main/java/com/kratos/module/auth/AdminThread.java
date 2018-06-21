package com.kratos.module.auth;


import com.kratos.module.auth.domain.Admin;

public class AdminThread extends UserThread<Admin> {
    private static AdminThread instance;

    private AdminThread() {
    }

    public static AdminThread getInstance() {
        if(instance == null) {
            instance = new AdminThread();
        }
        return instance;
    }
    public Admin get() {
        return super.get();
    }
}
