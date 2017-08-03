package com.qg.qgnews.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.Controller;
import com.qg.qgnews.controller.adapter.HeartBeatService;
import com.qg.qgnews.controller.adapter.NewsListAdapter2;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.fragment.NewsListFrag;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NewsListFrag.OnNewsItemClickListener, NewsListFrag.OnRefreshOrLoadIngListener {
    private Toolbar toolbar;
    private SearchView searchView;
    private FloatingActionButton plus, myNews, manager, edit;
    private LinearLayout myNewsLiner, managerLiner, editLiner;
    private static final int PLUS_OPEN = 1;
    private static final int PLUS_CLOSE = 0;
    private static int pulsButtonMode = PLUS_CLOSE;
    public static final int MODE_VISITOR = 0;
    public static final int MODE_MANAGER = 1;
    public static final int MODE_SUPPER_MANAGER = 2;
    public static int mode = MODE_SUPPER_MANAGER;
    private NewsListFrag newsListFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mode = getIntent().getIntExtra("visit_mode", MODE_VISITOR);
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        newsListFrag = new NewsListFrag();
        plus = (FloatingActionButton) findViewById(R.id.activity_main_plus_button);
        myNews = (FloatingActionButton) findViewById(R.id.activity_main_mynews_button);
        manager = (FloatingActionButton) findViewById(R.id.activity_main_manager_button);
        edit = (FloatingActionButton) findViewById(R.id.activity_main_edit_button);
        myNewsLiner = (LinearLayout) findViewById(R.id.activity_main_mynews_liner);
        managerLiner = (LinearLayout) findViewById(R.id.activity_main_manager_liner);
        editLiner = (LinearLayout) findViewById(R.id.activity_main_edit_liner);

        newsListFrag.setOnNewsItemClickListener(this);
        newsListFrag.setOnRefreshOrLoadIngListener(this);


        plus.setOnClickListener(this);
        myNews.setOnClickListener(this);
        manager.setOnClickListener(this);
        edit.setOnClickListener(this);
        if (mode == MODE_VISITOR) {
            plus.setVisibility(View.GONE);
        } else {
            plus.setVisibility(View.VISIBLE);
        }
        setFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.main_menu_logout);
        if (App.isManager) {
            item.setTitle("退出登录");
        } else {
            item.setTitle("我要登陆");
        }
        searchView = ((SearchView) menu.findItem(R.id.main_menu_search).getActionView());
        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.main_menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                isSearch = false;
                newsListFrag.onRefresh();
                return true;
            }
        });
        setSearchViewOptions(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_search:
                break;
            case R.id.main_menu_logout:
                //管理员注销，停止心跳包，并发送注销请求
                if (App.isManager) {
                    System.out.println("停止发送心跳包");
                    stopService(new Intent(this, HeartBeatService.class));
                }
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;

            case R.id.main_menu_select_download_path:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return super.onOptionsItemSelected(item);
                }//申请权限
                Intent intent = new Intent(this, FileSelector.class);
                intent.putExtra("mode", FileSelector.MODE_PATH);
                startActivity(new Intent(this, FileSelector.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean isSearch = false;
    String searchContent = "";

    private void setSearchViewOptions(final SearchView s) {
        s.setQueryHint("搜索新闻...");
        //显示提交按钮
        s.setSubmitButtonEnabled(true);
        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchContent = query;
                //清缓存
                //该标识
                isSearch = true;
                //触发刷新
                newsListFrag.onRefresh();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        //限制输入字符长度
        TextView et = (TextView) s.findViewById(R.id.search_src_text);
        et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_main_plus_button:
                if (pulsButtonMode == PLUS_CLOSE) {
                    plus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_to_45));
                    showButtons();
                    //TODO open
                } else {
                    plus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_back_45));
                    hideButtons();
                    //TODO close
                }
                pulsButtonMode = pulsButtonMode == PLUS_CLOSE ? PLUS_OPEN : PLUS_CLOSE;
                break;
            case R.id.activity_main_edit_button:
                Intent intent = new Intent(this, PublishNewsActivity.class);
                startActivityForResult(intent, 1);
                break;
            case R.id.activity_main_manager_button:
                Intent intent1 = new Intent(this, ManagerActivity.class);
                startActivity(intent1);
                break;
            case R.id.activity_main_mynews_button:
                Intent intent2 = new Intent(this, ManagerNews.class);
                intent2.putExtra("id", Tool.getCurrentManager().getManagerId());
                startActivity(intent2);
                break;
        }
    }

    private void showButtons() {
        switch (mode) {
            case MODE_VISITOR:
                break;
            case MODE_MANAGER:
                myNewsLiner.setVisibility(View.VISIBLE);
                editLiner.setVisibility(View.VISIBLE);
                myNewsLiner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_50));
                editLiner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_0));
                break;
            case MODE_SUPPER_MANAGER:
                managerLiner.setVisibility(View.VISIBLE);
                myNewsLiner.setVisibility(View.VISIBLE);
                editLiner.setVisibility(View.VISIBLE);
                myNewsLiner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_50));
                managerLiner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_100));
                editLiner.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_0));
                break;
        }


    }

    private void hideButtons() {
        Animation hide_0, hide_50, hide_100;
        hide_0 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_0);
        hide_50 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_50);
        hide_100 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_100);

        hide_0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                editLiner.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hide_50.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                myNewsLiner.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hide_100.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                managerLiner.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        switch (mode) {
            case MODE_VISITOR:
                break;
            case MODE_MANAGER:
                myNewsLiner.startAnimation(hide_50);
                editLiner.startAnimation(hide_0);
                break;
            case MODE_SUPPER_MANAGER:
                myNewsLiner.startAnimation(hide_50);
                managerLiner.startAnimation(hide_100);
                editLiner.startAnimation(hide_0);
                break;
        }


    }

    private void setFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_frag_container, newsListFrag);
        ft.commit();
    }

    /**
     * 新闻列表item点击监听
     *
     * @param v
     * @param pos
     * @param news
     */
    @Override
    public void OnItemClickListener(View v, int pos, News news) {
        Intent intent = new Intent(this, NewsMessageActivity.class);
        intent.putExtra("news_list", (Serializable) newsListFrag.dataNews);
        intent.putExtra("start_pos", pos);
        intent.putExtra("mode", NewsMessageActivity.MODE_VISIT);
        startActivity(intent);
    }

    @Override
    public void onRefresh(final NewsListAdapter2 adapter, final List<News> oldList) {
        if (isSearch) {
            Controller.RequestWithString2(RequestAdress.SEARCH_NEWS, "{\"newsTitle\":\"" + searchContent + "\"}", new Controller.OnRequestListener() {
                @Override
                public void onSuccess(String json) {
                    oldList.clear();
                    oldList.addAll(0, new Gson().fromJson(json, new TypeToken<List<News>>() {
                    }.getType()));
                    Tool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newsListFrag.error.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onFailed(int state) {
                    Tool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            newsListFrag.error.setImageResource(R.drawable.search_failed);
                            newsListFrag.error.setVisibility(View.VISIBLE);
                            oldList.clear();
                            adapter.notifyDataSetChanged();
                        }
                    });

                }
            });
        } else {
            Controller.getInstance().RequestNews(new Controller.OnRequestNewsListener() {
                @Override
                public void onSuccess(final List<News> list) {

                    oldList.clear();
                    oldList.addAll(0, list);
                    Tool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (list.size() == 0){
                                newsListFrag.error.setImageResource(R.drawable.loading_failed);
                                newsListFrag.error.setVisibility(View.VISIBLE);
                            } else {
                                newsListFrag.error.setVisibility(View.GONE);
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
                            newsListFrag.error.setImageResource(R.drawable.loading_failed);
                            newsListFrag.error.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    Tool.toast("刷新失败");
                }
            });
            isSearch = false;
        }

    }

    @Override
    public void onLoad(final NewsListAdapter2 adapter, final List<News> oldList) {
        try {
            final Gson gson = new Gson();
            Controller.RequestWithString2(RequestAdress.GET_NEWS_BEHIDE, gson.toJson(oldList.get(oldList.size() - 1)), new Controller.OnRequestListener() {
                @Override
                public void onSuccess(String json) {
                    Tool.toast("加载完毕");
                    oldList.addAll((Collection<? extends News>) gson.fromJson(json, new TypeToken<List<News>>() {
                    }.getType()));
                    Tool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

                @Override
                public void onFailed(int state) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    long start = 0;

    @Override
    public void onBackPressed() {

        if (pulsButtonMode == PLUS_OPEN) {
            onClick(plus);
        } else if (System.currentTimeMillis() - start > 2000) {
            Tool.toast("再按一次退出QGNEWS");
            start = System.currentTimeMillis();
        } else {
            for (Activity activity : App.getActivityStack()) {
                activity.finish();
            }
        }
    }

    /**
     * 权限请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(this, FileSelector.class);
                intent.putExtra("mode", FileSelector.MODE_PATH);
                startActivity(new Intent(this, FileSelector.class));
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (App.isManager) {
            Controller.RequestWithString2(RequestAdress.LOGOUT, "{\"managerId\":" + Tool.getCurrentManager().getManagerId() + "}", new Controller.OnRequestListener() {
                @Override
                public void onSuccess(String json) {

                }

                @Override
                public void onFailed(int state) {

                }
            });
        }
        App.isManager = false;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        newsListFrag.onRefresh();
    }
}
