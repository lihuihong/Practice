package com.example.administrator.practice.view;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.practice.R;
import com.example.administrator.practice.utils.DiaryData;
import com.example.administrator.practice.utils.GetDate;

import cc.trity.floatingactionbutton.FloatingActionButton;

public class UpDateActivity extends AppCompatActivity {
    private TextView tv_time;
    private EditText et_title;
    private EditText et_content;
    private FloatingActionButton update_diary_delete;
    private FloatingActionButton update_diary_save;
    private FloatingActionButton update_diary_back;
    private DiaryData mHlper = new DiaryData(this);

    private RelativeLayout mRl_bar;
    private TextView mTextView;
    private ImageButton mImageButton;
    private ImageButton mImageButtonBack;
    private Toolbar update_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_data);
        tv_time = (TextView) findViewById(R.id.tv_time);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        update_diary_back = (FloatingActionButton) findViewById(R.id.update_diary_back);
        update_diary_delete = (FloatingActionButton) findViewById(R.id.update_diary_delete);
        update_diary_save = (FloatingActionButton) findViewById(R.id.update_diary_save);
        update_toolbar=(Toolbar) findViewById(R.id.update_toolbar);

        intiView();
        update_toolbar.setNavigationIcon(R.drawable.news_back);
        update_toolbar.setTitle("编辑日记");
        update_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        update_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

            }
        });
        update_diary_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
            }
        });
        update_diary_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase dbUpdate = mHlper.getWritableDatabase();
                ContentValues valuesUpdate = new ContentValues();
                String title = et_title.getText().toString();
                String content = et_content.getText().toString();
                valuesUpdate.put("title", title);
                valuesUpdate.put("content", content);
                dbUpdate.update("Diary", valuesUpdate, "title = ?", new String[]{title});
                dbUpdate.update("Diary", valuesUpdate, "content = ?", new String[]{content});
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);

            }
        });
        update_diary_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show();
            }
        });
    }




    private void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getWindow().getContext());
        builder.setIcon(R.drawable.ee);
        builder.setTitle("提示");
        builder.setMessage("确定要删除该日记吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String title = et_title.getText().toString();
                SQLiteDatabase dbDelete = mHlper.getWritableDatabase();
                dbDelete.delete("Diary", "title = ?", new String[]{title});
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    private void intiView() {
        Intent intent = getIntent();
        if (intent != null) {
            tv_time.setText("今天，" + GetDate.getDate());
            et_title.setText(intent.getStringExtra("title"));
            et_content.setText(intent.getStringExtra("content"));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }

}
