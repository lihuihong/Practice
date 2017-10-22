package com.example.administrator.practice.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.administrator.practice.R;
import com.example.administrator.practice.utils.PrefUtils;

import java.util.ArrayList;

public class GuideActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private ArrayList<ImageView> mImageViews;
    private int[] mImageViewId = new int[]{R.drawable.splash1, R.drawable.splash2, R.drawable.splash3};

    //private int[] mImageViewId = new int[]{R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    private LinearLayout mLinearLayoutPiont;
    private ImageView mImageViewPiont;
    private Button mButtonPiont;
    private int mPiontDis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mLinearLayoutPiont = (LinearLayout) findViewById(R.id.ll_piont);
        mImageViewPiont = (ImageView) findViewById(R.id.iv_point);
        mButtonPiont = (Button) findViewById(R.id.btn_start);
        mButtonPiont.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.btn_start) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        mButtonPiont.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        mButtonPiont.setTextColor(Color.parseColor("#51ACE3"));
                    }
                }
                return false;
            }
        });
        intiData();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //viewpager设置适配器
        mViewPager.setAdapter(new GuideViewPagerAdapter());
        //设置viewpager监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            /**
             * 页面滑动过程中的回调
             * @param position 当前滑动的页面
             * @param positionOffset 移动偏移百分比
             * @param positionOffsetPixels 移动偏移像素
             */
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //更新小红点距离  就是小点当前的左边距  需要加上当前的位置
                int dis = (int) (mPiontDis * positionOffset) + position * mPiontDis;
                //小点当前的布局参数
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)
                        mImageViewPiont.getLayoutParams();
                params.leftMargin = dis;//修改左边距
                //重新设置参数
                mImageViewPiont.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                //莫个页面被选中
                if (position == mImageViews.size() - 1) {
                    mButtonPiont.setVisibility(View.VISIBLE);
                } else {
                    mButtonPiont.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //视图树,监听layout方法结束的事件
        mImageViewPiont.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //layout方法执行完毕的回调
                //移除OnGlobalLayoutListener()监听  避免重复回调
                mImageViewPiont.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                //测量 必须等oncreat()方法完毕
                //计算两个小圆点的距离  移动距离=第二个小圆点left-第一个小圆点left
                mPiontDis = mLinearLayoutPiont.getChildAt(1).getLeft()
                        - mLinearLayoutPiont.getChildAt(0).getLeft();
            }
        });
        //设置按钮点击事件
        mButtonPiont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //记录是否是第一次进入
                PrefUtils.setBoolean(getApplication(), "frist_enter", false);
                Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

    }

    //初始化数据
    private void intiData() {
        mImageViews = new ArrayList<>();
        for (int i = 0; i < mImageViewId.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mImageViewId[i]);//使用图片来添加背景
            mImageViews.add(imageView);//给容器添加图片

            //添加小点
            ImageView piont = new ImageView(this);
            piont.setImageResource(R.drawable.piontother);
            //初始化布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                //从第二个点设置小圆点左边距
                params.leftMargin = 20;
            }
            piont.setLayoutParams(params);
            mLinearLayoutPiont.addView(piont);//给容器添加小点

        }

    }

    class GuideViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = mImageViews.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
