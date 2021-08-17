package com.example.covidcontacttracing;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 4000;

    private ImageView ivTop, ivLogo, ivGif, ivBot;
    private TextView tvLogo;
    private Animation anim1, anim2;
    private CharSequence charSequence;
    private int index;
    private long delay = 200;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ivTop = findViewById(R.id.iv_top);
        ivLogo = findViewById(R.id.iv_logo);
        ivGif = findViewById(R.id.iv_gif);
        ivBot = findViewById(R.id.iv_bot);
        tvLogo = findViewById(R.id.tv_logo);

        //link animation file to java
        anim1 = AnimationUtils.loadAnimation(this, R.anim.top_wave);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.bot_wave);

        ivTop.setAnimation(anim1);
        ivBot.setAnimation(anim2);

        //Initialize object animator
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(
                ivLogo,
                PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        );

        //Set duration, repeat count, repeat mode - to let the logo inflate and shrink continuously
        objectAnimator.setDuration(500);
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        objectAnimator.start();

        //Show char one by one
        animatedText("NoCovid");

        //Set the loading gif
        Glide.with(this).load(R.drawable.loading).into(ivGif);

        //Handler to run splash screen then direct to next activity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT); // 4 seconds
    }

    //Slowly add the char one by one
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //When runnable is running, set text
            tvLogo.setText(charSequence.subSequence(0, index++));
            //check condition
            if(index <= charSequence.length()) {
                //When index is equal to text length => run handler
                handler.postDelayed(runnable, delay);
            }
        }
    };

    //Used once to activated the runnable
    public void animatedText(CharSequence cs) {
        charSequence = cs;
        index = 0;
        tvLogo.setText("");
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, delay);
    }
}