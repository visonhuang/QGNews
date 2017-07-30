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
import android.view.View;
import android.widget.ImageView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.FragmentPagerAdapterNewsMessage;
import com.qg.qgnews.model.News;
import com.qg.qgnews.ui.fragment.NewsMessage;

import java.util.ArrayList;
import java.util.List;

public class NewsMessageActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    ImageView cover;
    CollapsingToolbarLayout coll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_message);
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
    }


}
