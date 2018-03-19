package com.example.android.djangotry;

import android.annotation.SuppressLint;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by vinay on 19-03-2018.
 */

class SingletoneClass {
    private RequestQueue requestQueue;
    @SuppressLint("StaticFieldLeak")
    private static Context mtx;
    @SuppressLint("StaticFieldLeak")
    private static SingletoneClass instance;

    private SingletoneClass(Context ctx){
        mtx=ctx;
        requestQueue=getRequestQueue();
    }
    private RequestQueue getRequestQueue(){
        if(requestQueue==null){
            return Volley.newRequestQueue(mtx.getApplicationContext());
        }
        return requestQueue;
    }
    static synchronized SingletoneClass getInstance(Context context){
        if (instance==null){
            instance=new SingletoneClass(context);
        }
        return instance;
    }
    <T> void addToRequestQue(Request<T> request){
        requestQueue.add(request);
    }
}
