package com.kratos.module.auth;


import com.kratos.entity.BaseUser;

public class UserThread<T extends BaseUser> {
    private static ThreadLocal<BaseUser> userThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> clientIdThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> ipAddressThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<String> accessTokenThreadLocal = new ThreadLocal<>();
    private static UserThread instance;
    public UserThread() {}

    public static UserThread getInstance() {
        if(instance == null) {
            instance = new UserThread();
        }
        return instance;
    }

    public void set(T user) {
        userThreadLocal.set(user);
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return (T) userThreadLocal.get();
    }

    public void setClientId(String clientId) {
        clientIdThreadLocal.set(clientId);
    }

    public String getClientId() {
        return clientIdThreadLocal.get();
    }

    public void setIpAddress(String ipAddress) {
        ipAddressThreadLocal.set(ipAddress);
    }

    public String getIpAddress() {
        return ipAddressThreadLocal.get();
    }

    public String getAccessToken() {
        return accessTokenThreadLocal.get();
    }

    public void setAccessToken(String accessToken) {
        accessTokenThreadLocal.set(accessToken);
    }

    public void clear() {
        userThreadLocal.remove();
    }
}
