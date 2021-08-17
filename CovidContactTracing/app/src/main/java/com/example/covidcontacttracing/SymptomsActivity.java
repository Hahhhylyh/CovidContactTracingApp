package com.example.covidcontacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.awt.font.TextAttribute;

public class SymptomsActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextView radioTitle;
    private TableLayout most, less, serious;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Symptoms");

        most = findViewById(R.id.content_most);
        less = findViewById(R.id.content_less);
        serious = findViewById(R.id.content_serious);

        radioTitle = findViewById(R.id.radio_title);
        radioGroup = findViewById(R.id.radio_group);

        // dynamically show the related info by looking at the radio text
        // by showing the related info and hiding the unrelated info
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = findViewById(checkedId);
                switch (radioButton.getText().toString()) {
                    case "Most":
                        radioTitle.setText("Most Common Symptoms:");
                        most.setVisibility(View.VISIBLE);
                        less.setVisibility(View.GONE);
                        serious.setVisibility(View.GONE);
                        break;

                    case "Less":
                        radioTitle.setText("Less Common Symptoms:");
                        most.setVisibility(View.GONE);
                        less.setVisibility(View.VISIBLE);
                        serious.setVisibility(View.GONE);
                        break;

                    case "Serious":
                        radioTitle.setText("Serious Symptoms:");
                        most.setVisibility(View.GONE);
                        less.setVisibility(View.GONE);
                        serious.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        // By default, set "most" as checked
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
    }
}