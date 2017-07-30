package com.qg.qgnews.model;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.qg.qgnews.App;
import com.qg.qgnews.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/30.
 */

public class NewsDetailAdapter extends PagerAdapter {
    private List<View> viewList;
    Context context;

    public NewsDetailAdapter(List<View> viewList, Context context) {
        this.viewList = viewList;
        this.context = context;
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
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        RecyclerView viceFileList = (RecyclerView) view.findViewById(R.id.news_detial_vicefile_list);
        container.addView(view);
        viceFileList.setLayoutManager(new LinearLayoutManager(App.context));
        List<ViceFile> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ViceFile v = new ViceFile();
            v.setFileName("  附件" + i + ".mp3");
            list.add(v);
        }
        viceFileList.setAdapter(new NewsDetialViecFileAdapter(context, list));
        return view;
    }
}
