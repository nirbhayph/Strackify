package com.sportstracking.strackify.utility;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sportstracking.strackify.ui.CountrySelection;
import com.sportstracking.strackify.ui.SportSelection;
import com.sportstracking.strackify.ui.TeamSelection;

import org.json.JSONObject;

public class VolleyService {

    Activity activity;
    String screenName;

    public VolleyService(Activity activity, String screenName){
        this.activity = activity;
        this.screenName= screenName;
    }

    public void makeRequest(String url){
        SingletonRequestQueue singletonRequestQueue = SingletonRequestQueue.getInstance(activity.getApplicationContext());
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
        SingletonRequestQueue singletonRequestQueue = SingletonRequestQueue.getInstance(activity.getApplicationContext());
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

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(activity.getApplicationContext(), "No Network Coverage Available!", Toast.LENGTH_LONG).show();
            } /*else {
                Toast.makeText(activity.getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
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
        }
    }
}
