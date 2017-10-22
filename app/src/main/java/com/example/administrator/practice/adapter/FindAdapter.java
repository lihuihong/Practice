package com.example.administrator.practice.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.practice.R;
import com.example.administrator.practice.bean.FindBean;
import com.example.administrator.practice.fragment.FindFragment;
import com.example.administrator.practice.view.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xieya on 2017/5/22.
 */

public class FindAdapter extends RecyclerView.Adapter<FindAdapter.FindViewHolder>{
    private FindFragment findFragment;
    private List<FindBean> findBeanList;


    public FindAdapter(List<FindBean> findBeanList, FindFragment findFragment){
        this.findBeanList=findBeanList;
        this.findFragment=findFragment;
    }


    @Override
    public FindViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.find_item,parent,false);
        return new FindViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FindViewHolder holder, final int position) {
        Glide.with(findFragment)
                .load(findBeanList.get(position).getImageUrl())
                .fitCenter()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivFind);

        holder.ivFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> resultList = new ArrayList<>();
                for (FindBean meiziBean : findBeanList) {
                    resultList.add(meiziBean.getImageUrl());
                }
                DetailActivity.startActivity(findFragment.getActivity(), resultList,position);

            }
        });
    }

    @Override
    public int getItemCount() {
        if(findBeanList.size()>0){
            return findBeanList.size();
        }
        return 0;
    }
    public static class FindViewHolder extends RecyclerView.ViewHolder {

        ImageView ivFind;

        public FindViewHolder(View itemView) {
            super(itemView);
            ivFind = (ImageView) itemView.findViewById(R.id.item_iv_meizi);
        }
    }
}
