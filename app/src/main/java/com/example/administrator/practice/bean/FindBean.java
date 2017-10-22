package com.example.administrator.practice.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xieya on 2017/5/22.
 */

public class FindBean {
    @SerializedName("_id")
    private String id;
    @SerializedName("url")
    private String imageUrl;
    @SerializedName("who")
    private String who;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public FindBean(String imageUrl){
        this.imageUrl = imageUrl;
    }

}
