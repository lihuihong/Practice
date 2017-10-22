package com.example.administrator.practice.utils;

import com.example.administrator.practice.bean.FindBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Gson 的处理类
 *
 * Created by developerHaoz on 2017/5/3.
 */

public class GsonHelper {

    /**
     * 将一个 String 类型的数据解析成一个 List<MeiziBean>
     *
     * @param response 包含妹子信息的 String
     * @return List<MeiziBean>
     */
    public static List<FindBean> getMeiziBean(String response){
        List<FindBean> meiziBeanList = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(response);
                String meiziArrayStr = jsonObject.getString("results");
                Type meiziListType = new TypeToken<List<FindBean>>(){}.getType();
                Gson gson = new Gson();
                meiziBeanList = gson.fromJson(meiziArrayStr, meiziListType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return meiziBeanList;
    }

}
