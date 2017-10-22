package com.example.administrator.practice.utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by xieya on 2017/5/22.
 */

public class VolleyHelper {
    /**
     *  用于发送get请求
     * @param context
     * @param url
     * @param callBack
     */
    public static  void sendGetHttp(Context context,String url, final HttpResponseCallBack callBack){
        RequestQueue request= Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                callBack.success(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                callBack.error(volleyError);
            }
        });
        request.add(stringRequest);

    }
}
