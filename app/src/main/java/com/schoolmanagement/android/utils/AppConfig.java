package com.schoolmanagement.android.utils;

import android.support.annotation.NonNull;

/**
 * Config setter for app.
 * For more documentation about supported fields,
 * read the accessor methods.
 */
public class AppConfig {

    private static AppConfig appConfig;
    private String token;
    private String serverUrl;

    @NonNull
    public static AppConfig getInstance() {
        if (appConfig == null) {
            appConfig = new AppConfig();
        }

        return appConfig;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
}
