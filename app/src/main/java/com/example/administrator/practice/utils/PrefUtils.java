package com.example.administrator.practice.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Administrator on 2017/5/19.
 * 缓存数据类
 */

public class PrefUtils {
    public static boolean getBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences("frist_enter",Context.MODE_PRIVATE);
        return sp.getBoolean(key,value);
    }
    public static void setBoolean(Context context,String key,boolean value){
        SharedPreferences sp = context.getSharedPreferences("frist_enter",Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).commit();
    }
    public static String getString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences("frist_enter",Context.MODE_PRIVATE);
        return sp.getString(key,value);
    }
    public static void setString(Context context,String key,String value){
        SharedPreferences sp = context.getSharedPreferences("frist_enter",Context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }
}
