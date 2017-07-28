package com.qg.qgnews.ui.activity;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.UrlListAdapter;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.util.Arrays;

public class FileSelector extends TopBarBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    ImageView ok;
    ListView urlList;
    private int level = 0;
    TextView where;
    File now;


    @Override
    protected int getContentView() {
        return R.layout.activity_file_selector;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }//申请权限
        setTitle("文件选择器");
        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
        setTopRightButton("确认", R.drawable.selector_ok, new OnClickListener() {
            @Override
            public void onClick() {
                Tool.toast("点击了确定");
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
        if (file == null) {
            Tool.toast("asdasdasdsaasdsdasdasdasdadsasdasdasd");
        }
        LayoutAnimationController lac = new LayoutAnimationController(AnimationUtils.loadAnimation(this, R.anim.item_in));
        lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
        urlList.setLayoutAnimation(lac);
        UrlListAdapter adapter = new UrlListAdapter(this, R.layout.url_tiem, Arrays.asList(file.listFiles()));
        urlList.setAdapter(adapter);
        where.setText(now.getPath().replaceAll("/", " > ").replace("> storage > emulated > 0", "内部储存器"));
    }

    @Override
    public void onBackPressed() {
        if (level == 0) {
            super.onBackPressed();
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

            } else {
                finish();
            }
        }
    }

}