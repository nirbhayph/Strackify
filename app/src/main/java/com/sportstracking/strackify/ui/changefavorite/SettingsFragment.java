package com.sportstracking.strackify.ui.changefavorite;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.sportstracking.strackify.R;
import com.sportstracking.strackify.model.PastEvent;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.ui.TeamSelection;
import com.sportstracking.strackify.utility.Constants;
import com.sportstracking.strackify.utility.VolleyService;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("  Settings");

        setupUI();

        return root;
    }

    private void setupUI(){
        setupProfileDetails();
        setupFavoriteOptions();
        setupAboutDeveloper();
        setupPlayStoreRedirect();
        setupContributions();
        setupAcknowledgments();
        setupSharing();
    }

    private void setupProfileDetails(){
        TextView profileName = root.findViewById(R.id.profileName);
        TextView profileEmail = root.findViewById(R.id.profileEmail);
        CircleImageView profileThumb = root.findViewById(R.id.profileThumb);

        VolleyService volleyService = new VolleyService(this, Constants.SETTINGS_DISPLAY, getActivity().getApplicationContext());
        SharedPreferences userPreferences = getActivity().getSharedPreferences(Constants.SIGN_IN, Context.MODE_PRIVATE);
        String imageUrl = userPreferences.getString(Constants.SIGN_IN_PROFILE_IMAGE, "NA");
        if(!imageUrl.equals("NA")){
            volleyService.makeImageRequest(imageUrl, profileThumb);
        }
        else{
            volleyService.makeImageRequest("https://images.unsplash.com/photo-1476169785137-3bfe32e30ee1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80", profileThumb);
        }

        profileName.setText(userPreferences.getString(Constants.SIGN_IN_NAME, ""));
        profileEmail.setText(userPreferences.getString(Constants.SIGN_IN_EMAIL, ""));
    }

    private void setupFavoriteOptions(){
        TextView manageFavs = root.findViewById(R.id.settingsManageFavs);

        manageFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SportSelection.class);
                startActivity(intent);
            }
        });
    }

    private void setupAboutDeveloper(){
        TextView aboutView = root.findViewById(R.id.aboutDeveloper);
        aboutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nirbhay.me"));
                startActivity(browserIntent);
            }
        });
    }

    private void setupPlayStoreRedirect(){
        TextView playStore = root.findViewById(R.id.viewOnPlayStore);
        playStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/"));
                startActivity(browserIntent);
            }
        });
    }

    private void setupContributions(){
        TextView contributions = root.findViewById(R.id.contributions);
        contributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nirbhayph/strackify"));
                startActivity(browserIntent);
            }
        });
    }

    private void setupAcknowledgments(){
        TextView acks = root.findViewById(R.id.ackMentions);
        acks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nirbhayph/Strackify/blob/master/README.md"));
                startActivity(browserIntent);
            }
        });
    }

    private void setupSharing(){
        TextView share = root.findViewById(R.id.shareApp);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setChooserTitle("Strackify")
                        .setSubject("Share Now!")
                        .setText("https://google.com\nPlease visit the play store link to download the Strackify app now!")
                        .startChooser();
            }
        });
    }
}