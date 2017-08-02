package com.qg.qgnews.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.MyFragmentPagerAdapter;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.model.Status;
import com.qg.qgnews.ui.fragment.ManagerPerson;
import com.qg.qgnews.ui.fragment.ManagerPersonE;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {

    private TabLayout tabLayout;

    private ViewPager viewPager;

    private List<Fragment> fragmentList = new ArrayList<>();

    private List<String> titleList = new ArrayList<>();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.manager_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        tabLayout = (TabLayout) findViewById(R.id.manager_tabLayout);
        viewPager = (ViewPager) findViewById(R.id.manager_viewPager);
        fragmentList.add(new ManagerPerson());
        fragmentList.add(new ManagerPersonE());
        titleList.add("管理员列表");
        titleList.add("审批中");

        MyFragmentPagerAdapter myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList,titleList);
        viewPager.setAdapter(myFragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manager,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.manager_menu_add:
                //添加管理员
                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerActivity.this);
                builder.setTitle("添加管理员");
                View view = LayoutInflater.from(ManagerActivity.this).inflate(R.layout.add_manager_layout,null);
                final EditText addManagerAccount = (EditText) view.findViewById(R.id.add_manager_account);
                final EditText addManagerPassword = (EditText) view.findViewById(R.id.add_manager_password);
                final EditText addManagerName = (EditText) view.findViewById(R.id.add_manager_name);
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (addManagerAccount.getText().toString().equals("")) {
                            Tool.toast("管理员账户不能为空");
                        } else if (addManagerPassword.getText().toString().equals("")) {
                            Tool.toast("管理员密码不能为空");
                        } else if (addManagerName.getText().toString().equals("")) {
                            Tool.toast("管理员姓名不能为空");
                        } else {
                            if (!Tool.isEmail(addManagerAccount.getText().toString())) {
                                Tool.toast("管理员账户格式错误");
                            } else {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Gson gson = new Gson();
                                        Manager manager = new Manager();
                                        manager.setManagerAccount(addManagerAccount.getText().toString());
                                        manager.setManagerPassword(addManagerPassword.getText().toString());
                                        manager.setManagerName(addManagerName.getText().toString());
                                        String line = gson.toJson(manager);
                                        String response = Request.RequestWithString(RequestAdress.ADDMANAGER,line);
                                        FeedBack feedBack = gson.fromJson(response,FeedBack.class);
                                        int status = feedBack.getState();
                                        if (status == 1) {
                                            Tool.toast("添加管理员成功");
                                        }
                                        Status.statusResponse(status);
                                    }
                                }).start();

                            }
                        }
                    }
                }).show();
                break;
//            case R.id.manager_menu_check:
//                //查看附件下载
//                Intent intent = new Intent(ManagerActivity.this,ManagerFileListActivity.class);
//                startActivity(intent);
//                break;
            default:
        }
        return true;
    }
}
