package com.sportstracking.strackify.utility;

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

    Activity activity;
    String screenName;
    Object reference;
    Context context;

    public VolleyService(Activity activity, String screenName){
        this.activity = activity;
        this.screenName= screenName;
        this.context = activity.getApplicationContext();
    }

    public VolleyService(Object reference, String screenName, Context context){
        this.reference = reference;
        this.screenName= screenName;
        this.context = context;
    }

    public void makeRequest(String url){
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
                    public void onErrorResponse(VolleyError error)
                    {
                        error.printStackTrace();
                    }
                });
        singletonRequestQueue.getRequestQueue().add(jsonObjectRequest);
    }

    public void makeImageRequest(String imageUrl, final ImageView imageView) {
        SingletonRequestQueue singletonRequestQueue = SingletonRequestQueue.getInstance(context);
        ImageRequest imageRequest = new ImageRequest(imageUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {

                if (response != null) {
                    imageView.setImageBitmap(response);
                }

            }
        }, ViewGroup.LayoutParams.MATCH_PARENT+1, ViewGroup.LayoutParams.MATCH_PARENT+1, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, errorListener);

        singletonRequestQueue.getRequestQueue().add(imageRequest);
    }

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
        }, ViewGroup.LayoutParams.MATCH_PARENT+1, ViewGroup.LayoutParams.MATCH_PARENT+1, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.ARGB_8888, errorListener);

        singletonRequestQueue.getRequestQueue().add(imageRequest);
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(context, "No Network Coverage Available!", Toast.LENGTH_LONG).show();
            } /*else {
                Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
            }*/
        }
    };

    public void updateUI(JSONObject response){
        switch(screenName){
            case Constants.SPORTS_SELECTION: {
                SportSelection sportSelection = (SportSelection) activity;
                sportSelection.updateUI(response);
                break;
            }
            case Constants.COUNTRIES_SELECTION: {
                CountrySelection countrySelection = (CountrySelection) activity;
                countrySelection.updateUI(response);
                break;
            }
            case Constants.TEAMS_SELECTION: {
                TeamSelection teamSelection = (TeamSelection) activity;
                teamSelection.updateUI(response);
                break;
            }
            case Constants.PAST_EVENTS_DISPLAY: {
                PastEventsViewModel pastEventsViewModel = (PastEventsViewModel) reference;
                pastEventsViewModel.updatePastEvents(response);
                break;
            }
            case Constants.UPCOMING_EVENTS_DISPLAY: {
                UpcomingEventsViewModel upcomingEventsViewModel = (UpcomingEventsViewModel) reference;
                upcomingEventsViewModel.updateUpcomingEvents(response);
                break;
            }
        }
    }
}
