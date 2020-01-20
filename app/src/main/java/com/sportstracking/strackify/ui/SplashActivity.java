package com.sportstracking.strackify.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sportstracking.strackify.authentication.SignInActivity;
import com.sportstracking.strackify.utility.Constants;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // starting main activity
        SharedPreferences userPreferences = getSharedPreferences(Constants.SIGN_IN, MODE_PRIVATE);
        String visitedIntro = userPreferences.getString(Constants.INTRO_VISITED, "NA");
        if(visitedIntro.equals("NA")){
            SharedPreferences.Editor editor = userPreferences.edit();

            editor.putString(Constants.INTRO_VISITED,"VISITED");

            editor.commit();

            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        }
        else{
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        }



        // closing splash activity
        finish();
    }
}
