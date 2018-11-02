package com.schoolmanagement.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class SignUpActivity extends BaseActivity {

    static final int MIN_PASSWORD_LENGTH = 8;
    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText,
            mobileNumEditText, schoolNameEditText, specializationEditText;
    private TextView teacherRoleOption, parentRoleOption, studentRoleOption;
    private String userRole = null;

    private View.OnClickListener onSignUpClickListener = v -> validateData();
    private View.OnClickListener onRoleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            teacherRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_outline_primary));
            parentRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_outline_primary));
            studentRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_outline_primary));
            teacherRoleOption.setTextColor(getResources().getColor(R.color.subtitle_text_color));
            parentRoleOption.setTextColor(getResources().getColor(R.color.subtitle_text_color));
            studentRoleOption.setTextColor(getResources().getColor(R.color.subtitle_text_color));
            view.setBackground(getResources().getDrawable(R.drawable.bg_filled_primary));
            ((TextView) view).setTextColor(getResources().getColor(android.R.color.white));
            switch (view.getId()) {
                case R.id.role_teacher:
                    userRole = User.USER_ROLE_TEACHER;
                    break;
                case R.id.role_parent:
                    userRole = User.USER_ROLE_PARENT;
                    break;
                case R.id.role_student:
                    userRole = User.USER_ROLE_STUDENT;
                    break;
                default:
                    userRole = null;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameEditText = findViewById(R.id.edit_last_name);
        emailEditText = findViewById(R.id.edit_email);
        passwordEditText = findViewById(R.id.edit_password);
        mobileNumEditText = findViewById(R.id.edit_mobile_num);
        schoolNameEditText = findViewById(R.id.edit_school);
        specializationEditText = findViewById(R.id.edit_specialization);
        teacherRoleOption = findViewById(R.id.role_teacher);
        teacherRoleOption.setOnClickListener(onRoleClickListener);
        parentRoleOption = findViewById(R.id.role_parent);
        parentRoleOption.setOnClickListener(onRoleClickListener);
        studentRoleOption = findViewById(R.id.role_student);
        studentRoleOption.setOnClickListener(onRoleClickListener);
        Button signUpButton = findViewById(R.id.button_sign_up);
        signUpButton.setOnClickListener(onSignUpClickListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        toLoginActivity(null);
    }

    private void validateData() {
        String firstName = AppUtils.getMandatoryDataFromEditText(this, firstNameEditText);
        if (firstName == null) {
            return;
        }

        String lastName = AppUtils.getMandatoryDataFromEditText(this, lastNameEditText);
        if (lastName == null) {
            return;
        }

        String email = AppUtils.getMandatoryDataFromEditText(this, emailEditText);
        if (email == null) {
            return;
        }

        if (!AppUtils.isEmailValid(email)) {
            emailEditText.setError(getString(R.string.error_not_valid));
            emailEditText.requestFocus();
            return;
        }

        String password = AppUtils.getMandatoryDataFromEditText(this, passwordEditText);
        if (password == null) {
            return;
        }

        if (password.length() < MIN_PASSWORD_LENGTH) {
            passwordEditText.setError(getString(R.string.error_min_eight_char));
            passwordEditText.requestFocus();
            return;
        }

        String mobileNum = AppUtils.getMandatoryDataFromEditText(this, mobileNumEditText);
        if (mobileNum == null) {
            return;
        }

        if (!AppUtils.isValidMobileNum(mobileNum)) {
            mobileNumEditText.setError(getString(R.string.error_not_valid));
            mobileNumEditText.requestFocus();
            return;
        }

        String schoolName = AppUtils.getMandatoryDataFromEditText(this, schoolNameEditText);
        if (schoolName == null) {
            return;
        }

        String specialization = AppUtils.getMandatoryDataFromEditText(this, specializationEditText);
        if (specialization == null) {
            return;
        }

        if (AppUtils.isEmpty(userRole)) {
            showMessage(getString(R.string.msg_select_role));
            return;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setMobileNumber(mobileNum);
        user.setSchoolName(schoolName);
        user.setSpecialization(specialization);
        user.setRole(userRole);

        signUpApiCall(user);
    }

    private void signUpApiCall(User user) {
        if (user == null) {
            DebugLog.e("User found null");
            return;
        }

        AppUtils.showProgressDialog(this, null, getString(R.string.msg_please_wait), true);
        Observable<Response<User>> observable = AppApiInstance.getApi().userRegistration(user);
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
        toLoginActivity(user.getEmail());
    }

    private void toLoginActivity(String email) {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra(LoginActivity.INTENT_EXTRA_EMAIL, email);
        startActivity(intent);
        finish();
    }
}
