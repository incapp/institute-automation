package com.incapp.instituteautomation.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.incapp.instituteautomation.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(SplashActivity.this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Intent intent = new Intent(
                            SplashActivity.this,
                            LoginActivity.class
                    );
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(
                            SplashActivity.this,
                            MainActivity.class
                    );
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}
