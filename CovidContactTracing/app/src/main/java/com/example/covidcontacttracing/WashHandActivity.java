package com.example.covidcontacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class WashHandActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wash_hand);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Handwashing");
    }

    // show toast message
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    public void showWashHandStep1(View view) {
        displayToast(getString(R.string.handwashing_t2_1));
    }

    public void showWashHandStep2(View view) {
        displayToast(getString(R.string.handwashing_t2_2));
    }

    public void showWashHandStep3(View view) {
        displayToast(getString(R.string.handwashing_t2_3));
    }

    public void showWashHandStep4(View view) {
        displayToast(getString(R.string.handwashing_t2_4));
    }

    public void showWashHandStep5(View view) {
        displayToast(getString(R.string.handwashing_t2_5));
    }

    public void showWashHandStep6(View view) {
        displayToast(getString(R.string.handwashing_t2_6));
    }
}