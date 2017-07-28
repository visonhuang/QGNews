package com.qg.qgnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qg.qgnews.R;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.util.Tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by linzongzhan on 2017/7/28.
 */

public class Login extends Fragment {

    private EditText user;

    private EditText password;

    private Button login;

    private Button register;

    private TextView forgetPassword;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.login_fragment,container,false);



        return view;
    }

    /**
     * 实例化控件
     */
    private void initView () {
        user = (EditText) view.findViewById(R.id.user);
        password = (EditText) view.findViewById(R.id.password);
        login = (Button) view.findViewById(R.id.login);
        register = (Button) view.findViewById(R.id.register);
        forgetPassword = (TextView) view.findViewById(R.id.forget_password);
    }

    /**
     * 设置点击事件
     */
    private void viewOnclick () {

        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getText().toString();
                if (!Tool.isEmail(email)) {
                    Tool.toast("邮箱格式不正确");
                } else {
                    HttpURLConnection connection = null;
                    BufferedReader reader = null;
                    try {
                        URL url = new URL("http://ip:80/manager/login");
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setDoInput(true);
                        connection.setDoOutput(true);
                        connection.connect();
                        //请求参数


                        InputStream inputStream = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder build = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            build.append(line);
                        }
                        String response = build.toString();

                        Gson gson = new Gson();
                        FeedBack feedBack = gson.fromJson(reader,FeedBack.class);
                        int state = feedBack.getState();
                        if (state == 1) {
                            //进入主界面
                        } else if (state == 3) {
                            Tool.toast("邮箱不存在");
                        } else if (state == 4) {
                            Tool.toast("邮箱为空");
                        } else if (state == 5) {
                            Tool.toast("密码为空");
                        } else if (state == 6) {
                            Tool.toast("邮箱格式不正确");
                        } else if (state == 8) {
                            Tool.toast("密码错误");
                        } else if (state == 9) {
                            Tool.toast("账户未激活");
                        } else if (state == 10) {
                            Tool.toast("账户未审批");
                        } else if (state == 11) {
                            Tool.toast("账户被封了");
                        } else if (state == 5000) {
                            Tool.toast("服务器异常");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Tool.toast("连接异常");
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (connection != null) {
                            connection.disconnect();
                        }
                    }
                }
            }
        });

        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //忘记密码
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
