package com.sportstracking.strackify.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sportstracking.strackify.authentication.SignInActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // starting main activity
        startActivity(new Intent(SplashActivity.this, SignInActivity.class));

        // closing splash activity
        finish();
    }
}
