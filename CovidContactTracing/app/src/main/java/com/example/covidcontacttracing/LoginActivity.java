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

public class LoginActivity extends AppCompatActivity {

    private Button btnRegister;
    private TextInputLayout tilName, tilPw;
    private TextInputEditText etName, etPw;

    private DatabaseHelper myDb;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new DatabaseHelper(this);
        pref = this.getSharedPreferences("user_details", 0);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        tilName = findViewById(R.id.til_name);
        tilPw = findViewById(R.id.til_pw);
        etName = findViewById(R.id.et_name);
        etPw = findViewById(R.id.et_pw);

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
    }

    // input field validation in Login page - just check whether it is empty or not only
    private boolean validateName() {
        String nameInput = tilName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            tilName.setError("Field cant't be empty");
            return false;
        }  else {
            tilName.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String pwInput = tilPw.getEditText().getText().toString().trim();

        if (pwInput.isEmpty()) {
            tilPw.setError("Field cant't be empty");
            return false;
        }  else {
            tilPw.setError(null);
            return true;
        }
    }

    // activate when 'sign in' button is clicked
    public void login(View v) {
        // check if the validation is passed
        if (!validateName() ||  !validatePassword()) {
            Toast.makeText(this, "Error - Please check all the input fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = etName.getText().toString();
        String pw = etPw.getText().toString();

        //check credential - if return value > 0 ? create shared preferences with user id + direct to Home page : login failed
        int user_id = myDb.checkCredential(name, pw);
        if(user_id > 0)
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("uid", String.valueOf(user_id));
            editor.commit();
            Toast.makeText(LoginActivity.this, "Signed in successfully.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            Toast.makeText(LoginActivity.this, "Invalid credentials. Authentication failed.", Toast.LENGTH_SHORT).show();
        }
    }
}