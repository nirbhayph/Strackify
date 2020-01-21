package com.sportstracking.strackify.utility;

/**
 * strackify: volley service class
 * this service is used to request for data or for an image
 * uses the volley library as the name suggests
 * calls for the update of the requesting activity / fragment
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sportstracking.strackify.ui.CountrySelection;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.ui.TeamSelection;
import com.sportstracking.strackify.ui.pastevents.PastEventsViewModel;
import com.sportstracking.strackify.ui.upcomingevents.UpcomingEventsViewModel;

import org.json.JSONObject;

public class VolleyService {

    Activity activity; // activity calling
    String screenName; // calling screen
    Object reference; // reference to class
    Context context; // application context

    /**
     * parametrized constructor for activities calling
     *
     * @param activity   calling activity
     * @param screenName calling screen
     */
    public VolleyService(Activity activity, String screenName) {
        this.activity = activity;
        this.screenName = screenName;
        this.context = activity.getApplicationContext();
    }

    /**
     * parametrized constructor for fragment calling
     *
     * @param reference  calling class
     * @param screenName calling screen
     * @param context    application context
     */
    public VolleyService(Object reference, String screenName, Context context) {
        this.reference = reference;
        this.screenName = screenName;
        this.context = context;
    }

    /**
     * request for json object using volley library
     * singleton request queue used
     * calls update ui with response obtained
     *
     * @param url link to fetch data from
     */
    public void makeRequest(String url) {
        SingletonRequestQueue singletonRequestQueue = SingletonRequestQueue.getInstance(context);
        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        updateUI((JSONObject) response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });
        singletonRequestQueue.getRequestQueue().add(jsonObjectRequest);
    }

    /**
     * makes an image request using the volley library
     * singleton request queue used
     *
     * @param imageUrl  url to receive bitmap from
     * @param imageView imageview to set bitmap on
     */
    public void makeImageRequest(String imageUrl, final ImageView imageView) {
        SingletonRequestQueue singletonRequestQueue = SingletonRequestQueue.getInstance(context);
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                if (response != null) {
                    imageView.setImageBitmap(response);
                }

            }
        }, ViewGroup.LayoutParams.MATCH_PARENT + 1, ViewGroup.LayoutParams.MATCH_PARENT + 1, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, errorListener);

        singletonRequestQueue.getRequestQueue().add(imageRequest);
    }

    /**
     * makes an image request using the volley library
     * singleton request queue used
     *
     * @param imageUrl url to receive bitmap from
     * @param layout   layout to set background on with obtained bitmap
     */
    public void makeImageRequest(String imageUrl, final ConstraintLayout layout) {
        SingletonRequestQueue singletonRequestQueue = SingletonRequestQueue.getInstance(context);
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                if (response != null) {
                    BitmapDrawable background = new BitmapDrawable(context.getResources(), response);
                    layout.setBackground(background);
                }

            }
        }, ViewGroup.LayoutParams.MATCH_PARENT + 1, ViewGroup.LayoutParams.MATCH_PARENT + 1, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, errorListener);

        singletonRequestQueue.getRequestQueue().add(imageRequest);
    }

    /**
     * error listener, displays appropriate message on error
     */
    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(context, "No network coverage available!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Cannot retrieve data currently!", Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * calls the update function of the calling activity / fragment
     * with the response obtained from the url
     * cast the reference or activity variable using the screename accepted earlier.
     *
     * @param response JSONObject containing data obtained from the url accepted earlier.
     */
    public void updateUI(JSONObject response) {
        switch (screenName) {
            case Values.SPORTS_SELECTION: {
                SportSelection sportSelection = (SportSelection) activity;
                sportSelection.updateUI(response);
                break;
            }
            case Values.COUNTRIES_SELECTION: {
                CountrySelection countrySelection = (CountrySelection) activity;
                countrySelection.updateUI(response);
                break;
            }
            case Values.TEAMS_SELECTION: {
                TeamSelection teamSelection = (TeamSelection) activity;
                teamSelection.updateUI(response);
                break;
            }
            case Values.PAST_EVENTS_DISPLAY: {
                PastEventsViewModel pastEventsViewModel = (PastEventsViewModel) reference;
                pastEventsViewModel.updatePastEvents(response);
                break;
            }
            case Values.UPCOMING_EVENTS_DISPLAY: {
                UpcomingEventsViewModel upcomingEventsViewModel = (UpcomingEventsViewModel) reference;
                upcomingEventsViewModel.updateUpcomingEvents(response);
                break;
            }
        }
    }
}
