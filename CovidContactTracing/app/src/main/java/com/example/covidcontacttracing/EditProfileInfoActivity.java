package com.example.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

public class EditProfileInfoActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    SharedPreferences pref;

    private TextInputLayout tilName, tilEmail, tilTel;
    private TextInputEditText etName, etEmail, etTel, etDob;
    private RadioGroup radioGroup;
    private RadioButton radioButton, rbMale, rbFemale;
    private AutoCompleteTextView autoCompleteTextView;
    private DatePickerDialog datePickerDialog;

    String uid, username, email, gender, dob, tel, state;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_info);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Editing Profile");

        myDb = new DatabaseHelper(this);
        pref = this.getSharedPreferences("user_details", 0);

        tilName = findViewById(R.id.til_name);
        tilEmail = findViewById(R.id.til_email);
        tilTel = findViewById(R.id.til_tel);
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etTel = findViewById(R.id.et_tel);

        uid = pref.getString("uid", null);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateName();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        etTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateTel();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        radioGroup = findViewById(R.id.radio_group);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);

        // Populate items in dropdown list
        String[] states = getResources().getStringArray(R.array.states);
        arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.dropdown_item_states, states);
        autoCompleteTextView.setAdapter(arrayAdapter);

        // Date Picker
        etDob = findViewById(R.id.et_dob);
        initDatePicker();

        retrieveProfileData();
    }

    // first, populate the input field with existing data (from database)
    private void retrieveProfileData() {
        Cursor res = myDb.getUserData(uid);
        if(res.moveToNext()){
            username = res.getString(res.getColumnIndex("name"));
            email = res.getString(res.getColumnIndex("email"));
            gender = res.getString(res.getColumnIndex("gender"));
            dob = res.getString(res.getColumnIndex("dob"));
            tel = res.getString(res.getColumnIndex("tel"));
            state = res.getString(res.getColumnIndex("state"));
        }

        etName.setText(username);
        etEmail.setText(email);

        // if user haven't fill their profile yet, show default value
        if(gender == null) {
            etDob.setText(getTodayDate()); // Default birthday - today
            rbMale.setChecked(true); // Default gender - Male
            autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false);    // Default state - Johor
        }
        else { // show previous saved profile info to edit
            etTel.setText(tel);
            etDob.setText(dob);
            if(gender.equals("Male"))
                rbMale.setChecked(true);
            else
                rbFemale.setChecked(true);

            switch(state) {
                case "Johor":
                    autoCompleteTextView.setText(arrayAdapter.getItem(0).toString(), false);
                    break;

                case "Kedah":
                    autoCompleteTextView.setText(arrayAdapter.getItem(1).toString(), false);
                    break;

                case "Kelantan":
                    autoCompleteTextView.setText(arrayAdapter.getItem(2).toString(), false);
                    break;

                case "Melaka":
                    autoCompleteTextView.setText(arrayAdapter.getItem(3).toString(), false);
                    break;

                case "Negeri Sembilan":
                    autoCompleteTextView.setText(arrayAdapter.getItem(4).toString(), false);
                    break;

                case "Pahang":
                    autoCompleteTextView.setText(arrayAdapter.getItem(5).toString(), false);
                    break;

                case "Penang":
                    autoCompleteTextView.setText(arrayAdapter.getItem(6).toString(), false);
                    break;

                case "Perak":
                    autoCompleteTextView.setText(arrayAdapter.getItem(7).toString(), false);
                    break;

                case "Perlis":
                    autoCompleteTextView.setText(arrayAdapter.getItem(8).toString(), false);
                    break;

                case "Sabah":
                    autoCompleteTextView.setText(arrayAdapter.getItem(9).toString(), false);
                    break;

                case "Sarawak":
                    autoCompleteTextView.setText(arrayAdapter.getItem(10).toString(), false);
                    break;

                case "Selangor":
                    autoCompleteTextView.setText(arrayAdapter.getItem(11).toString(), false);
                    break;

                case "Terengganu":
                    autoCompleteTextView.setText(arrayAdapter.getItem(12).toString(), false);
                    break;

                case "Kuala Lumpur":
                    autoCompleteTextView.setText(arrayAdapter.getItem(13).toString(), false);
                    break;

                case "Labuan":
                    autoCompleteTextView.setText(arrayAdapter.getItem(14).toString(), false);
                    break;

                case "Putrajaya":
                    autoCompleteTextView.setText(arrayAdapter.getItem(15).toString(), false);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    // Validation at EditProfile - mainly check for empty, specific length and format
    // Name and Email - check if any existed name or email in the database
    private boolean validateName() {
        String nameInput = tilName.getEditText().getText().toString().trim();

        if (nameInput.isEmpty()) {
            tilName.setError("Field cant't be empty");
            return false;
        } else if (nameInput.length() > 15) {
            tilName.setError("Username too long");
            return false;
        } else if (myDb.checkNameUniqueness(nameInput) && !etName.getText().toString().equals(username)) {
            // if the desired name is found in the database and yet not the one the user previously using
            tilName.setError("Username Existed");
            return false;
        } else {
            tilName.setError(null);
            return true;
        }
    }

    // Validation at EditProfile - mainly check for empty, specific length and format
    // Name and Email - check if any existed name or email in the database
    private boolean validateEmail() {
        String emailInput = tilEmail.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            tilEmail.setError("Field cant't be empty");
            return false;
        } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            if (myDb.checkEmailUniqueness(emailInput) && !etEmail.getText().toString().equals(email)) {
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

    private boolean validateTel() {
        String telInput = tilTel.getEditText().getText().toString().trim();

        if (telInput.isEmpty()) {
            tilTel.setError("Field cant't be empty");
            return false;
        } else if (telInput.length() >= 9 && telInput.length() <= 10){
            tilTel.setError(null);
            return true;
        } else {
            tilTel.setError("Invalid phone number");
            return false;
        }
    }

    public String getRadioText() {
        int radioId = radioGroup.getCheckedRadioButtonId();

        radioButton = findViewById(radioId);

        return radioButton.getText().toString();
    }

    public String getDropDownText() {
        return autoCompleteTextView.getText().toString();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1; // month should start from 1 to 12
                String date = makeDateString(day, month, year);
                etDob.setText(date);
            }
        };

        // create a date (today), set as maximum data in the date picker
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()); // avoid user choosing birthday beyond today
    }

    // default birthday is today (the position of the date picker)
    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // after user choosing from the date picker, the choosen date will be shown as text format
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) return "Jan";
        if (month == 2) return "Feb";
        if (month == 3) return "Mar";
        if (month == 4) return "Apr";
        if (month == 5) return "May";
        if (month == 6) return "Jun";
        if (month == 7) return "Jul";
        if (month == 8) return "Aug";
        if (month == 9) return "Sep";
        if (month == 10) return "Oct";
        if (month == 11) return "Nov";
        if (month == 12) return "Dec";

        // default
        return "Jan";
    }

    public void openDatePicker(View v){
        datePickerDialog.show();
    }

    public String getBirthday() {
        return etDob.getText().toString();
    }

    // activate when user click 'save changes'
    public void confirmInput(View v) {
        // check all input field validation passed
        if (!validateName() || !validateEmail() || !validateTel()) {
            Toast.makeText(this, "Error - Please check all the input fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // try update database
        Boolean success = myDb.updateUser(uid, etName.getText().toString(), etEmail.getText().toString(), etTel.getText().toString(), getBirthday(), getRadioText(), getDropDownText());
        if(success)
        {
            Toast.makeText(this, "Profile Update Successfully ~", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        else
        {
            Toast.makeText(this, "Update failed !",Toast.LENGTH_SHORT).show();
        }
    }
}