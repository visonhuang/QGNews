package com.qg.qgnews.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qg.qgnews.R;

import java.util.ArrayList;
import java.util.List;

public class NewsMessageActivity extends AppCompatActivity {

    //存储新闻信息的集合,注：在这个集合的泛型是新闻信息的一个类
    //里边的泛型还需修改（重要）
    private List<String> mNewsMessageList = new ArrayList<>();

    //存储初始化后的Fragment
    private List<Fragment> mFragmentList = new ArrayList<>();

    //标识进入了哪一条新闻
    private int mPosition;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_message);


    }



    /**
     * 初始化活动中的控件
     */
    private void initView () {
        mViewPager = (ViewPager) findViewById(R.id.news_viewpager);
    }
}
