package com.sportstracking.strackify.utility;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

public class VolleyService {

    Context context;

    public VolleyService(Context context){
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
                       Log.d("Response from API", response.toString());
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
}
