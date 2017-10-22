package com.example.administrator.practice.bean;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.administrator.practice.R;


/**
 * Created by Administrator on 2017/6/17.
 * 底部弹窗实现
 */

public class BottomPopupWindow extends PopupWindow {
    private Button mBtnTakePhoto;
    private Button mBtnSelectPhoto;
    private Button mBtnCancel;
    private View mView;

    public BottomPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = mInflater.inflate(R.layout.popup_up, null);
        mBtnCancel = (Button) mView.findViewById(R.id.btn_cancel);
        mBtnSelectPhoto = (Button) mView.findViewById(R.id.btn_select_photo);
        mBtnTakePhoto = (Button) mView.findViewById(R.id.btn_take_photo);
        mBtnTakePhoto.setOnClickListener(itemsOnClick);
        mBtnSelectPhoto.setOnClickListener(itemsOnClick);

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        // mView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if (y > height){
                        dismiss();
                    }
                }
                return true;
            }
        });
        //设置PopupWindow的View
        this.setContentView(mView);
        //设置PopupWindow弹出窗体的宽
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        //设置PopupWindow弹出窗体的高
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置PopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为全透明
        ColorDrawable dw = new ColorDrawable(0x00000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);


    }
}

