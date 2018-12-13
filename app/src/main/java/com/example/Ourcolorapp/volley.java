package com.example.Ourcolorapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class volley {
    private static volley mInstance;
    private RequestQueue requestQueue;
    private static Context mCtx;

    private volley (Context context) {
        mCtx = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized volley getmInstance(Context context) {
        if (mInstance == null) {
            mInstance = new volley(context);
        }
        return mInstance;
    }

    public<T> void addToRequestque(Request<T> request) {
        requestQueue.add(request);
    }
}
