package com.example.dell.booksearchapp;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.net.ConnectException;

/**
 * Created by dell on 1/30/2019.
 */

public class VolleySingleton {

    private static VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    private static Context mContext;


    private VolleySingleton(Context context)
    {
       mContext = context;
       requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue()
    {
        if (requestQueue == null)
        {
            requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized VolleySingleton getInstance(Context context)
    {
       if (volleySingleton == null)
       {
           volleySingleton = new VolleySingleton(context);
       }
       return volleySingleton;
    }

    public<T> void addRequest(Request<T> request)
    {
        requestQueue.add(request);
    }
}
