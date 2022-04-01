package com.example.audiorecorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {

    LottieAnimationView lottieAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final int delay = 3500;
        lottieAnim = findViewById(R.id.anim);
        lottieAnim.animate().setDuration(2500).setStartDelay(500);

        new Handler().postDelayed(() ->
                        startActivity(new Intent
                                (SplashActivity.this, MainActivity.class)
                        )
                , delay);
    }
}