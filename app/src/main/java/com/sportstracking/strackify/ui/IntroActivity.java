package com.sportstracking.strackify.ui;

/**
 * strackify: intro screen
 * informs the user about the app
 * introduces to the different features to expect
 * uses the app intro library for the fragments
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.content.Intent;
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

    /**
     * on pressing skip move to sign in activity
     *
     * @param currentFragment fragment from where skip was called
     */
    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, SignInActivity.class));
    }

    /**
     * on pressing done move to sign in activity
     *
     * @param currentFragment fragment where done is pressed
     */
    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        startActivity(new Intent(IntroActivity.this, SignInActivity.class));

    }

    /**
     * adds the first welcome fragment
     */
    private void addPageA() {
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Welcome to Strackify!");
        sliderPage.setDescription("Manage your favorite sports teams and their activities in no time.");
        sliderPage.setImageDrawable(R.drawable.a);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    /**
     * adds the second fragment
     * informs user about various sports teams
     */
    private void addPageB() {
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Strackify has a wide spread of options to offer!");
        sliderPage.setDescription("Browse from a number of sports teams to choose from across the globe. Add or remove favorites at ease");
        sliderPage.setImageDrawable(R.drawable.b);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    /**
     * adds the third fragment
     * informs user about switching between teams
     */
    private void addPageC() {
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Have a knack of using stories?");
        sliderPage.setDescription("Switch between sports teams like you switch stories! Yes .. its that easy!");
        sliderPage.setImageDrawable(R.drawable.c);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    /**
     * adds the fourth fragment
     * information for developers trying the app
     */
    private void addPageD() {
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("For developers out there!");
        sliderPage.setDescription("If you are a developer who wishes to learn and contribute go ahead and start contributing now to Strackify's GitHub respository!");
        sliderPage.setImageDrawable(R.drawable.d);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }

    /**
     * adds the fifth fragment
     * asks users to provide feedback
     */
    private void addPageE() {
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Finally feedback!");
        sliderPage.setDescription("Visit the playstore to give feedback if you liked or disliked the app. All suggestions and responses are welcome. Thank you!");
        sliderPage.setImageDrawable(R.drawable.e);
        sliderPage.setBgColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));
    }
}