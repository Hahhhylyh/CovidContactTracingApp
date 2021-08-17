package com.example.covidcontacttracing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout tilName, tilEmail, tilPw, tilRePw;
    private TextInputEditText etName, etEmail, etPw, etRePw;

    private DatabaseHelper myDb;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myDb = new DatabaseHelper(this);
        pref = this.getSharedPreferences("user_details", 0);

        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilPw = findViewById(R.id.til_pw);
        tilRePw = findViewById(R.id.til_re_pw);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPw = findViewById(R.id.et_pw);
        etRePw = findViewById(R.id.et_re_pw);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateName();
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateEmail();
            }
        });

        etPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validatePassword();
            }
        });

        etRePw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateRePassword();
            }
        });
    }

    // validation in register page - check for empty, length and format (email), username or email existed or not
    private boolean validateName() {
        String nameInput = tilName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            tilName.setError("Field cant't be empty");
            return false;
        } else if (nameInput.length() > 15) {
            tilName.setError("Username too long");
            return false;
        } else if (myDb.checkNameUniqueness(nameInput)) {
            tilName.setError("Username Existed");
            return false;
        } else {
            tilName.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String emailInput = tilEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            tilEmail.setError("Field cant't be empty");
            return false;
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            if (myDb.checkEmailUniqueness(emailInput)) {
                tilEmail.setError("Email Existed");
                return false;
            } else {
                tilEmail.setError(null);
                return true;
            }
        } else {
            tilEmail.setError("Invalid email format. Ex. example@mail.com");
            return false;
        }
    }

    public boolean validatePassword() {
        String pwInput = tilPw.getEditText().getText().toString().trim();

        if (pwInput.isEmpty()) {
            tilPw.setError("Field cant't be empty");
            return false;
        } else if (pwInput.length() < 8) {
            tilPw.setError("Password too short");
            return false;
        } else if (pwInput.length() > 20) {
            tilPw.setError("Password too long");
            return false;
        } else {
            tilPw.setError(null);
            return true;
        }
    }

    public boolean validateRePassword() {
        String pw = tilPw.getEditText().getText().toString().trim();
        String retryPw = tilRePw.getEditText().getText().toString().trim();

        if (retryPw.isEmpty()) {
            tilRePw.setError("Field cant't be empty");
            return false;
        } else if (retryPw.equals(pw)) {
            tilRePw.setError(null);
            return true;
        } else {
            tilRePw.setError("Passwords does not match");
            return false;
        }
    }

    // activate when user click on 'register' button
    public void register(View v) {
        // check if the validation passed
        if (!validateName() || !validateEmail() || !validatePassword() || !validateRePassword()) {
            Toast.makeText(this, "Error detected, please check all the input fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // add new user to database
        int uid = myDb.addUser(etEmail.getText().toString(), etName.getText().toString(), etPw.getText().toString());
        if(uid == -1) // failed to insert
        {
            Toast.makeText(RegisterActivity.this, "Register failed !", Toast.LENGTH_SHORT).show();
        }
        else    // create a shared preference storing the user id and direct to home activity (no need login again)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("uid", String.valueOf(uid));
            editor.commit();
            Toast.makeText(RegisterActivity.this, "Registered successfully ~", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void haveAccount(View v) {
        super.onBackPressed();
    }
}