package com.example.administrator.practice.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.practice.R;
import com.example.administrator.practice.bean.RcommendBean;
import com.example.administrator.practice.utils.PrefUtils;

import java.util.List;

import static com.example.administrator.practice.R.id.tv_title3;

/**
 * Created by Administrator on 2017/5/24.
 */

public class MyAdapter extends BaseAdapter {
    private Context context;

    private List<RcommendBean.ResultBean.DataBean> mNewsDatas;

    private LayoutInflater inflater;

    private final int TYPE_COUNT = 3;

    private final int TYPE_ONE = 0;

    private final int TYPE_TWO = 1;

    private final int TYPE_THREE = 2;

    private int currentType;
    private RcommendBean.ResultBean.DataBean mNewsData;

    public MyAdapter(Context context, List<RcommendBean.ResultBean.DataBean> newsDatas) {
        this.context = context;
        mNewsDatas = newsDatas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mNewsDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mNewsDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderThree viewHolderThree = null;
        ViewHolderTwo viewHolderTwo = null;
        ViewHolderOne viewHolderOne = null;
        RcommendBean.ResultBean.DataBean newsData = mNewsDatas.get(position);
        currentType = getItemViewType(position);
        //根据currentType来加载不同的布局,并复用convertview
        if (currentType == TYPE_ONE) {    //加载第一种布局
            //首先判断convertview==null  inflater.inflate(R.layout.recommend_item1,null);
            if (convertView == null) {
                viewHolderOne = new ViewHolderOne();
                convertView = inflater.inflate(R.layout.recommend_item1,null);
                viewHolderOne.iv_title1 = (ImageView) convertView.findViewById(R.id.iv_title1);
                viewHolderOne.tv_come1 = (TextView) convertView.findViewById(R.id.tv_come1);
                viewHolderOne.tv_time1 = (TextView) convertView.findViewById(R.id.tv_time1);
                viewHolderOne.tv_title1 = (TextView) convertView.findViewById(R.id.tv_title1);
                convertView.setTag(viewHolderOne);
            }else {
                viewHolderOne = (ViewHolderOne) convertView.getTag();
            }
            viewHolderOne.tv_come1.setText(newsData.getAuthor_name());
            viewHolderOne.tv_title1.setText(newsData.getTitle());
            viewHolderOne.tv_time1.setText(newsData.getDate());
            Glide.with(context).load(newsData.getThumbnail_pic_s()).placeholder(R.drawable.news_pic_default).into(viewHolderOne.iv_title1);
        }else if(currentType == TYPE_TWO){ //加载第二种布局
            if (convertView == null) {
                viewHolderTwo = new ViewHolderTwo();
                convertView = inflater.inflate(R.layout.recommend_item2,null);
                viewHolderTwo.iv_title2 = (ImageView) convertView.findViewById(R.id.iv_title2);
                viewHolderTwo.tv_time2 = (TextView) convertView.findViewById(R.id.tv_time2);
                viewHolderTwo.tv_come2 = (TextView) convertView.findViewById(R.id.tv_come2);
                viewHolderTwo.tv_title2 = (TextView) convertView.findViewById(R.id.tv_title2);
                convertView.setTag(viewHolderTwo);
            }else {
                viewHolderTwo = (ViewHolderTwo) convertView.getTag();
            }
            viewHolderTwo.tv_title2.setText(newsData.getTitle());
            viewHolderTwo.tv_come2.setText(newsData.getAuthor_name());
            viewHolderTwo.tv_time2.setText(newsData.getDate());
            Glide.with(context).load(newsData.getThumbnail_pic_s()).placeholder(R.drawable.news_pic_default).into(viewHolderTwo.iv_title2);
        }else {
            if (currentType == TYPE_THREE) {//加载第三种布局
                if (convertView == null) {
                    viewHolderThree = new ViewHolderThree();
                    convertView = inflater.inflate(R.layout.recommend_item3,null);
                    viewHolderThree.tv_title3 = (TextView) convertView.findViewById(tv_title3);
                    viewHolderThree.iv_title_imageView1 = (ImageView) convertView.findViewById(R.id.iv_title_imageView1);
                    viewHolderThree.iv_title_imageView3 = (ImageView) convertView.findViewById(R.id.iv_title_imageView3);
                    viewHolderThree.iv_title_imageView2 = (ImageView) convertView.findViewById(R.id.iv_title_imageView2);
                    viewHolderThree.tv_come3 = (TextView) convertView.findViewById(R.id.tv_come3);
                    viewHolderThree.tv_time3 = (TextView) convertView.findViewById(R.id.tv_time3);
                    convertView.setTag(viewHolderThree);
                } else {
                    viewHolderThree = (ViewHolderThree) convertView.getTag();
                }
                viewHolderThree.tv_title3.setText(newsData.getTitle());
                viewHolderThree.tv_come3.setText(newsData.getAuthor_name());
                viewHolderThree.tv_time3.setText(newsData.getDate());
                Glide.with(context).load(newsData.getThumbnail_pic_s()).placeholder(R.drawable.news_pic_default).into(viewHolderThree.iv_title_imageView1);
                Glide.with(context).load(newsData.getThumbnail_pic_s02()).placeholder(R.drawable.news_pic_default).into(viewHolderThree.iv_title_imageView2);
                Glide.with(context).load(newsData.getThumbnail_pic_s03()).placeholder(R.drawable.news_pic_default).into(viewHolderThree.iv_title_imageView3);
            }
        }
        //根据本地记录。记录每个item的唯一标记,用来标记已读
        String ids = PrefUtils.getString(context, "ids", "");
        if (ids.contains(mNewsData.getUniquekey())){
        //要将被点击的item的文字的颜色给位灰色
            if (!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s02()) && !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s03()) && !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s())) {
                viewHolderThree.tv_title3.setTextColor(Color.GRAY);
            } else if (!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s()) && !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s02())) {
                viewHolderTwo.tv_title2.setTextColor(Color.GRAY);
            } else {
                viewHolderOne.tv_title1.setTextColor(Color.GRAY);
            }
        }else {
            if (!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s02()) && !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s03()) && !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s())) {
                viewHolderThree.tv_title3.setTextColor(Color.BLACK);
            } else if (!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s()) && !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s02())) {
                viewHolderTwo.tv_title2.setTextColor(Color.BLACK);
            } else {
                viewHolderOne.tv_title1.setTextColor(Color.BLACK);
            }
        }

        return convertView;
    }

    class ViewHolderOne {

        public ImageView iv_title1;

        public TextView tv_title1;
        public TextView tv_come1;
        public TextView tv_time1;
    }
    class ViewHolderTwo {

        public TextView tv_title2;
        public ImageView iv_title2;
        private TextView tv_come2;
        private TextView tv_time2;
    }
    class ViewHolderThree {

        public TextView tv_title3;
        public ImageView iv_title_imageView1;
        private ImageView iv_title_imageView2;
        private ImageView iv_title_imageView3;
        private TextView tv_come3;
        private TextView tv_time3;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        mNewsData = mNewsDatas.get(position);

        if (!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s02()) &&!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s03())&& !TextUtils.isEmpty(mNewsData.getThumbnail_pic_s())){
            switch (position){
                case TYPE_THREE:
                    return TYPE_THREE;
                default:
                    return TYPE_THREE;
            }
        }else if (!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s02()) &&!TextUtils.isEmpty(mNewsData.getThumbnail_pic_s())){
            switch (position){
                case TYPE_TWO:
                    return TYPE_TWO;
                default:
                    return TYPE_TWO;
            }
        }else{
            switch (position){
                case TYPE_ONE:
                    return TYPE_ONE;
                default:
                    return TYPE_ONE;
            }
        }

    }
}
