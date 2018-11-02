package com.schoolmanagement.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.schoolmanagement.android.R;
import com.schoolmanagement.android.models.User;
import com.schoolmanagement.android.sync.AppAccountManager;
import com.schoolmanagement.android.utils.AppConfig;

public class SplashActivity extends AppCompatActivity {
    private static final int DELAY_MS = 1800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sleepAndContinue();
    }

    private void dispatchActivity() {
        User user = AppAccountManager.getInstance(this,
                AppConfig.getInstance().getSyncAccountType()).getUserDetails();
        Intent intent = new Intent();
        if (user == null) {
            intent.setClass(this, LoginActivity.class);
        } else {
            intent.setClass(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void sleepAndContinue() {
        Runnable activityStart = this::dispatchActivity;
        new Handler().postDelayed(activityStart, DELAY_MS);
    }
}
