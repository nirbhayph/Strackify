package com.sportstracking.strackify.ui;

/**
 * strackify: splash activity
 * is the launcher activity.
 * moves to the intro activity if first time user
 * otherwise directly to the sign in activity for authentication
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sportstracking.strackify.authentication.SignInActivity;
import com.sportstracking.strackify.utility.Values;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // uses shared preferences to see if user has seen the intro before

        SharedPreferences userPreferences = getSharedPreferences(Values.SIGN_IN, MODE_PRIVATE);
        String visitedIntro = userPreferences.getString(Values.INTRO_VISITED, "NA");

        // checking if visited or not
        if (visitedIntro.equals("NA")) {
            SharedPreferences.Editor editor = userPreferences.edit();
            editor.putString(Values.INTRO_VISITED, "VISITED");
            editor.commit();

            // starts intro activity
            startActivity(new Intent(SplashActivity.this, IntroActivity.class));
        } else {
            // starts sign in activity
            startActivity(new Intent(SplashActivity.this, SignInActivity.class));
        }

        // closing splash activity
        finish();
    }
}
