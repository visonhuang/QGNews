package com.qg.qgnews.ui.activity;

import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.util.Tool;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private SearchView searchView;
    private FloatingActionButton plus;
    private static final int PLUS_OPEN = 1;
    private static final int PLUS_CLOSE = 0;
    private static int pulsButtonMode = PLUS_CLOSE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        plus = (FloatingActionButton) findViewById(R.id.activity_main_plus_button);
        plus.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
        searchView = ((SearchView) menu.findItem(R.id.main_menu_search).getActionView());
        setSearchViewOptions(searchView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.main_menu_search:
                break;
            case R.id.main_menu_logout:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setSearchViewOptions(final SearchView s) {
        s.setQueryHint("搜索新闻...");
        //显示提交按钮
        s.setSubmitButtonEnabled(true);
        s.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Tool.toast("点击了搜索");
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
                    //TODO open
                } else {
                    plus.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_back_45));
                    //TODO close
                }
                pulsButtonMode = pulsButtonMode == PLUS_CLOSE ? PLUS_OPEN : PLUS_CLOSE;
                break;
        }
    }
}
