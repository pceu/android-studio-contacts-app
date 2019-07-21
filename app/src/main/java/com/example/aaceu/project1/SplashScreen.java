package com.example.aaceu.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    /*
        How to create splash screen is learned from
        https://medium.com/viithiisys/android-perfect-way-to-create-splash-screen-ca3c5bee137f
        However, I do not use the same code as this resource as it would not fit for my project
        so i use different method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Intent intent = new Intent(getApplicationContext(), login_page.class);
        startActivity(intent);
        finish();
    }
}
