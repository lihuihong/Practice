package com.example.administrator.practice.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.practice.R;
import com.example.administrator.practice.adapter.MyAdapter;
import com.example.administrator.practice.bean.RcommendBean;
import com.example.administrator.practice.utils.CacheUtlis;
import com.example.administrator.practice.utils.PrefUtils;
import com.example.administrator.practice.view.NewsDetailActivity;
import com.example.administrator.practice.view.RecommendApi;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;


/**
 * Created by xieya on 2017/5/19.
 */

public class RecommendFragment extends Fragment {
    @ViewInject(R.id.lv_recommend)
    private ListView mListView;
    @ViewInject(R.id.sfl_recommend)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<RcommendBean.ResultBean.DataBean> mDataBeen;

    private ImageButton mImageButton;
    private Toolbar recommend_Toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recommend, null);
        ViewUtils.inject(this, view);
        recommend_Toolbar= (Toolbar) view.findViewById(R.id.recommend_toolbar);
        recommend_Toolbar.setTitle("ONE·一个");
        recommend_Toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
   //    recommend_Toolbar.setNavigationIcon(R.drawable.menu);
        //先判断有没有缓存，如果有，就加载缓存
        String cache = CacheUtlis.getCache(RecommendApi.NEWS, getActivity());
        if (!TextUtils.isEmpty(cache)) {
            parsedJson(cache);
        }
        processData();
        refresh();
        return view;
    }
/*
    //测量状态栏的高度
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }*/

    private void refresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        processData();
                        // 停止刷新
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                }, 3000); // 10秒后发送消息，停止刷新
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             *
             * @param parent 当前lisyview
             * @param view 当前被点击的itemview对象
             * @param position
             * @param id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RcommendBean.ResultBean.DataBean dataBean = mDataBeen.get(position);
                //记录每个item的唯一标记,用来标记已读
                String ids = PrefUtils.getString(getActivity(), "ids", "");
                //判断标记是否已经存在，只有不包含，才追加
                if (!ids.contains(dataBean.getUniquekey())) {
                    ids = ids + dataBean.getUniquekey() + ",";
                    PrefUtils.setString(getActivity(), "ids", ids);
                }
                //要将被点击的item的文字的颜色给位灰色
                if (!TextUtils.isEmpty(dataBean.getThumbnail_pic_s02()) && !TextUtils.isEmpty(dataBean.getThumbnail_pic_s03()) && !TextUtils.isEmpty(dataBean.getThumbnail_pic_s())) {
                    TextView tv_title3 = (TextView) view.findViewById(R.id.tv_title3);
                    tv_title3.setTextColor(Color.GRAY);
                } else if (!TextUtils.isEmpty(dataBean.getThumbnail_pic_s()) && !TextUtils.isEmpty(dataBean.getThumbnail_pic_s02())) {
                    TextView tv_title2 = (TextView) view.findViewById(R.id.tv_title2);
                    tv_title2.setTextColor(Color.GRAY);
                } else {
                    TextView tv_title1 = (TextView) view.findViewById(R.id.tv_title1);
                    tv_title1.setTextColor(Color.GRAY);
                }

                Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
                intent.putExtra("url", dataBean.getUrl());
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

            }
        });
        //listview滑动监听
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:
                        //滑动停止时调用
                        if (mListView.getLastVisiblePosition()==mDataBeen.size() - 1) {
                            Toast.makeText(getActivity(), "我是有底线的", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL:
                        //正在滚动时调用
                        break;
                    case SCROLL_STATE_FLING:
                        //手指快速滑动时,在离开ListView由于惯性滑动
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private void processData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, RecommendApi.NEWS, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String JsonData = responseInfo.result;
                Log.i("debug", "jsonStr=" + JsonData);
                //写缓存
                CacheUtlis.setCache(RecommendApi.NEWS, JsonData, getActivity());
                if (!TextUtils.isEmpty(JsonData)) {
                    parsedJson(JsonData);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });

    }

    private void parsedJson(String json) {
        Gson gson = new Gson();
        RcommendBean bean = gson.fromJson(json, RcommendBean.class);
        mDataBeen = bean.getResult().getData();
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.color1, R.color.color2);
        mSwipeRefreshLayout.setDistanceToTriggerSync(400);
        mSwipeRefreshLayout.setSize(SwipeRefreshLayout.LARGE);
        mListView.setAdapter(new MyAdapter(getContext(), mDataBeen) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                //先缩小view
                ViewHelper.setScaleX(view, 0.3f);
                ViewHelper.setScaleY(view, 0.3f);
                //以属性动画放大
                com.nineoldandroids.view.ViewPropertyAnimator.animate(view).scaleX(1).setDuration(500).start();
                com.nineoldandroids.view.ViewPropertyAnimator.animate(view).scaleY(1).setDuration(500).start();
                return view;
            }
        });

    }
}
