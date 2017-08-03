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
import android.widget.ImageView;
import android.widget.ListView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.Controller;
import com.qg.qgnews.controller.adapter.NewsListAdapter;
import com.qg.qgnews.controller.adapter.NewsListAdapter2;
import com.qg.qgnews.model.News;
import com.qg.qgnews.ui.diyview.RefreshLayout;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class NewsListFrag extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, RefreshLayout.OnLoadListener, AdapterView.OnItemLongClickListener {
    private ListView newList;
    private NewsListAdapter2 adapter;
    private RefreshLayout refreshLayout;
    public ImageView error;
    public List<News> dataNews = new ArrayList<>();

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (adapter.getCount() <= position) {
            return;
        }
        if (onNewsItemClickListener != null) {

            onNewsItemClickListener.OnItemClickListener(view, position, adapter.getItem(position));
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (onNewsItemLongClickListener != null) {
            onNewsItemLongClickListener.OnItemLongClick(view,position,dataNews.get(position),adapter);
        }
        return true;
    }


    public interface OnNewsItemClickListener {
        void OnItemClickListener(View v, int pos, News news);
    }
    public interface OnNewsItemLongClickListener {
        void OnItemLongClick(View v, int pos, News news,NewsListAdapter2 adapter);
    }


    public interface OnRefreshOrLoadIngListener {
        void onRefresh(NewsListAdapter2 adapter, List<News> oldList);
        void onLoad(NewsListAdapter2 adapter, List<News> oldList);
    }

    private OnNewsItemClickListener onNewsItemClickListener = null;
    private OnNewsItemLongClickListener onNewsItemLongClickListener = null;
    private OnRefreshOrLoadIngListener onRefreshOrLoadIngListener = null;

    public void setOnNewsItemLongClickListener(OnNewsItemLongClickListener onNewsItemLongClickListener) {
        this.onNewsItemLongClickListener = onNewsItemLongClickListener;
    }

    public void setOnRefreshOrLoadIngListener(OnRefreshOrLoadIngListener onRefreshOrLoadIngListener) {
        this.onRefreshOrLoadIngListener = onRefreshOrLoadIngListener;
    }

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
        error = (ImageView) content.findViewById(R.id.news_list_error);
        newList = (ListView) content.findViewById(R.id.news_list);
        refreshLayout = (RefreshLayout) content.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadListener(this);
        adapter = new NewsListAdapter2(getContext(), R.layout.news_item, dataNews);
        newList.setOnItemClickListener(NewsListFrag.this);
        newList.setOnItemLongClickListener(this);
        newList.setAdapter(adapter);
        //onRefresh();

    }


    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        //刷新接口丢出去
        if (onRefreshOrLoadIngListener != null) {
            onRefreshOrLoadIngListener.onRefresh(adapter, dataNews);
            refreshLayout.setRefreshing(false);
        }

    }

    @Override
    public void onLoad() {
        if (onRefreshOrLoadIngListener != null) {
            onRefreshOrLoadIngListener.onLoad(adapter, dataNews);
            refreshLayout.setLoading(false);
        }
    }
}
