package com.example.rideshareneon;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DURATION = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        LottieAnimationView animationView = findViewById(R.id.animationView);
        animationView.setAnimation(R.raw.neon_loading);
        animationView.playAnimation();

        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
        }, SPLASH_DURATION);
    }
}