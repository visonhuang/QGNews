package com.qg.qgnews.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.Controller;
import com.qg.qgnews.controller.adapter.HeartBeatService;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Manager;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.activity.LoginActivity;
import com.qg.qgnews.ui.activity.MainActivity;
import com.qg.qgnews.util.Request;
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

    private static final String TAG = "Login";

    private EditText user;

    private EditText password;

    private Button login;

    private Button register;

    private TextView forgetPassword;

    private View view;

    private TextView userState;

    private TextView passwordState;

    private ImageView userImage;

    private ImageView passwordImage;

    private TextView vistor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_login, container, false);

        initView();
        viewOnclick();
        editViewOnclick();

        login.setClickable(false);
        login.setBackgroundColor(Color.parseColor("#d6d7d7"));

        return view;
    }

    /**
     * 实例化控件
     */
    private void initView() {



        ImageView earth = (ImageView) view.findViewById(R.id.earth_image);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.route_360);
        animation.setInterpolator(new LinearInterpolator());
        earth.startAnimation(animation);
        user = (EditText) view.findViewById(R.id.user);
        password = (EditText) view.findViewById(R.id.password);
        login = (Button) view.findViewById(R.id.login);
        register = (Button) view.findViewById(R.id.register);
        forgetPassword = (TextView) view.findViewById(R.id.forget_password);
        vistor = (TextView) view.findViewById(R.id.visitor);
        userState = (TextView) view.findViewById(R.id.login_user_state);
        passwordState = (TextView) view.findViewById(R.id.login_password_state);
        userImage = (ImageView) view.findViewById(R.id.user_image);
        passwordImage = (ImageView) view.findViewById(R.id.password_image);
    }

    /**
     * 设置点击事件
     */
    private void viewOnclick() {

        //登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getText().toString();
                String passwordText = password.getText().toString();
                if (!Tool.isEmail(email)) {
                    Log.d(TAG, "" + email);
                    Tool.toast("邮箱格式不正确");
                } else if (passwordText.equals("")) {
                    Tool.toast("密码不能为空");
                } else {
                    //请求参数
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            Manager manager = new Manager();
                            manager.setManagerAccount(user.getText().toString());
                            manager.setManagerPassword(Tool.encryption(password.getText().toString()));
                            String line = gson.toJson(manager);
                            Gson gson1 = new Gson();
                            FeedBack feedBack = Request.RequestWithString2(RequestAdress.LOGIN, line);
                            if (feedBack == null) {
                                Tool.toast("服务器无返回");
                            } else {
                                int state = feedBack.getState();
                                if (state == 1) {
                                    App.isManager = true;
                                    //开始心跳
                                    getActivity().startService(new Intent(getActivity(), HeartBeatService.class));
                                    //进入主界面
                                    Manager message = gson1.fromJson(feedBack.getData(), Manager.class);
                                    Log.d("登陆===========", message.toString());
                                    Tool.setCurrentManager(message);
                                    Intent intent = new Intent(getContext(), MainActivity.class);
                                    if (message.getManagerSuper() == 1) {
                                        intent.putExtra("visit_mode", MainActivity.MODE_SUPPER_MANAGER);
                                    } else if (message.getManagerSuper() == 0) {
                                        intent.putExtra("visit_mode", MainActivity.MODE_MANAGER);
                                    }
                                    startActivity(intent);
                                    LoginActivity loginActivity = (LoginActivity) getActivity();
                                          loginActivity.finish();
                                    Tool.toast("登录成功");
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
                                } else if (state == 17) {
                                    Tool.toast("账户已登录，不能重复登录");
                                }
                            }

                        }
                    }).start();
                }
            }
        });

        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.replaceFragment(new Regiister(), "注册");
                LoginActivity.mode = LoginActivity.REGISTER;
            }
        });

        //忘记密码
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginActivity loginActivity = (LoginActivity) getActivity();
                loginActivity.replaceFragment(new ForgetPassword(), "忘记密码");
                LoginActivity.mode = LoginActivity.FORGETPASSWORD;
            }
        });

        vistor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Controller.RequestWithString2(RequestAdress.REQUEST_NEWS, "{\"visitorUUID\":" + Tool.saveRandomUUID() + "}", new Controller.OnRequestListener() {
                    @Override
                    public void onSuccess(String json) {

                    }

                    @Override
                    public void onFailed(int state) {

                    }
                });
                App.isManager = false;
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.putExtra("visit_mode", MainActivity.MODE_VISITOR);
                startActivity(intent);
                LoginActivity loginActivity = (LoginActivity) getActivity();
                 loginActivity.finish();
            }
        });
    }

    /**
     * 编辑框监听
     */
    private void editViewOnclick() {
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text;
                text = user.getText().toString();
                if (text.length() == 0) {
                    userState.setVisibility(View.GONE);
                    userImage.setVisibility(View.GONE);
                    login.setClickable(false);
                    login.setBackgroundColor(Color.parseColor("#d6d7d7"));
                } else {
                    if (Tool.isEmail(text)) {
                        userImage.setVisibility(View.VISIBLE);
                        userState.setVisibility(View.GONE);
                        userImage.setImageResource(R.drawable.state_true);
                        if (password.getText().toString().length() > 5 && password.getText().toString().length() < 21) {
                            login.setClickable(true);
                            login.setBackgroundColor(Color.parseColor("#30b0ff"));
                        }
                    } else {
                        userImage.setVisibility(View.GONE);
                        userState.setVisibility(View.VISIBLE);
                        userState.setText("输入的邮箱格式错误");
                        login.setClickable(false);
                        login.setBackgroundColor(Color.parseColor("#d6d7d7"));
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
                        passwordState.setText("输入的密码不能低于6位");
                        login.setClickable(false);
                        login.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    } else if (text.length() > 20) {
                        passwordImage.setVisibility(View.GONE);
                        passwordState.setVisibility(View.VISIBLE);
                        passwordState.setText("输入的密码不能多与20位");
                        login.setClickable(false);
                        login.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    } else {
                        passwordState.setVisibility(View.GONE);
                        passwordImage.setVisibility(View.VISIBLE);
                        passwordImage.setImageResource(R.drawable.state_true);
                        if (Tool.isEmail(user.getText().toString())) {
                            login.setClickable(true);
                            login.setBackgroundColor(Color.parseColor("#30b0ff"));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}
