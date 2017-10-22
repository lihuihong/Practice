package com.example.administrator.practice.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.administrator.practice.utils.FindApi;
import com.example.administrator.practice.R;
import com.example.administrator.practice.adapter.FindAdapter;
import com.example.administrator.practice.bean.FindBean;
import com.example.administrator.practice.utils.GsonHelper;
import com.example.administrator.practice.utils.HttpResponseCallBack;
import com.example.administrator.practice.utils.VolleyHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by xieya on 2017/5/19.
 */

public class FindFragment extends Fragment{
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView_photo;
    private View v;
    private static String response = "";
    List<FindBean> meiziBeanList = new ArrayList<>();
    private RelativeLayout mRl_bar;
    private TextView mTextView;
    private ImageButton mImageButton;
    private Toolbar find_Toolbar;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v=inflater.inflate(R.layout.find_frgment,null);
        initId();
        find_Toolbar.setTitle("GIRLS");
        find_Toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        intView();

        refreshMeizi();
        return v;
    }

    /**
     * 刷新当前界面
     */
   private void refreshMeizi() {
     //   swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
             //   intView();
               // swipeRefreshLayout.setRefreshing(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        intView();
                        // 停止刷新
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000); // 10秒后发送消息，停止刷新


            }
        });
    }
    private void intView() {
        VolleyHelper.sendGetHttp(getActivity(), FindApi.getMeiziApi(), new HttpResponseCallBack() {
            @Override
            public void success(String s) {
                response=s;
                meiziBeanList = GsonHelper.getMeiziBean(response);
                recyclerView_photo.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary,R.color.color1,R.color.color2);
                swipeRefreshLayout.setDistanceToTriggerSync(400);
             //   swipeRefreshLayout.setProgressBackgroundColor(R.color.colorWite);
                swipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
                Collections.shuffle(meiziBeanList);
                recyclerView_photo.setAdapter(new FindAdapter(meiziBeanList, FindFragment.this));
            }

            @Override
            public void error(VolleyError error) {

            }
        });

    }
    private void initId() {
        swipeRefreshLayout= (SwipeRefreshLayout) v.findViewById(R.id.sfl_find);
        recyclerView_photo= (RecyclerView) v.findViewById(R.id.item_iv_find);
        find_Toolbar= (Toolbar) v.findViewById(R.id.find_toolbar);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
