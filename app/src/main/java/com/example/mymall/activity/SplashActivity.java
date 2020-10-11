package com.example.mymall.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;

import com.example.mymall.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SystemClock.sleep(3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth=FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            Intent RegisterIntent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(RegisterIntent);
            finish();
        } else {
            Intent MainIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(MainIntent);
            finish();
        }
    }
}
