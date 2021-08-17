package com.example.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.NumberFormat;

import static com.example.covidcontacttracing.Constants.COUNTRY_ACTIVE;
import static com.example.covidcontacttracing.Constants.COUNTRY_CONFIRMED;
import static com.example.covidcontacttracing.Constants.COUNTRY_DEATH;
import static com.example.covidcontacttracing.Constants.COUNTRY_FLAGURL;
import static com.example.covidcontacttracing.Constants.COUNTRY_NAME;
import static com.example.covidcontacttracing.Constants.COUNTRY_NEW_CONFIRMED;
import static com.example.covidcontacttracing.Constants.COUNTRY_NEW_DEATH;
import static com.example.covidcontacttracing.Constants.COUNTRY_PER_CASE;
import static com.example.covidcontacttracing.Constants.COUNTRY_PER_DEATH;
import static com.example.covidcontacttracing.Constants.COUNTRY_PER_TEST;
import static com.example.covidcontacttracing.Constants.COUNTRY_POPULATION;
import static com.example.covidcontacttracing.Constants.COUNTRY_RECOVERED;
import static com.example.covidcontacttracing.Constants.COUNTRY_TESTS;

public class CountryActivity extends AppCompatActivity {

    private TextView tv_country_name, tv_population, tv_confirmed, tv_confirmed_new, tv_active, tv_death, tv_death_new,
            tv_recovered, tv_tests, tv_one_case_per, tv_one_death_per, tv_one_test_per;

    private String str_countryName, str_confirmed, str_confirmed_new, str_active, str_death, str_death_new,
            str_recovered, str_tests, str_one_case_per, str_one_death_per, str_one_test_per, str_population, str_flag;

    private ImageView iv_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Country Stats");

        initialize();

        readIntent();

        retrieveCountryData();
    }

    // initialize (link) all the textview and imageview used
    private void initialize() {
        tv_country_name = findViewById(R.id.tv_country_name);
        iv_flag = findViewById(R.id.iv_country_flag);
        tv_population = findViewById(R.id.tv_population);
        tv_confirmed = findViewById(R.id.tv_confirmed_cases);
        tv_confirmed_new = findViewById(R.id.tv_new_confirmed_cases);
        tv_death = findViewById(R.id.tv_death);
        tv_death_new = findViewById(R.id.tv_new_death);
        tv_active = findViewById(R.id.tv_active);
        tv_recovered = findViewById(R.id.tv_country_recovered);
        tv_tests = findViewById(R.id.tv_test);
        tv_one_case_per = findViewById(R.id.tv_per_case);
        tv_one_death_per = findViewById(R.id.tv_per_death);
        tv_one_test_per = findViewById(R.id.tv_per_test);
    }

    // store the intent passed from CountryAdapter (the country clicked) into string variables
    private void readIntent() {
        Intent intent = getIntent();
        str_countryName = intent.getStringExtra(COUNTRY_NAME);
        str_confirmed = intent.getStringExtra(COUNTRY_CONFIRMED);
        str_active = intent.getStringExtra(COUNTRY_ACTIVE);
        str_death = intent.getStringExtra(COUNTRY_DEATH);
        str_recovered = intent.getStringExtra(COUNTRY_RECOVERED);
        str_confirmed_new = intent.getStringExtra(COUNTRY_NEW_CONFIRMED);
        str_death_new = intent.getStringExtra(COUNTRY_NEW_DEATH);
        str_tests = intent.getStringExtra(COUNTRY_TESTS);
        str_population = intent.getStringExtra(COUNTRY_POPULATION);
        str_one_case_per = intent.getStringExtra(COUNTRY_PER_CASE);
        str_one_death_per = intent.getStringExtra(COUNTRY_PER_DEATH);
        str_one_test_per = intent.getStringExtra(COUNTRY_PER_TEST);
        str_flag = intent.getStringExtra(COUNTRY_FLAGURL);
    }

    // set the text of the textview and imageview according to the stored variables
    private void retrieveCountryData() {
        Glide.with(this).load(str_flag).diskCacheStrategy(DiskCacheStrategy.DATA).into(iv_flag);
        tv_country_name.setText(str_countryName);
        tv_population.setText(NumberFormat.getInstance().format(Integer.parseInt(str_population)));
        tv_confirmed.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed)));
        tv_confirmed_new.setText(NumberFormat.getInstance().format(Integer.parseInt(str_confirmed_new)));
        tv_death.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death)));
        tv_death_new.setText(NumberFormat.getInstance().format(Integer.parseInt(str_death_new)));
        tv_active.setText(NumberFormat.getInstance().format(Integer.parseInt(str_active)));
        tv_recovered.setText(NumberFormat.getInstance().format(Integer.parseInt(str_recovered)));
        tv_tests.setText(NumberFormat.getInstance().format(Integer.parseInt(str_tests)));
        tv_one_case_per.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_case_per)));
        tv_one_death_per.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_death_per)));
        tv_one_test_per.setText(NumberFormat.getInstance().format(Integer.parseInt(str_one_test_per)));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}