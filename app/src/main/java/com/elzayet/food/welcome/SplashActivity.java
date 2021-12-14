package com.elzayet.food.welcome;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.elzayet.food.MainActivity;
import com.elzayet.food.R;

public class SplashActivity extends AppCompatActivity {
    private TextView a_s_w , a_s_appName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        a_s_w       = findViewById(R.id.a_s_w);
        a_s_appName = findViewById(R.id.a_s_appName);
    }

    @Override
    protected void onStart() {
        super.onStart();
        animateLogo();
    }



    private void startUp() {
        final ProgressDialog dialog;
        dialog = new ProgressDialog(SplashActivity.this);
        dialog.setMessage("Please Wait");
        dialog.setCancelable(false);
        dialog.show();
        SharedPreferences sharedPreferences = getSharedPreferences("onBoardingScreen",MODE_PRIVATE);
        boolean isFirstTime = sharedPreferences.getBoolean("firstTime",true);
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    if(isFirstTime){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("firstTime",false);
                        editor.apply();
                        Intent intent = new Intent(SplashActivity.this,OnBoardActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }else{
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    finish();
                } catch (InterruptedException e) { e.printStackTrace(); }
            }
        }.start();
    }

    private void animateLogo() {
        a_s_w.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.zoom_in_out));
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            a_s_appName.setVisibility(View.VISIBLE);
            a_s_appName.setAnimation(AnimationUtils.loadAnimation(SplashActivity.this,R.anim.zoom_out_in));
            new Handler(Looper.getMainLooper()).postDelayed(this::startUp, 1200);
        }, 1200);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { hideSystemUI(); }
        else { showSystemUI(); }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE  | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    // Shows the system bars by removing all the flags
    // except for the ones that make the content appear under the system bars.
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}