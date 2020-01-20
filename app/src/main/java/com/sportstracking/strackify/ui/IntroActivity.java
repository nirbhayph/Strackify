package com.sportstracking.strackify.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.authentication.SignInActivity;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPageA();
        addPageB();
        addPageC();
        addPageD();
        addPageE();

        setBarColor(ContextCompat.getColor(getApplicationContext(), R.color.backgroundDark));
        setSeparatorColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));

    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, SignInActivity.class));
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, SignInActivity.class));

    }

    private void addPageA(){
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Welcome to Strackify!");
        sliderPage.setDescription("Manage your favorite sports teams and their activities in no time.");
        sliderPage.setImageDrawable(R.drawable.a);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    private void addPageB(){
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Strackify has a wide spread of options to offer!");
        sliderPage.setDescription("Browse from a number of sports teams to choose from across the globe. Add or remove favorites at ease");
        sliderPage.setImageDrawable(R.drawable.b);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    private void addPageC(){
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Have a knack of using stories?");
        sliderPage.setDescription("Switch between sports like you switch stories! Yes .. its that easy!");
        sliderPage.setImageDrawable(R.drawable.c);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    private void addPageD(){
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("For developers out there!");
        sliderPage.setDescription("If you are a developer who wishes to learn and contribute go ahead and start contributing now to Strackify's GitHub respository!");
        sliderPage.setImageDrawable(R.drawable.d);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    private void addPageE(){
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Finally feedback!");
        sliderPage.setDescription("Visit the playstore to give feedback if you liked or disliked the app. All suggestions and responses are welcome. Thank you!");
        sliderPage.setImageDrawable(R.drawable.e);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }
}