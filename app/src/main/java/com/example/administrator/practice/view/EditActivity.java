package com.example.administrator.practice.view;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.practice.R;
import com.example.administrator.practice.utils.DiaryData;
import com.example.administrator.practice.utils.GetDate;

import cc.trity.floatingactionbutton.FloatingActionButton;
import cc.trity.floatingactionbutton.FloatingActionsMenu;

public class EditActivity extends AppCompatActivity {
    private TextView mData;
    private EditText mTitle;
    private EditText mContent;
    private FloatingActionsMenu mActionButtonRignt;
    private FloatingActionButton mActionButtonBack;
    private FloatingActionButton mActionButtonSave;
    private DiaryData mHelper;
    private String mDate;
    private String mContent1;
    private String mTitle1;
    private Toolbar edit_toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mHelper = new DiaryData(this);
        intiView();
        intiData();
    }

    private void intiView() {
        mData = (TextView) findViewById(R.id.tv_time);
        mContent = (EditText) findViewById(R.id.et_content);
        mTitle = (EditText) findViewById(R.id.et_title);
        mActionButtonRignt = (FloatingActionsMenu) findViewById(R.id.right);
        mActionButtonBack = (FloatingActionButton) findViewById(R.id.fab_back);
        mActionButtonSave = (FloatingActionButton) findViewById(R.id.fab_save);
        edit_toolbar=(Toolbar) findViewById(R.id.edit_toolbar);
        edit_toolbar.setTitle("添加日记");
        edit_toolbar.setNavigationIcon(R.drawable.news_back);
        edit_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        edit_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });
        mData.setText("今天，" + GetDate.getDate());
    }



    private void intiData() {
        mActionButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getWindow().getContext());
                builder.setIcon(R.drawable.ee);
                builder.setTitle("提示");
                builder.setMessage("确定保存？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!TextUtils.isEmpty(mContent.getText().toString()) || !TextUtils.isEmpty(mTitle.getText().toString())) {
                            mDate = GetDate.getDate().toString();
                            mTitle1 = mTitle.getText().toString();
                            mContent1 = mContent.getText().toString();
                            SQLiteDatabase db = mHelper.getReadableDatabase();
                            ContentValues values = new ContentValues();
                            values.put("date", mDate);
                            values.put("title", mTitle1);
                            values.put("content", mContent1);
                            db.insert("Diary", null, values);
                            values.clear();
                            Toast.makeText(EditActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            finish();
                            overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
                        } else {
                            Toast.makeText(EditActivity.this, "请输入你要保存的主题或内容", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Toast.makeText(EditActivity.this, "已取消", Toast.LENGTH_SHORT).show();

                    }
                });
                builder.create().show();
            }
        });
        mActionButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
}
