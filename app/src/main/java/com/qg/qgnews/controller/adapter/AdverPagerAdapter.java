package com.qg.qgnews.controller.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.qg.qgnews.ui.fragment.AdverFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 黄伟烽 on 2017/8/2.
 */

public class AdverPagerAdapter extends FragmentPagerAdapter {

    private List<AdverFragment> mFragmentList = new ArrayList<>();

    public AdverPagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentList.add(AdverFragment.newInstance(1));
        mFragmentList.add(AdverFragment.newInstance(2));
        mFragmentList.add(AdverFragment.newInstance(3));
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
