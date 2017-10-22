package com.example.administrator.practice.view;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.administrator.practice.R;
import com.example.administrator.practice.bean.CustomProgressDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NewsDetailActivity extends AppCompatActivity {
    @ViewInject(R.id.wv_news)
    private WebView mWebView;
    @ViewInject(R.id.pd_loading)
    private ProgressBar mProgressBar;

    private ImageButton mImageButtonBack;
    private Toolbar news_toolbar;
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };
    private CustomProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ViewUtils.inject(this);
        String url = getIntent().getStringExtra("url");
        mWebView.loadUrl(url);
        WebSettings settings = mWebView.getSettings();
        settings.setBuiltInZoomControls(true);//显示缩放按钮
        settings.setUseWideViewPort(true);//支持双击缩放
        settings.setJavaScriptEnabled(true);//支持js功能

        //   setStatusBar();
        news_toolbar = (Toolbar) findViewById(R.id.news_toolbar);
        news_toolbar.setTitle("资讯");
        news_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        news_toolbar.setNavigationIcon(R.drawable.news_back);
        news_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isExit) {
                    isExit = true;
                    Toast.makeText(getApplicationContext(), "再按一次退出资讯",
                            Toast.LENGTH_SHORT).show();
                    // 利用handler延迟发送更改状态信息
                    mHandler.sendEmptyMessageDelayed(0, 2000);
                } else {
                    Intent intent = new Intent(NewsDetailActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                }

            }
        });
        mDialog = new CustomProgressDialog(this, "加载中...", R.drawable.loading);
        mDialog.getWindow().setDimAmount(0.5f);//去除遮罩
        mWebView.setWebViewClient(new WebViewClient() {
            //开始加载网页

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mDialog.show();
            }

            //加载网页结束
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                mDialog.dismiss();
            }

            //所有链路跳转走这个方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String request) {
                view.loadUrl(request);
                return true;
            }

        });
        mWebView.goBack();
        mWebView.goForward();
    }


    //按下返回按钮两次  退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次退出资讯",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(0, 2000);
        } else {
            finish();
            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        }
    }
}
