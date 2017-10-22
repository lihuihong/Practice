package com.example.administrator.practice.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.administrator.practice.R;
import com.example.administrator.practice.utils.PrefUtils;

public class SplashActivity extends AppCompatActivity {
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            //在setContentView()之前执行，用于告诉Window页面切换需要使用动画
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            setContentView(R.layout.activity_splash);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_splash);
            //缩放动画
            ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //渐变动画
            AlphaAnimation aa = new AlphaAnimation(0f, 1f);
            //动画合集
            AnimationSet animationSet = new AnimationSet(true);
            //animationSet.addAnimation(ra);
            animationSet.addAnimation(sa);
            animationSet.addAnimation(aa);
            animationSet.setDuration(1000);
            //启动动画
            mRelativeLayout.startAnimation(animationSet);

            animationSet.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    //动画结束跳转页面
                    //如果第一次进入，就跳新手引导
                    //否则，就跳主页面
                    boolean frist_enter = PrefUtils.getBoolean(SplashActivity.this, "frist_enter", true);
                    Intent intent;
                    if (frist_enter) {
                        //跳转到引导页面
                        intent = new Intent(SplashActivity.this, GuideActivity.class);
                        //结束启动动画
                        SplashActivity.this.finish();
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    } else {
                        //跳转到登陆页面
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                        //结束启动动画
                        SplashActivity.this.finish();
                        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                    }
                    startActivity(intent);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }
