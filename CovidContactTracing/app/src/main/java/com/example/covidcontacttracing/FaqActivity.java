package com.example.covidcontacttracing;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.covidcontacttracing.Adapters.FaqAdapter;
import com.example.covidcontacttracing.Models.Faq;

import java.util.ArrayList;
import java.util.List;

public class FaqActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Faq> faqList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("FAQ");

        recyclerView = findViewById(R.id.rcv_faq);
        initializeData();
        setRecyclerView();
    }

    private void setRecyclerView() {
        FaqAdapter faqAdapter = new FaqAdapter(faqList, getApplicationContext());
        recyclerView.setAdapter(faqAdapter);
        recyclerView.setHasFixedSize(false);
    }

    // add data to faqlist from string.xml
    private void initializeData() {
        faqList = new ArrayList<>();

        faqList.add(new Faq(getString(R.string.faq_q1), getString(R.string.faq_a1)));
        faqList.add(new Faq(getString(R.string.faq_q2), getString(R.string.faq_a2)));
        faqList.add(new Faq(getString(R.string.faq_q3), getString(R.string.faq_a3)));
        faqList.add(new Faq(getString(R.string.faq_q4), getString(R.string.faq_a4)));
        faqList.add(new Faq(getString(R.string.faq_q5), getString(R.string.faq_a5)));
        faqList.add(new Faq(getString(R.string.faq_q6), getString(R.string.faq_a6)));
        faqList.add(new Faq(getString(R.string.faq_q7), getString(R.string.faq_a7)));
    }
}