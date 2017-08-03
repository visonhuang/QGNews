package com.qg.qgnews.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.Controller;
import com.qg.qgnews.controller.adapter.DownloadDetialAdapter;
import com.qg.qgnews.controller.adapter.NewsListAdapter2;
import com.qg.qgnews.model.DownloadDetial;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.fragment.NewsListFrag;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerNews extends AppCompatActivity implements NewsListFrag.OnRefreshOrLoadIngListener, NewsListFrag.OnNewsItemClickListener, NewsListFrag.OnNewsItemLongClickListener {

    private TextView title;
    NewsListFrag frag;
    private int managerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        managerId = getIntent().getIntExtra("id", 0);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_news);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.manager_news_toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.manager_news_title);
        title.setText("新闻列表");
        frag = new NewsListFrag();
        frag.setOnRefreshOrLoadIngListener(this);
        frag.setOnNewsItemClickListener(this);
        frag.setOnNewsItemLongClickListener(this);
        //显示newslistFrag
        replaceFragment(frag);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.manager_news_frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onRefresh(final NewsListAdapter2 adapter, final List<News> oldList) {
        Controller.RequestWithString2(RequestAdress.SHOW_OWN_NEWS, "{\"managerId\":" + managerId + "}", new Controller.OnRequestListener() {
            @Override
            public void onSuccess(String json) {
                Log.d("我的新闻刷新返回", json);
                oldList.clear();
                oldList.addAll(0, new Gson().fromJson(json, new TypeToken<List<News>>() {
                }.getType()));
                Tool.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (oldList.size() == 0){
                            frag.error.setImageResource(R.drawable.loading_failed);
                            frag.error.setVisibility(View.VISIBLE);
                        } else {
                            frag.error.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onFailed(int state) {
                oldList.clear();
                Tool.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frag.error.setImageResource(R.drawable.loading_failed);
                        frag.error.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                    }
                });
                Tool.toast("刷新失败");
            }
        });
    }

    @Override
    public void onLoad(final NewsListAdapter2 adapter, final List<News> oldList) {
        Tool.toast("没有更多了");
    }

    @Override
    public void OnItemClickListener(View v, int pos, News news) {
        Intent intent = new Intent(this, NewsMessageActivity.class);
        intent.putExtra("news_list", (Serializable) frag.dataNews);
        intent.putExtra("start_pos", pos);
        intent.putExtra("mode", NewsMessageActivity.MODE_MANAGE);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        frag.onRefresh();
    }

    @Override
    public void OnItemLongClick(View v, int pos, News news, NewsListAdapter2 adapter) {
        showDeleteDialog(news);
    }

    private void showDeleteDialog(News news) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(App.getActivityStack().lastElement());
        dialog.setTitle("删除新闻");
        dialog.setCancelable(true);
        dialog.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Controller.RequestWithString2(RequestAdress.DELETE_NEWS, "{\"newsId\":" + news.getNewsId() + "}", new Controller.OnRequestListener() {
                    @Override
                    public void onSuccess(String json) {
                        Tool.toast("删除成功");
                        Tool.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                frag.onRefresh();
                            }
                        });
                    }
                    @Override
                    public void onFailed(int state) {
                        Tool.toast("删除失败");
                    }
                });
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
