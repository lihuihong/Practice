package com.example.administrator.practice.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by xieya on 2017/5/19.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> pagerFragments;
    public SectionsPagerAdapter(FragmentManager fm,List<Fragment> pagerFragments) {
        super(fm);
        this.pagerFragments=pagerFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return pagerFragments.get(position);
    }

    @Override
    public int getCount() {
        return pagerFragments.size();
    }

}
