package com.qg.qgnews.model;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
    List<News> newsList;

    public NewsDetailAdapter(List<View> viewList, List<News> newsList,Context context) {
        this.viewList = viewList;
        this.context = context;
        this.newsList = newsList;
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
        // TODO Auto-generated method stub
        container.removeView(viewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = viewList.get(position);
        RecyclerView viceFileList = (RecyclerView) view.findViewById(R.id.news_detial_vicefile_list);
        TextView body = (TextView) view.findViewById(R.id.news_details_body);
        container.addView(view);
        body.setText("wowowowo");
        viceFileList.setLayoutManager(new LinearLayoutManager(App.context));
        List<ViceFile> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ViceFile v = new ViceFile();
            v.setFilePath("http://fs.web.kugou.com/bec06d0d1a493bc098c44470793dea39/597deee7/G009/M02/06/15/SQ0DAFUJf-KAa_LnAD3gDgbvu7o702.mp3");
            v.setFileName("附件" + i + ".mp3");
            list.add(v);
        }
        viceFileList.setAdapter(new NewsDetialViecFileAdapter(context, list));
        return view;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
