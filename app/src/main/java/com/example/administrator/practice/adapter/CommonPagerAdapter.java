package com.example.administrator.practice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xieya on 2017/5/22.
 */

public class CommonPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public CommonPagerAdapter(FragmentManager fragmentManager, List<Fragment> mFragments){
        super(fragmentManager);
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }
}
