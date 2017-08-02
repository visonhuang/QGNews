package com.qg.qgnews.model;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.Controller;
import com.qg.qgnews.ui.activity.NewsMessageActivity;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/30.
 */

public class NewsDetailAdapter extends PagerAdapter {
    private List<View> viewList;
    Context context;
    List<News> newsList;
    private int mode = NewsMessageActivity.MODE_VISIT;

    public NewsDetailAdapter(List<View> viewList, List<News> newsList, Context context, int mode) {
        this.viewList = viewList;
        this.context = context;
        this.newsList = newsList;
        this.mode = mode;
    }

    @Override
    public int getCount() {
        return viewList.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        // TODO Auto-generated method stub
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        if (position == viewList.size()) {
            position--;
        }
        if (position == -1) {
            position++;
        }
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View view = viewList.get(position);
        final RecyclerView viceFileList = (RecyclerView) view.findViewById(R.id.news_detial_vicefile_list);
        final TextView body = (TextView) view.findViewById(R.id.news_details_body);
        final TextView writer = (TextView) view.findViewById(R.id.news_detial_writer);
        container.addView(view);
        writer.setText("本文作者：" + newsList.get(position).getNewsAuthor());
        Controller.RequestNewsDetial(newsList.get(position).getNewsId(), new Controller.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                Gson gson = new Gson();
                final News news = gson.fromJson(json, News.class);
                Tool.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        body.setText(news.getNewsBody());
                        writer.setText("本文作者：" + news.getNewsAuthor());
                        viceFileList.setLayoutManager(new LinearLayoutManager(App.context));
                        viceFileList.setAdapter(new NewsDetialViecFileAdapter(context, news.getFileList(),mode));
                    }
                });
            }

            @Override
            public void onFailed(int state) {

            }
        });

        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
