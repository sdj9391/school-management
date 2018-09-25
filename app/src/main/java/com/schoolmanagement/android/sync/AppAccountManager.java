package com.schoolmanagement.android.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.schoolmanagement.android.models.User;
import com.schoolmanagement.android.utils.AppUtils;
import com.schoolmanagement.android.utils.DebugLog;

public class AppAccountManager {

    private static final String KEY_USER_DETAILS = "logged_in_user_details";
    private static final String KEY_IS_VERIFIED_USER = "user.isVerified";

    private final AccountManager accountManager;
    protected Account account;
    private static AppAccountManager appAccountManager;

    public static AppAccountManager getInstance(Context context, String accountType) {
        if (appAccountManager == null) {
            appAccountManager = new AppAccountManager(context, accountType);
            DebugLog.d("new instance creation");
        }
        return appAccountManager;
    }

    public void invalidate() {
        appAccountManager = null;
    }

    public AppAccountManager(Context context, String accountType) {
        this.accountManager = AccountManager.get(context);
        Account[] accounts = accountManager.getAccountsByType(accountType);
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
    }

    /**
     * @return Null if the user is not logged in.
     */
    public Account getAccount() {
        return account;
    }


    public void removeAccount() {
        if (account != null) {
            accountManager.removeAccount(account, null, null);
        }
    }

    public void setUserDetails(User user) {
        String udString = new Gson().toJson(user);

        if (account == null) {
            DebugLog.e("Account is null. Not setting user data");
            return;
        }

        accountManager.setUserData(account, KEY_USER_DETAILS, udString);
    }

    @Nullable
    /**
     *
     */
    public User getUserDetails() {

        if (account == null) {
            DebugLog.d("User account not found!");
            return null;
        }

        String userDetails = accountManager.getUserData(account, KEY_USER_DETAILS);

        if (userDetails != null) {
            return new Gson().fromJson(userDetails, User.class);
        } else {
            return null;
        }
    }

    public void setVerifiedUser(boolean isVerified) {
        if (account == null) {
            DebugLog.d("User account not found!");
            return;
        }

        accountManager.setUserData(account, AppAccountManager.KEY_IS_VERIFIED_USER, Boolean.toString(isVerified));
    }

    public boolean isVerifiedUser(Context context, String accountType) {

        Account[] accounts = AccountManager.get(context)
                .getAccountsByType(accountType);
        if (accounts.length > 0) {
            DebugLog.d("account found");
            account = accounts[0];
        } else {
            DebugLog.d("account NOT found");
            account = null;
        }

        if (account == null) {
            return false;
        }

        String booleanStr = accountManager.getUserData(account, KEY_IS_VERIFIED_USER);

        if (AppUtils.isEmpty(booleanStr)) {
            return false;
        } else {
            return Boolean.parseBoolean(booleanStr);
        }
    }

    /**
     * @return
     * @deprecated Use {@link #isUserLoggedIn()}
     */
    public boolean isUserLoggedIn(Context context, String accountType) {
        Account[] accounts = AccountManager.get(context)
                .getAccountsByType(accountType);
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return getUserDetails() != null;
    }

    /**
     * @return
     */
    public boolean isUserLoggedIn() {
        return getUserDetails() != null;
    }
}
