package com.qg.qgnews.ui.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.UrlListAdapter;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileSelector extends TopBarBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, UrlListAdapter.OnSelectedFilesChangedListener {
    ImageView ok;
    ListView urlList;
    private int level = 0;
    TextView where;
    File now;
    public static String KEY_MODE = "mode";
    public static String KEY_MAX = "maxSelected";
    public static final int MODE_PATH = 0;
    public static final int MODE_FILE = 1;
    private int mode = MODE_FILE;
    private UrlListAdapter adapter;
    Map<String, File> selectedFiles = new HashMap<>();
    public int maxSelected = 10;

    @Override
    protected int getContentView() {
        return R.layout.activity_file_selector;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        Intent intent = getIntent();
        mode = intent.getIntExtra(KEY_MODE, MODE_PATH);
        maxSelected = intent.getIntExtra(KEY_MAX, 0);

        switch (mode) {
            case MODE_FILE:
                setTitle("选择上传文件0/" + maxSelected);
                setTopRightButton("确认", R.drawable.selector_ok, new OnClickListener() {
                    @Override
                    public void onClick() {
                        Intent intent = new Intent();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("map_key", (Serializable) selectedFiles);
                        intent.putExtra("map_key", bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        //TODO 选择文件逻辑
                    }
                });
                break;
            case MODE_PATH:
                setTitle("选择文件保存路径");
                setTopRightButton("确认", R.drawable.selector_ok, new OnClickListener() {
                    @Override
                    public void onClick() {
                        Tool.setFileSavePath(now.getPath());
                        Tool.toast("路径已保存");
                        finish();
                    }
                });
                break;
        }

        //返回键监听
        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("map_key", (Serializable) selectedFiles);
                intent.putExtra("map_key", bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        urlList = (ListView) findViewById(R.id.url_list);
        where = (TextView) findViewById(R.id.where);
        urlList.setOnItemClickListener(this);
        now = new File(Environment.getExternalStorageDirectory().getPath());
        setAdapter(now);
    }


    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = getSharedPreferences("PathData", MODE_PRIVATE).edit();
        editor.putString("path", now.getPath());
        editor.apply();
        finish();
    }


    private void setAdapter(File file) {
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.item_in));
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        urlList.setLayoutAnimation(lac);
        adapter = new UrlListAdapter(this, R.layout.url_tiem, Arrays.asList(file.listFiles()), mode, selectedFiles, maxSelected);
        adapter.setOnSelectedFilesChangedListener(this);
        urlList.setAdapter(adapter);
        where.setText(now.getPath().replaceAll("/", " > ").replace("> storage > emulated > 0", "内部储存器"));
    }

    @Override
    public void onBackPressed() {
        if (level == 0) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("map_key", (Serializable) selectedFiles);
            intent.putExtra("map_key", bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            level--;
            now = now.getParentFile();
            setAdapter(now);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.on_click);
        view.setAnimation(animation);
        if (now.listFiles()[position].isDirectory()) {
            level++;
            now = now.listFiles()[position];
            setAdapter(now);
        } else if (mode == MODE_FILE) {
            CheckBox checkBox = (CheckBox) view.findViewById(R.id.url_item_checkBox);
            checkBox.setChecked(!checkBox.isChecked());

            if (checkBox.isChecked()) {
                if (selectedFiles.size() >= maxSelected) {
                    checkBox.setChecked(false);
                    Tool.toast("已达到上限");
                    return;
                }
                selectedFiles.put(now.listFiles()[position].getPath(), now.listFiles()[position]);
            } else {
                selectedFiles.remove(now.listFiles()[position].getPath());
            }
            setTitle("选择上传文件" + selectedFiles.size() + "/" + maxSelected);
        }

    }


    @Override
    public void onChanged() {
        setTitle("选择上传文件" + selectedFiles.size() + "/" + maxSelected);
    }
}