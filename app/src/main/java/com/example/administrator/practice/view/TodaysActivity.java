package com.example.administrator.practice.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.practice.R;
import com.example.administrator.practice.bean.TodayBean;
import com.example.administrator.practice.utils.TodayApi;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TodaysActivity extends AppCompatActivity {
    private ListView mListView;
    private TextView mTextView;
    private ImageButton mImageButton;
    private List<TodayBean.ResultBean> mResult;
    private ImageButton mImageButtonBlack;
    private Toolbar today_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today);
        mListView = (ListView) findViewById(R.id.listview);
        today_toolbar=(Toolbar) findViewById(R.id.today_toolbar);
        today_toolbar.setNavigationIcon(R.drawable.news_back);
        today_toolbar.setTitle("那年今日");
        today_toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        processData();

        //返回按钮
today_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        finish();
        overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
    }
});

    }
    private void processData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, TodayApi.TODAY, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String JsonData = responseInfo.result;
                parsedJson(JsonData);
            }
            @Override
            public void onFailure(HttpException e, String s) {
            }
        });
    }

    private void parsedJson(String jsonData) {
        //手动解析json
        TodayBean todayBean = new TodayBean();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray data = jsonObject.optJSONArray("result");
            if (data!=null && data.length()>0){

                List<TodayBean.ResultBean> resultBeans = new ArrayList<>();
                //设置列表数据
                todayBean.setResult(resultBeans);
                //for循环，解析每条数据
                for (int i = 0;i<data.length();i++){
                    JSONObject object = (JSONObject) data.get(i);
                    TodayBean.ResultBean resultBean = new TodayBean.ResultBean();
                    //添加到集合中
                    resultBeans.add(resultBean);

                    String des=object.optString("des");
                    resultBean.setDes(des);
                    String title = object.optString("title");
                    resultBean.setTitle(title);
                    int year = object.optInt("year");
                    resultBean.setYear(year);
                    int month = object.optInt("month");
                    resultBean.setMonth(month);
                    int day = object.optInt("day");
                    resultBean.setDay(day);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mResult = todayBean.getResult();
        TodayAdapter todayAdapter = new TodayAdapter();
        Log.i("debug", "jsonStr=" +mResult.get(1).getTitle());


        mListView.setAdapter(todayAdapter);
    }

    class TodayAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mResult.size();
        }

        @Override
        public Object getItem(int position) {
            return mResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            TodayBean.ResultBean bean = mResult.get(position);
            if (convertView == null){
                viewHolder = new ViewHolder();
                Log.i("debug", "jsonStr=" +bean.getTitle());
                //convertView = mLayoutInflater.inflate(R.layout.item_today,null);
                convertView = View.inflate(getApplicationContext(),R.layout.item_today,null);
                viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                viewHolder.tv_title_content = (TextView) convertView.findViewById(R.id.tv_title_content);
                viewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_title.setText(bean.getTitle());
            viewHolder.tv_title_content.setText(bean.getDes());
            viewHolder.tv_time.setText(bean.getYear()+"年"+bean.getMonth()+"月"+bean.getDay()+"日");
            return convertView;
        }
        class ViewHolder {

            public TextView tv_title;
            public TextView tv_title_content;
            public TextView tv_time;
        }
    }
    //返回键拦截
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.zoomin,R.anim.zoomout);
    }


}
