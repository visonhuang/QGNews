package com.qg.qgnews.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.Toast;

import com.qg.qgnews.util.Tool;

import java.util.List;

/**
 * Created by linzongzhan on 2017/7/28.
 */

public class FragmentPagerAdapterNewsMessage extends FragmentPagerAdapter {

    private List<Fragment> mFragmentList;

    /**
     * @param fragmentManager
     * @param mFragmentList Fragment的一个集合
     */
    public FragmentPagerAdapterNewsMessage (FragmentManager fragmentManager,List<Fragment> mFragmentList) {
        super(fragmentManager);
        this.mFragmentList = mFragmentList;
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
