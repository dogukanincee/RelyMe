package com.example.RelyMe;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    private TextView appName;   //App Name TextView
    private ImageView appLogo;  //App Logo ImageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        appName = findViewById(R.id.appName);
        appLogo = findViewById(R.id.appLogo);

        //Creates first animation
        final Animation splashAnimation1 = AnimationUtils.loadAnimation(this, R.anim.alpha);

        Thread timer = new Thread() { //Creates a thread to start activity after 4 seconds
            public void run() {
                try {
                    //Starts animation
                    appName.startAnimation(splashAnimation1);
                    appLogo.startAnimation(splashAnimation1);
                    sleep(2000); //Waits 2 seconds
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //Starts Login Activity
                    Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(loginActivity);
                    finish();
                }
            }
        };
        timer.start(); //Starts thread
    }
}