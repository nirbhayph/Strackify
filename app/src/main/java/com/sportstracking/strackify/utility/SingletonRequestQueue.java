package com.sportstracking.strackify.utility;

/**
 * strackify: singleton request queue
 * gives out a singleton reference.
 * acts like a utility to the volley service class
 *
 * @author Nirbhay Ashok Pherwani
 * email: np5318@rit.edu
 * profile: https://nirbhay.me
 */

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class SingletonRequestQueue {

    private static SingletonRequestQueue mInstance; // single instance
    private Context mContext;
    private RequestQueue mRequestQueue; // volley's request queue reference

    /**
     * constructor for settings the request queue
     * @param context application context
     */
    private SingletonRequestQueue(Context context) {
        mContext = context;
        mRequestQueue = getRequestQueue();
    }

    /**
     * If present returns it, otherwise creates a new one returns it
     * @param context application context
     * @return singleton request queue instance
     */
    public static synchronized SingletonRequestQueue getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SingletonRequestQueue(context);
        }
        return mInstance;
    }

    /**
     * If present returns it, otherwise sets a new one and returns that
     * @return volley's request queue instance
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext);
        }
        return mRequestQueue;
    }
}
