package com.sportstracking.strackify.ui.settings;

/**
 * strackify: settings fragment
 * displays user's profile information
 * gives the user a number of options to switch from
 * manage favorites (add/subtract)
 * if a developer can contribute to the project
 * share the app
 * provide feedback on the play store
 * logout from the app
 * acknowledgements and mentions
 * app walkthrough
 * developer information
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ShareCompat;
import androidx.fragment.app.Fragment;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.authentication.SignInActivity;
import com.sportstracking.strackify.ui.IntroActivity;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsFragment extends Fragment {

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_settings, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("  Settings");

        setupUI();

        return root;
    }

    /**
     * sets up the various options
     * for the user to choose from
     */
    private void setupUI() {
        setupProfileDetails();
        setupFavoriteOptions();
        setupAboutDeveloper();
        setupPlayStoreRedirect();
        setupContributions();
        setupAcknowledgments();
        setupSharing();
        setupShowHelper();
        setupLogout();
    }

    /**
     * sets the user's profile details (name, email and photograph)
     */
    private void setupProfileDetails() {
        TextView profileName = root.findViewById(R.id.profileName);
        TextView profileEmail = root.findViewById(R.id.profileEmail);
        CircleImageView profileThumb = root.findViewById(R.id.profileThumb);

        VolleyService volleyService = new VolleyService(this, Values.SETTINGS_DISPLAY, getActivity().getApplicationContext());
        SharedPreferences userPreferences = getActivity().getSharedPreferences(Values.SIGN_IN, Context.MODE_PRIVATE);
        String imageUrl = userPreferences.getString(Values.SIGN_IN_PROFILE_IMAGE, "NA");
        if (!imageUrl.equals("NA")) {
            volleyService.makeImageRequest(imageUrl, profileThumb);
        } else {
            volleyService.makeImageRequest("https://images.unsplash.com/photo-1476169785137-3bfe32e30ee1?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1350&q=80", profileThumb);
        }

        profileName.setText(userPreferences.getString(Values.SIGN_IN_NAME, ""));
        profileEmail.setText(userPreferences.getString(Values.SIGN_IN_EMAIL, ""));
    }

    /**
     * user can manage their favorites from here (add/remove)
     */
    private void setupFavoriteOptions() {
        TextView manageFavs = root.findViewById(R.id.settingsManageFavs);

        manageFavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SportSelection.class);
                startActivity(intent);
            }
        });
    }

    /**
     * redirects user to developer's information page
     */
    private void setupAboutDeveloper() {
        TextView aboutView = root.findViewById(R.id.aboutDeveloper);
        aboutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nirbhay.me"));
                startActivity(browserIntent);
            }
        });
    }

    /**
     * redirects user to the app's playstore page
     */
    private void setupPlayStoreRedirect() {
        TextView playStore = root.findViewById(R.id.viewOnPlayStore);
        final String url = "https://play.google.com/store/apps/details?id=com.sportstracking.strackify";
        playStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(browserIntent);
                } catch(Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), "App in publishing state!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * redirects user to the github page for the project's repository
     */
    private void setupContributions() {
        TextView contributions = root.findViewById(R.id.contributions);
        contributions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nirbhayph/strackify"));
                startActivity(browserIntent);
            }
        });
    }

    /**
     * redirects user to the acknowledgements and mentions page
     */
    private void setupAcknowledgments() {
        TextView acks = root.findViewById(R.id.ackMentions);
        acks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/nirbhayph/Strackify/blob/master/README.md"));
                startActivity(browserIntent);
            }
        });
    }

    /**
     * allows the user to share the app with others
     */
    private void setupSharing() {
        TextView share = root.findViewById(R.id.shareApp);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setChooserTitle("Strackify")
                        .setSubject("Share Now!")
                        .setText("Visit the play store to download now! https://play.google.com/store/apps/details?id=com.sportstracking.strackify")
                        .startChooser();
            }
        });
    }

    /**
     * logout from the app
     */
    private void setupLogout() {
        TextView logout = root.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SignInActivity.class);
                intent.putExtra(Values.SIGN_OUT, Values.SIGN_OUT);
                startActivity(intent);
                getActivity().finishAffinity();
            }
        });
    }

    /**
     * to view the walkthrough of the app
     */
    private void setupShowHelper() {
        TextView showHelper = root.findViewById(R.id.showHelper);

        showHelper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IntroActivity.class);
                startActivity(intent);
            }
        });
    }


}