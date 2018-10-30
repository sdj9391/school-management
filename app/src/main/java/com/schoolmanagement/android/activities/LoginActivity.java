package com.schoolmanagement.android.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.schoolmanagement.android.MultiDexApp;
import com.schoolmanagement.android.R;
import com.schoolmanagement.android.models.User;
import com.schoolmanagement.android.restapis.AppApiInstance;
import com.schoolmanagement.android.utils.AppUtils;
import com.schoolmanagement.android.utils.DebugLog;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

    private EditText mobileNumEditText, passwordEditText;
    private View.OnClickListener onSignInClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String mobileNum = mobileNumEditText.getText().toString().trim();
            if (AppUtils.isEmpty(mobileNum)) {
                mobileNumEditText.setError(getString(R.string.error_required));
                mobileNumEditText.requestFocus();
                return;
            }

            if (!AppUtils.isValidMobileNum(mobileNum)) {
                mobileNumEditText.setError(getString(R.string.error_invalid_mobile_num));
                mobileNumEditText.requestFocus();
                return;
            }

            String password = passwordEditText.getText().toString().trim();
            if (AppUtils.isEmpty(password)) {
                passwordEditText.setError(getString(R.string.error_required));
                passwordEditText.requestFocus();
                return;
            }

            login(mobileNum, password);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        View signInButton = findViewById(R.id.button_sing_in);
        signInButton.setOnClickListener(onSignInClickListener);

        mobileNumEditText = findViewById(R.id.input_mobile_num);
        passwordEditText = findViewById(R.id.input_password);
    }

    private void login(String userName, String password) {
        if (AppUtils.isEmpty(userName) || AppUtils.isEmpty(password)) {
            DebugLog.e("User name or password is empty");
            return;
        }

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);

        Observable<Response<User>> observable = AppApiInstance.getApi().login(user);
        AppUtils.showProgressDialog(this, null, getString(R.string.msg_please_wait), true);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse, this::handleError);
    }

    private void handleError(Throwable throwable) {
        AppUtils.dismissProgressDialog();
        DebugLog.e("Error: " + AppUtils.parse(this, throwable));
        showMessage(AppUtils.parse(this, throwable));
    }

    private void handleResponse(Response<User> response) {
        AppUtils.dismissProgressDialog();

        if (response == null) {
            showMessage(AppUtils.parse(this, response));
            return;
        }
        if (!response.isSuccessful()) {
            showMessage(AppUtils.parse(this, response));
            return;
        }
        User user = response.body();
        if (user == null) {
            DebugLog.e("User found null");
            return;
        }

        DebugLog.v("Data: " + new Gson().toJson(user));
        // reinitialize config after successful auth
        ((MultiDexApp) this.getApplicationContext()).initAppConfig();
        setResult(Activity.RESULT_OK);
        toMainActivity();
    }

    protected void toMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
