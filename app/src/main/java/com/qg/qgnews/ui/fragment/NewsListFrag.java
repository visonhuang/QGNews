package com.qg.qgnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.NewsListAdapter;
import com.qg.qgnews.model.News;

import java.util.ArrayList;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class NewsListFrag extends Fragment implements NewsListAdapter.OnItemClickListener {
    private RecyclerView newList;
    private NewsListAdapter adapter;

    public interface OnNewsItemClickListener {
        void OnItemClickListener(View v, int pos, News news);
    }

    private OnNewsItemClickListener onNewsItemClickListener = null;

    public void setOnNewsItemClickListener(OnNewsItemClickListener onNewsItemClickListener) {
        this.onNewsItemClickListener = onNewsItemClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.frag_news_list, container, false);
        adapter = new NewsListAdapter(new ArrayList<News>(),getContext());
        adapter.setOnItemClickListener(this);
        newList = (RecyclerView) content.findViewById(R.id.news_list);
        newList.setLayoutManager(new LinearLayoutManager(getContext()));
        newList.setAdapter(adapter);
        return content;
    }

    @Override
    public void OnItemClick(View v, int pos, News news) {
        if (onNewsItemClickListener != null) {
            onNewsItemClickListener.OnItemClickListener(v, pos, news);
        }
    }
}
