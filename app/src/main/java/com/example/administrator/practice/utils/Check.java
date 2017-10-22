package com.example.administrator.practice.utils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by xieya on 2017/5/22.
 */

public class Check {

    public static boolean isNull(Object o){
        return o == null;
    }

    public static boolean isEmpty(CharSequence str){
        return isNull(str) || str.length() ==0;
    }

    public static boolean isEmpty(Object[] objects){
        return isNull(objects) || objects.length == 0;
    }

    public static boolean isEmpty(Collection<?> collection){
        return isNull(collection) || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> map){
        return isNull(map) || map.isEmpty();
    }


}