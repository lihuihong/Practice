package com.example.administrator.practice.utils;

import android.content.Context;

/**
 * Created by Administrator on 2017/5/25.
 * 网络缓存
 */

public class CacheUtlis {

    public static void setCache(String url, String json, Context ctx){
        PrefUtils.setString(ctx,url,json);

    }
    /**
     * 获取缓存
     */
    public static String getCache (String url,Context ctx){
        return PrefUtils.getString(ctx,url,null);
    }
}
