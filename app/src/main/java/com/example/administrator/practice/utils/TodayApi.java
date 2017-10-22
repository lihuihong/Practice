package com.example.administrator.practice.utils;


import java.util.Calendar;

/**
 * Created by Administrator on 2017/6/6.
 */

public class TodayApi {
    static Calendar c = Calendar.getInstance();
    private static int month =c.get(Calendar.MONTH) + 1;
    private static int day = c.get(Calendar.DAY_OF_MONTH) ;
    public static final String TODAY="http://api.juheapi.com/japi/toh?key=ecbf68e7b4a14a6e52cc37958191fde0&v=1.0&month="+month+"&day="+day;
}
