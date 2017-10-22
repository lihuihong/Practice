package com.example.administrator.practice.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.practice.R;
import com.example.administrator.practice.adapter.DiaryAdapter;
import com.example.administrator.practice.bean.DiaryBean;
import com.example.administrator.practice.utils.DiaryData;
import com.example.administrator.practice.utils.GetDate;
import com.example.administrator.practice.view.EditActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

import static android.support.design.widget.NavigationView.OnClickListener;

/**
 * Created by xieya on 2017/5/19.
 */
@SuppressLint("ValidFragment")
public class NoteFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private LinearLayout mLinearLayoutTimeTitle;
    private LinearLayout mLinearLayout;
    private TextView mDataTime;
    private Toolbar note_toolbar;
    private DiaryData mDiaryData;
    private static List<DiaryBean> mDiaryBeanList;
    private SQLiteDatabase mDatabase;

    private FloatingActionButton mButton;
    private View view;
    private RelativeLayout mRl_bar;
    private TextView mTextView;
    private ImageButton mImageButton;
    private SlidingMenu mSlidingMenu;


    public NoteFragment(SlidingMenu slidingMenu) {
        mSlidingMenu = slidingMenu;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary, null);
        mButton = (FloatingActionButton) view.findViewById(R.id.fab);
        mDataTime = (TextView) view.findViewById(R.id.tv_datetime);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rl_diary);
        mLinearLayout = (LinearLayout) view.findViewById(R.id.ll_default);
        mLinearLayoutTimeTitle = (LinearLayout) view.findViewById(R.id.ll_title_time);
            note_toolbar= (Toolbar) view.findViewById(R.id.note_toolbar);
        note_toolbar.setTitle("日记");
        note_toolbar.setTitleTextColor(getResources().getColor(R.color.colorWite));

        note_toolbar.setNavigationIcon(R.drawable.menu);
        note_toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingMenu.toggle();
            }
        });
        intiView();
        intiData();
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        return view;
    }
    private void intiData() {



        //用线性显示 类似于listview
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mDataTime.setText(GetDate.getDate().toString());
        mRecyclerView.setAdapter(new DiaryAdapter(getContext(), mDiaryBeanList));

    }

    private void intiView() {
        mDiaryData = new DiaryData(getActivity());
        mDiaryBeanList = new ArrayList<>();
        List<DiaryBean> diaryList = new ArrayList<>();
        mDatabase = mDiaryData.getReadableDatabase();
        Cursor cursor = mDatabase.query("Diary", null, null, null, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {//判断  查询出的数据是否为 null
            while (cursor.moveToNext()) {//用于循环   查询出的数据
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                mDiaryBeanList.add(new DiaryBean(date, title, content));
                String dateSystem = GetDate.getDate().toString();
                System.out.println("时间" + dateSystem + "....." + date);
                //根据判断时间是否相等，来移除默认条目
                if (date.equals(dateSystem)) {
                    mLinearLayoutTimeTitle.setVisibility(View.GONE);
                    mLinearLayout.setVisibility(View.GONE);
                    // mLinearLayoutRoot.removeView(mLinearLayout);
                } else {
                }
            }
        } else {
            mLinearLayoutTimeTitle.setVisibility(View.VISIBLE);
            mLinearLayout.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "记录每一天", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        for (int i = mDiaryBeanList.size() - 1; i >= 0; i--) {
            diaryList.add(mDiaryBeanList.get(i));
        }
        mDiaryBeanList = diaryList;

    }

    @Override
    public void onResume() {
        super.onResume();
        intiView();
        intiData();
    }
}
