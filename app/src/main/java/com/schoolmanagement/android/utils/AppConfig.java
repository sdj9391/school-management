package com.schoolmanagement.android.utils;

import android.support.annotation.NonNull;

/**
 * Config setter for app.
 * For more documentation about supported fields,
 * read the accessor methods.
 */
public class AppConfig {

    private static AppConfig sAppConfig;

    private String role;
    private String jwtToken;
    private String serverUrl;

    /**
     * To use file provider authority throughout app and also in modules
     */
    private String providerAuthority;

    /**
     * To get user from the account manager
     * We can retrieve the user account using BaseAccountManager from social core module.
     */
    private String syncAccountType;
    /**
     * Set sync content authority
     */
    private String syncContentAuthority;

    /**
     * To set application name and use it to store the downloaded files.
     * It is useful in BaseSocialFeedFragment
     */
    private String appName;

    @NonNull
    public static AppConfig getInstance() {
        if (sAppConfig == null) {
            sAppConfig = new AppConfig();
        }

        return sAppConfig;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public String getProviderAuthority() {
        return providerAuthority;
    }

    public void setProviderAuthority(String providerAuthority) {
        this.providerAuthority = providerAuthority;
    }

    public String getSyncAccountType() {
        return syncAccountType;
    }

    public void setSyncAccountType(String syncAccountType) {
        this.syncAccountType = syncAccountType;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getSyncContentAuthority() {
        return syncContentAuthority;
    }

    public void setSyncContentAuthority(String syncContentAuthority) {
        this.syncContentAuthority = syncContentAuthority;
    }
}
