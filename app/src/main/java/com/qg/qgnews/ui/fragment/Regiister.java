package com.qg.qgnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qg.qgnews.R;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.ui.activity.LoginActivity;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by linzongzhan on 2017/7/29.
 */

public class Regiister extends Fragment {

    private View view;

    private EditText user;

    private EditText password;

    private EditText userName;

    private Button register;

    private TextView userState;

    private TextView passwordState;

    private ImageView userImage;

    private ImageView passwordImage;

    private ImageView userNameImage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_register,container,false);

        LoginActivity loginActivity = (LoginActivity) getActivity();
        loginActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        initView();
        viewOnClick();
        editViewOnClick();


        return view;
    }

    /**
     * 实例化控件
     */
    private void initView () {
        user = (EditText) view.findViewById(R.id.register_user);
        password = (EditText) view.findViewById(R.id.register_password);
        userName = (EditText) view.findViewById(R.id.register_userName);
        register = (Button) view.findViewById(R.id.register_register);

        userState = (TextView) view.findViewById(R.id.register_user_state);
        passwordState = (TextView) view.findViewById(R.id.register_password_state);
        userImage = (ImageView) view.findViewById(R.id.register_user_image);
        passwordImage = (ImageView) view.findViewById(R.id.register_password_image);
        userNameImage = (ImageView) view.findViewById(R.id.register_name_image);
    }

    /**
     * 设置点击事件
     */
    private void viewOnClick () {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getText().toString();
                if (!Tool.isEmail(email)) {
                    Tool.toast("邮箱格式不正确");
                } else {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            Manager manager = new Manager();
                            manager.setManagerAccount(user.getText().toString());
                            manager.setManagerPassword(password.getText().toString());
                            manager.setManagerName(userName.getText().toString());
                            String line = gson.toJson(manager);
                            String response = Request.RequestWithString("http://192.168.43.142:8080/admin/addaccount",line);
                            Gson gson1 = new Gson();
                            FeedBack feedBack = gson1.fromJson(response,FeedBack.class);
                            int state = feedBack.getState();
                            if (state == 1) {
                                //注册成功
                                Tool.toast("注册成功");
                            } else if (state == 5000) {
                                Tool.toast("服务器出错");
                            } else if (state == 2) {
                                Tool.toast("用户名存在");
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
                            }
                        }
                    }).start();


                }
            }
        });
    }

    /**
     * 设置编辑框监听
     */
    private void editViewOnClick () {
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String email;
                email = user.getText().toString();
                if (email.length() == 0) {
                    userState.setVisibility(View.GONE);
                    userImage.setVisibility(View.GONE);
                } else {
                    if (Tool.isEmail(email)) {
                        userState.setVisibility(View.GONE);
                        userImage.setVisibility(View.VISIBLE);
                        userImage.setImageResource(R.drawable.state_true);
                    } else {
                        userImage.setVisibility(View.GONE);
                        userState.setVisibility(View.VISIBLE);
                        userState.setText("邮箱格式错误");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text;
                text = password.getText().toString();
                if (text.length() == 0) {
                    passwordState.setVisibility(View.GONE);
                    passwordImage.setVisibility(View.GONE);
                } else {
                    if (text.length() < 6) {
                        passwordImage.setVisibility(View.GONE);
                        passwordState.setVisibility(View.VISIBLE);
                        passwordState.setText("输入的密码不能少于6位");
                    } else if (text.length() > 20) {
                        passwordImage.setVisibility(View.GONE);
                        passwordState.setVisibility(View.VISIBLE);
                        passwordState.setText("输入的密码不能多于20位");
                    } else {
                        passwordState.setVisibility(View.GONE);
                        passwordImage.setVisibility(View.VISIBLE);
                        passwordImage.setImageResource(R.drawable.state_true);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text;
                text = userName.getText().toString();
                if (text.length() == 0) {
                    userNameImage.setVisibility(View.GONE);
                } else {
                    userNameImage.setVisibility(View.VISIBLE);
                    userNameImage.setImageResource(R.drawable.state_true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


}
