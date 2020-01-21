package com.sportstracking.strackify.authentication;

/**
 * strackify: sign in activity
 * here google's firebase authentication has been used to authenticate the user
 * users can switch between multiple profiles easily.
 * also, unsplash has been used for the random background
 * shared preferences are used to manage different profile's data
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sportstracking.strackify.R;
import com.sportstracking.strackify.ui.Home;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.utility.Values;
import com.sportstracking.strackify.utility.VolleyService;
import static com.sportstracking.strackify.utility.Values.FAV_CHECKER;
import static com.sportstracking.strackify.utility.Values.LATEST_FAV_TEAM;
import static com.sportstracking.strackify.utility.Values.SIGN_OUT;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;

    /**
     * called at the beginning
     * initializes the google sign in client
     * checks if there is a logout request from inside the app
     * on pressing sign in verifies the user identity and moves on.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        changeBackground();

        Button signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        if (getIntent().hasExtra(SIGN_OUT)) {
            this.signOut();
        }
    }

    /**
     * updates ui with current user state on start
     * changes the background obtained from unsplash
     */
    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        changeBackground();
        updateUI(currentUser);
    }

    /**
     * changes the background obtained from unsplash
     */
    @Override
    protected void onResume() {
        super.onResume();
        changeBackground();
    }

    /**
     * updates the ui according to whether user was signed in successfully or not
     * @param requestCode request code for sign in
     * @param resultCode result code for sign in
     * @param data data from intent
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                updateUI(null);
            }
        }
    }

    /**
     * for firebase authentication of the user
     * updates ui accordingly
     * @param acct credentials and other info of the account
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }

    /**
     * sign in intent created displaying all available accounts
     */
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * called to log user out of the system (firebase and google both)
     */
    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    /**
     * updates the UI and takes the user according to his state to the next screen
     * If not authernticated correctly, a message is displayed to the user.
     * @param user
     */
    private void updateUI(FirebaseUser user) {

        //check for non-null user
        if (user != null) {

            // updates values used to differentiate users based on
            // uid for retrieving data from shared preferences
            Values.LATEST_FAV_TEAM = "LATEST_FAV_TEAM" + user.getUid();
            Values.LATEST_FAV_TEAM_NAME = "LATEST_FAV_TEAM_NAME" + user.getUid();
            Values.FAV_TEAMS = "FAV_TEAMS" + user.getUid();
            Values.FAV_CHECKER = "FAV_TEAM" + user.getUid();
            Values.DEFAULT = "DEFAULT" + user.getUid();

            // for stored information retrieval
            SharedPreferences preferences = getSharedPreferences(LATEST_FAV_TEAM, MODE_PRIVATE);

            // to see if there is at least one favorite team selected by user
            String latestFavTeam = preferences.getString(LATEST_FAV_TEAM, FAV_CHECKER);

            // for profile details
            SharedPreferences userPreferences = getSharedPreferences(Values.SIGN_IN, MODE_PRIVATE);
            SharedPreferences.Editor editor = userPreferences.edit();

            // stores in basic profile information
            editor.putString(Values.SIGN_IN_EMAIL, user.getEmail().toString());
            editor.putString(Values.SIGN_IN_NAME, user.getDisplayName().toString());
            editor.putString(Values.SIGN_IN_PROFILE_IMAGE, user.getPhotoUrl().toString());

            editor.commit();

            Intent intent;

            // if present takes user to home activity otherwise asks user to choose a favorite
            if (latestFavTeam.equals(FAV_CHECKER)) {
                intent = new Intent(SignInActivity.this, SportSelection.class);
            } else {
                intent = new Intent(SignInActivity.this, Home.class);
            }
            startActivity(intent);
        } else {
            // message for user if failed to authenticate
            Toast.makeText(getApplicationContext(), "Unable to sign you in currently!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * changed the background of the sign in screen
     * uses the volley service and unsplash to get the random background
     */
    private void changeBackground() {
        ConstraintLayout signInLayout = findViewById(R.id.signInLayout);
        VolleyService volleyService = new VolleyService(this, Values.SIGN_IN, getApplicationContext());
        volleyService.makeImageRequest("https://source.unsplash.com/900x1600/?basketball", signInLayout);
    }
}


