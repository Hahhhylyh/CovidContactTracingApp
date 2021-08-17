package com.example.covidcontacttracing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class PreventionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prevention);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        TextView title = getSupportActionBar().getCustomView().findViewById(R.id.tv_ab_title);
        title.setText("Prevention Tips");

        // Make parts of a TextView clickable (1) --> snackbar ask --> link to WashHandActivity
        TextView textView = findViewById(R.id.clickable_text_wash);
        String text = getResources().getString(R.string.prevent_t2_1);
        SpannableString ss = new SpannableString(text);

        RelativeLayout relativeLayout = findViewById(R.id.relative_layout);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // create snackbar when user click on the red color enlarged text
                Snackbar.make(relativeLayout, "Looking for Hand Washing Tips?", Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setAction("YES", v -> {
                            Intent intent = new Intent(PreventionActivity.this, WashHandActivity.class);
                            startActivity(intent);
                        })
                        .setActionTextColor(getResources().getColor(R.color.my_red))
                        .show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.my_red));
                ds.setFakeBoldText(true);
            }
        };

        // set the range of clickable text (from 0 to 5 char) and enlarge the size
        ss.setSpan(new RelativeSizeSpan(1.6f), 0,5, 0);
        ss.setSpan(clickableSpan, 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());

        // Make parts of a TextView clickable (2) --> snackbar ask --> link to WearMaskActivity
        TextView tvMask = findViewById(R.id.clickable_text_mask);
        String textMask = getResources().getString(R.string.prevent_t2_3);
        SpannableString ssMask = new SpannableString(textMask);

        ClickableSpan clickableSpan_Mask = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                // create snackbar when user click on the red color enlarged text
                Snackbar.make(relativeLayout, "Looking for Mask Wearing Tips?", Snackbar.LENGTH_LONG)
                        .setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE)
                        .setAction("YES", v -> {
                            Intent intent = new Intent(PreventionActivity.this, MaskWearingActivity.class);
                            startActivity(intent);
                        })
                        .setActionTextColor(getResources().getColor(R.color.my_red))
                        .show();
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(ContextCompat.getColor(getApplicationContext(), R.color.my_red));
                ds.setFakeBoldText(true);
            }
        };

        // set the position of the clickable text (from 7 to 11 char) and size
        ssMask.setSpan(new RelativeSizeSpan(1.6f), 7,11, 0);
        ssMask.setSpan(clickableSpan_Mask, 7, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMask.setText(ssMask);
        tvMask.setMovementMethod(LinkMovementMethod.getInstance());
    }
}