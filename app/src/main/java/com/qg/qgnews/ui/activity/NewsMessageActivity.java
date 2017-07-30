package com.qg.qgnews.ui.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.FragmentPagerAdapterNewsMessage;
import com.qg.qgnews.model.News;
import com.qg.qgnews.ui.fragment.NewsMessage;

import java.util.ArrayList;
import java.util.List;

public class NewsMessageActivity extends AppCompatActivity {

    //存储新闻信息的集合,注：在这个集合的泛型是新闻信息的一个类
    private List<News> mNewsMessageList = new ArrayList<>();

    //存储初始化后的Fragment
    private List<Fragment> mFragmentList = new ArrayList<>();

    //标识进入了哪一条新闻
    private int mPosition =1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_message);

    //    getMessage();
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
      //  int size = mFragmentList.size();
        News new1 = new News();
        new1.setNewsBody("11111111111111111111111");
        News new2 = new News();
        new2.setNewsBody("2222222222222222222222");
        News new3 = new News();
        new3.setNewsBody("333333333333333333333");
        News new4 = new News();
        News new5 = new News();
        News new6 = new News();
        mNewsMessageList.add(new1);
        mNewsMessageList.add(new2);
        mNewsMessageList.add(new3);
        mNewsMessageList.add(new4);
        mNewsMessageList.add(new5);
        mNewsMessageList.add(new6);
        int size = 6;
        for (int index = 0; index < size; index++) {
            Bundle bundle = new Bundle();
            NewsMessage fragment = new NewsMessage();
            bundle.putSerializable("message",mNewsMessageList.get(index));
            fragment.setArguments(bundle);
            mFragmentList.add(fragment);
        }
    }


}
