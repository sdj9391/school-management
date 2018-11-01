package com.schoolmanagement.android.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.schoolmanagement.android.R;

public class SignUpActivity extends BaseActivity {

    private EditText firstNameEditText, lastNameExitText, emailExitText, passwordExitText,
            mobileNumExitText, schoolNameExitText, specializationExitText;
    private TextView teacherRoleOption, parentRoleOption, studentRoleOption;
    private String roleSelected = null;

    private View.OnClickListener onSignUpClickListener = v -> validateData();
    private View.OnClickListener onRoleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            teacherRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_outline_primary));
            parentRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_outline_primary));
            studentRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_outline_primary));
            switch (v.getId()) {
                case R.id.role_teacher:
                    teacherRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_filled_primary));
                    roleSelected = getString(R.string.label_teacher);
                    break;
                case R.id.role_parent:
                    parentRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_filled_primary));
                    roleSelected = getString(R.string.label_parent);
                    break;
                case R.id.role_student:
                    studentRoleOption.setBackground(getResources().getDrawable(R.drawable.bg_filled_primary));
                    roleSelected = getString(R.string.label_student);
                    break;
                default:
                    roleSelected = null;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstNameEditText = findViewById(R.id.edit_first_name);
        lastNameExitText = findViewById(R.id.edit_last_name);
        emailExitText = findViewById(R.id.edit_email_name);
        passwordExitText = findViewById(R.id.edit_password);
        mobileNumExitText = findViewById(R.id.edit_mobile_num);
        schoolNameExitText = findViewById(R.id.edit_school);
        specializationExitText = findViewById(R.id.edit_specialization);
        teacherRoleOption = findViewById(R.id.role_teacher);
        teacherRoleOption.setOnClickListener(onRoleClickListener);
        parentRoleOption = findViewById(R.id.role_parent);
        studentRoleOption = findViewById(R.id.role_student);
        Button signUpButton = findViewById(R.id.button_sign_up);
        signUpButton.setOnClickListener(onSignUpClickListener);
    }

    private void validateData() {

    }
}
