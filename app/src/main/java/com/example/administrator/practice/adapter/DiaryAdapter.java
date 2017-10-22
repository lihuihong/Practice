package com.example.administrator.practice.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.practice.R;
import com.example.administrator.practice.bean.DiaryBean;
import com.example.administrator.practice.view.UpDateActivity;

import java.util.List;

/**
 * 日记界面的 Adapter
 *
 *
 */

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<DiaryBean> mDiaryBeanList;
    private int mEditPosition = -1;

    public DiaryAdapter(Context context, List<DiaryBean> mDiaryBeanList){
        mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mDiaryBeanList = mDiaryBeanList;
    }
    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DiaryViewHolder(mLayoutInflater.inflate(R.layout.diary_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final DiaryViewHolder holder, final int position) {
        holder.mData.setText(mDiaryBeanList.get(position).getDate());
        holder.mTitle.setText(mDiaryBeanList.get(position).getTitle());
        holder.mContent.setText("  " + mDiaryBeanList.get(position).getContent());
        holder.mIvEdit.setVisibility(View.INVISIBLE);
        holder.mll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.mIvEdit.getVisibility() == View.VISIBLE) {
                    holder.mIvEdit.setVisibility(View.GONE);
                } else {
                    holder.mIvEdit.setVisibility(View.VISIBLE);
                }
                if (mEditPosition != position) {
                    notifyItemChanged(mEditPosition);
                }
                mEditPosition = position;
            }
        });

        holder.mIvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpDateActivity.class);
                intent.putExtra("title",mDiaryBeanList.get(position).getTitle());
                intent.putExtra("content",  mDiaryBeanList.get(position).getContent());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDiaryBeanList.size();
    }

    public static class DiaryViewHolder extends RecyclerView.ViewHolder{


        TextView mData;
        TextView mTitle;
        TextView mContent;
        ImageView mIvEdit;
        LinearLayout mll;


        DiaryViewHolder(View view){
            super(view);
            mData = (TextView) view.findViewById(R.id.tv_date);
            mTitle = (TextView) view.findViewById(R.id.tv_title);
            mContent = (TextView) view.findViewById(R.id.tv_content);
            mIvEdit = (ImageView) view.findViewById(R.id.iv_end);
            mll = (LinearLayout) view.findViewById(R.id.ll_item);
        }
    }
}
