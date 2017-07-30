package com.qg.qgnews.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.MyFragmentPagerAdapter;
import com.qg.qgnews.ui.fragment.ManagerPerson;
import com.qg.qgnews.ui.fragment.ManagerPersonE;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends TopBarBaseActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<String> titleList = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_manager;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        tabLayout = (TabLayout) findViewById(R.id.manager_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.manager_viewPager);
        fragmentList.add(new ManagerPerson());
        fragmentList.add(new ManagerPersonE());
        titleList.add("管理员列表");
        titleList.add("审批中");

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }




 /*   public void replaceFragment (Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.manager_frameLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitle(title);
    } */
}
