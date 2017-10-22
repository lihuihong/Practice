package com.example.administrator.practice.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.practice.R;
import com.example.administrator.practice.adapter.CommonPagerAdapter;
import com.example.administrator.practice.fragment.DetailFragment;
import com.example.administrator.practice.utils.Check;
import com.example.administrator.practice.utils.GlideHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

public class DetailActivity extends AppCompatActivity {
    /**
     * 保存图片网络地址的 List
     */
    private List<String> mImageUrlList;

    /**
     * ViewPager 中所有的 Fragment
     */
    private List<Fragment> mFragments;

    /**
     * 保存图片缓存地址的 List
     */
    private List<String> mCachePathList;

    private static final String IMAGE_URL_LIST = "imageUrlList";
    private static final String POSITION = "position";

    private ViewPager vp_show_photo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        vp_show_photo= (ViewPager) findViewById(R.id.detail_vp_show_photo);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Bundle bundle = getIntent().getExtras();
        mImageUrlList = new ArrayList<>();
        mFragments = new ArrayList<>();
        mCachePathList = new ArrayList<>();
        int position = getIntent().getIntExtra(POSITION, -1);
        mImageUrlList = getIntent().getStringArrayListExtra(IMAGE_URL_LIST);
        initViewWithCache(position);
    }

    private void initViewWithCache(final int position) {
        Task.call(new Callable<List<String>>() {
            @Override
            public List<String> call() throws Exception {
                for (String imageUrl : mImageUrlList) {
                    mCachePathList.add(GlideHelper.getImagePathFromCache(imageUrl, DetailActivity.this));
                }
                return mCachePathList;
            }
        }, Task.BACKGROUND_EXECUTOR).continueWith(new Continuation<List<String>, Object>() {
            @Override
            public Object then(Task<List<String>> task) throws Exception {
                List<String> mCachePathList = task.getResult();
                if(!Check.isEmpty(mCachePathList)){
                    for (String cachePath : mCachePathList) {
                        DetailFragment fragment = DetailFragment.newInstance(cachePath);
                        mFragments.add(fragment);
                    }
                    CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(), mFragments);
                    vp_show_photo.setAdapter(adapter);
                    vp_show_photo.setCurrentItem(position);
                }
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);

    }

    public static void startActivity(Context context, ArrayList<String> resultList, int position) {
        Intent intent = new Intent(context, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IMAGE_URL_LIST,resultList);
        bundle.putInt(POSITION, position);
        intent.putExtras(bundle);
        context.startActivity(intent);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
