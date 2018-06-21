package com.kratos.common.utils;

public class OSUtils {
    private OSUtils() {

    }
    public static boolean isWindows() {
        String osName = System.getProperty("os.name").toLowerCase();
        return osName.contains("windows");
    }
}
