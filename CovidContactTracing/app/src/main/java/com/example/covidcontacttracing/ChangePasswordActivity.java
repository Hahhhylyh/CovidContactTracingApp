package com.example.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    SharedPreferences pref;

    private TextInputLayout tilOldPw, tilNewPw, tilConfirmedPw;
    private TextInputEditText etOldPw, etNewPw, etConfirmedPw;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Changing Password");

        myDb = new DatabaseHelper(this);
        pref = this.getSharedPreferences("user_details", 0);
        uid = pref.getString("uid", null);

        tilOldPw = findViewById(R.id.til_old_pw);
        tilNewPw = findViewById(R.id.til_new_pw);
        tilConfirmedPw = findViewById(R.id.til_confirmed_pw);
        etOldPw = findViewById(R.id.et_old_pw);
        etNewPw = findViewById(R.id.et_new_pw);
        etConfirmedPw = findViewById(R.id.et_confirmed_pw);

        etOldPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateOldPassword();
            }
        });

        etNewPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateNewPassword();
            }
        });

        etConfirmedPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateConfirmedPassword();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    // validation at ChangePasswordPage - check about empty field and password length only
    // except confirmPassword field - check if the confirmed password is same as new password or not
    public boolean validateOldPassword() {
        String oldPwInput = tilOldPw.getEditText().getText().toString().trim();

        if (oldPwInput.isEmpty()) {
            tilOldPw.setError("Field cant't be empty");
            return false;
        } else if (oldPwInput.length() < 8) {
            tilOldPw.setError("Password too short");
            return false;
        } else if (oldPwInput.length() > 20) {
            tilOldPw.setError("Password too long");
            return false;
        } else {
            tilOldPw.setError(null);
            return true;
        }
    }

    public boolean validateNewPassword() {
        String newPwInput = tilNewPw.getEditText().getText().toString().trim();

        if (newPwInput.isEmpty()) {
            tilNewPw.setError("Field cant't be empty");
            return false;
        } else if (newPwInput.length() < 8) {
            tilNewPw.setError("Password too short");
            return false;
        } else if (newPwInput.length() > 20) {
            tilNewPw.setError("Password too long");
            return false;
        } else {
            tilNewPw.setError(null);
            return true;
        }
    }

    public boolean validateConfirmedPassword() {
        String newPw = tilNewPw.getEditText().getText().toString().trim();
        String retryPw = tilConfirmedPw.getEditText().getText().toString().trim();

        if (retryPw.isEmpty()) {
            tilConfirmedPw.setError("Field cant't be empty");
            return false;
        } else if (retryPw.equals(newPw)) {
            tilConfirmedPw.setError("Password match");
            return true;
        } else {
            tilConfirmedPw.setError("Passwords does not match");
            return false;
        }
    }

    // Activate when user click the 'update password' button
    public void updatePassword(View v) {
        // check if all the 3 input fields passed
        if (!validateOldPassword() || !validateNewPassword() || !validateConfirmedPassword()) {
            Toast.makeText(this, "Error - Please check all the input fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // then, first check if old password is valid or not
        Cursor res = myDb.getUserData(uid);
        if(res.moveToNext()){
            String pw = res.getString(res.getColumnIndex("password"));

            // if wrong old password - Toast
            if (!etOldPw.getText().toString().equals(pw)) {
                Toast.makeText(this, "Invalid Old Password!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // update password in database
        Boolean success = myDb.updatePassword(uid, etNewPw.getText().toString());
        if(success)
        {
            Toast.makeText(this, "Password Changed Successfully ~", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this, "Password failed to change!",Toast.LENGTH_SHORT).show();
        }
    }

    public void cancel(View v) {
        super.onBackPressed();
    }
}