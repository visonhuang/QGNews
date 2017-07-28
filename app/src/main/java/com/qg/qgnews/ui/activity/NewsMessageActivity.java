package com.qg.qgnews.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.FragmentPagerAdapterNewsMessage;
import com.qg.qgnews.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsMessageActivity extends AppCompatActivity {

    //存储新闻信息的集合,注：在这个集合的泛型是新闻信息的一个类
    private List<News> mNewsMessageList = new ArrayList<>();

    //存储初始化后的Fragment
    private List<Fragment> mFragmentList = new ArrayList<>();

    //标识进入了哪一条新闻
    private int mPosition;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_message);

        getMessage();
        initView();
        initFragment();

        FragmentPagerAdapterNewsMessage fragmentPagerAdapterNewsMessage = new FragmentPagerAdapterNewsMessage(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(fragmentPagerAdapterNewsMessage);

        //跳转到指定界面
        mViewPager.setCurrentItem(mPosition);
    }

    /**
     * 从活动调用者获得新闻的信息
     */
    private void getMessage () {
        Intent intent = getIntent();
        mNewsMessageList = (List<News>) intent.getSerializableExtra("NewsMessageList");
        mPosition = intent.getIntExtra("position",0);
    }

    /**
     * 初始化活动中的控件
     */
    private void initView () {
        mViewPager = (ViewPager) findViewById(R.id.news_viewpager);
    }

    /**
     * 初始化Fragment，并将Fragment加入集List集合
     */
    private void initFragment () {
        int size = mFragmentList.size();
        for (int index = 0; index < size; index++) {
            Bundle bundle = new Bundle();
            Fragment fragment = new Fragment();
            bundle.putSerializable("message",mNewsMessageList.get(index));
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
        }
    }


}
