package com.kratos.config;

/**
 * 框架配置
 */
public class FrameworkProperties {
    private static String projectName = "/framework";

    public static String getProjectName() {
        return projectName;
    }

    public static void setProjectName(String projectName) {
        FrameworkProperties.projectName = projectName;
    }
}
