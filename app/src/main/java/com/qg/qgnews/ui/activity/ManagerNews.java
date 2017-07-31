package com.qg.qgnews.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.NewsListAdapter2;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.fragment.NewsListFrag;
import com.qg.qgnews.util.Request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerNews extends AppCompatActivity {

    int id;

    private FeedBack feedBack;

    private TextView title;

    private List<News> newsLists;

    private Gson gson = new Gson();

    public static final int GET = 1;

    private Handler handler = new Handler() {
        public void handleMessage (Message message) {
            switch (message.what) {
                case GET:
                    NewsListFrag fragment = new NewsListFrag();
                    fragment.setOnRefreshOrLoadIngListener(new NewsListFrag.OnRefreshOrLoadIngListener() {
                        @Override
                        public void onRefresh(NewsListAdapter2 adapter, List<News> oldList) {
                            oldList.clear();
                            for (int i = 0; i < newsLists.size(); i++) {
                                oldList.add(newsLists.get(i));
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onLoad(NewsListAdapter2 adapter, List<News> oldList) {

                        }
                    });
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_news);
        Toolbar toolbar = (Toolbar) findViewById(R.id.manager_news_toolbar);
        setSupportActionBar(toolbar);
        title = (TextView) findViewById(R.id.manager_news_title);
        title.setText("新闻列表");

        getMessage();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String,Integer> map = new HashMap<String, Integer>();
                map.put("managerId",id);
                String line = gson.toJson(map);

                String respose = Request.RequestWithString(RequestAdress.SHOWOWNNEWS,line);
                feedBack = gson.fromJson(respose,FeedBack.class);
                String data = feedBack.getData();
                newsLists = gson.fromJson(data,new TypeToken<List<News>>(){}.getType());

                Message message = new Message();
                message.what = GET;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.manager_news_frameLayout,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getMessage () {
        Intent intent = getIntent();
        id = intent.getIntExtra("id",0);
    }
}
