package com.qg.qgnews.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.FragmentPagerAdapterNewsMessage;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.NewsDetailAdapter;
import com.qg.qgnews.ui.fragment.NewsMessage;

import java.util.ArrayList;
import java.util.List;

public class NewsMessageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {
    Toolbar toolbar;
    ViewPager viewPager;
    ImageView cover;
    CollapsingToolbarLayout coll;
    List<News> newsList;
    List<View> viewList;
    int startPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_message);
        Intent intent = getIntent();
        newsList = (List<News>) intent.getSerializableExtra("news_list");
        startPos = intent.getIntExtra("start_pos", 0);
        initView();
    }

    /**
     * 初始化活动中的控件
     */
    private void initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        viewPager = (ViewPager) findViewById(R.id.activity_news_message_view_pager);
        toolbar = (Toolbar) findViewById(R.id.activity_news_message_tool_bar);
        coll = (CollapsingToolbarLayout) findViewById(R.id.activity_news_message_coll);
        cover = (ImageView) findViewById(R.id.activity_news_message_news_cover);
        setSupportActionBar(toolbar);
        coll.setExpandedTitleColor(Color.WHITE);
        coll.setCollapsedTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initViewPager();
    }

    private void initViewPager() {
        viewList = new ArrayList<>();
        for (int i = 0; i < newsList.size(); i++) {
            viewList.add(LayoutInflater.from(this).inflate(R.layout.news_details, null, false));
        }
        viewPager.setAdapter(new NewsDetailAdapter(viewList,this));
        viewPager.setCurrentItem(startPos, true);
        viewPager.setOnPageChangeListener(this);
    }


    //viewpager华东监听
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position % 2 == 0) {
            cover.setImageResource(R.drawable.example);
        } else {
            cover.setImageResource(R.drawable.examp2);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //viewpager华东监听
}
