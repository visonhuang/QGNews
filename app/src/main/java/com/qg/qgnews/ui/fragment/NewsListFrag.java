package com.qg.qgnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentController;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.NewsListAdapter;
import com.qg.qgnews.controller.adapter.NewsListAdapter2;
import com.qg.qgnews.controller.controller;
import com.qg.qgnews.model.News;
import com.qg.qgnews.ui.diyview.RefreshLayout;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class NewsListFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener {
    private ListView newList;
    private NewsListAdapter2 adapter;
    private RefreshLayout refreshLayout;
    private List<News> dataNews = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (adapter.getCount() <= position) {
            return;
        }
        //onNewsItemClickListener.OnItemClickListener(view, position, adapter.getItem(position));
    }


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
        initView(content);
        return content;
    }

    private void initView(View content) {
        newList = (ListView) content.findViewById(R.id.news_list);
        refreshLayout = (RefreshLayout) content.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new NewsListAdapter2(getContext(), R.layout.news_item, dataNews);
        newList.setOnItemClickListener(NewsListFrag.this);
        newList.setAdapter(adapter);
        onRefresh();

    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        controller.getInstance().RequestNews(0, new controller.OnRequestNewsListener() {
            @Override
            public void onSuccess(final List<News> list) {
                Tool.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataNews.addAll(0, list);
                        adapter.notifyDataSetChanged();
                        refreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onFailed(int state, String stateInfo) {

            }
        });
    }

    @Override
    public void onLoad() {
        Tool.toast("上啦加载");
        controller.getInstance().RequestNews(0, new controller.OnRequestNewsListener() {
            @Override
            public void onSuccess(List<News> list) {
                dataNews.addAll(list);
                Tool.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        refreshLayout.setLoading(false);

                    }
                });
            }

            @Override
            public void onFailed(int state, String stateInfo) {

            }
        });

    }
}
