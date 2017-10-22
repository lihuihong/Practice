package com.example.administrator.practice.utils;

import com.android.volley.VolleyError;

/**
 * Created by xieya on 2017/5/22.
 */

public interface HttpResponseCallBack {
    void success(String response);
    void error(VolleyError error);
}
