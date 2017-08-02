package com.qg.qgnews.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.activity.LoginActivity;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by linzongzhan on 2017/7/30.
 */

public class ForgetPassword extends Fragment {

    private int buttonState = 1; //1为验证码按钮还没计时，0为验证码在计时

    private View view;

    private EditText user;

    private EditText password;

    private EditText number;

    private Button number_button;

    private Button change;

    private TextView userState;

    private TextView passwordState;

    private ImageView userImage;

    private ImageView passwordImage;

    private TimeCount time;

    String sessionid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_forget_password, container, false);

        LoginActivity loginActivity = (LoginActivity) getActivity();
        loginActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        loginActivity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);

        time = new TimeCount(60000, 1000);

        initView();
        viewOnClick();
        editViewOnClick();

        number_button.setClickable(false);
        change.setClickable(false);


        return view;
    }

    /**
     * 实例化控件
     */
    private void initView() {
        user = (EditText) view.findViewById(R.id.forget_user);
        password = (EditText) view.findViewById(R.id.forget_password_Edit);
        number = (EditText) view.findViewById(R.id.forget_number);
        number_button = (Button) view.findViewById(R.id.forget_number_button);
        change = (Button) view.findViewById(R.id.forget_change);

        userState = (TextView) view.findViewById(R.id.forger_user_state);
        passwordState = (TextView) view.findViewById(R.id.forget_password_state);
        userImage = (ImageView) view.findViewById(R.id.forget_user_image);
        passwordImage = (ImageView) view.findViewById(R.id.forget_password_image);
    }

    /**
     * 设置点击事件
     */
    private void viewOnClick() {

        number_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.start();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //发送验证码
                        Gson gson = new Gson();
                        Manager manager = new Manager();
                        manager.setManagerAccount(user.getText().toString());
                        String line = gson.toJson(manager);
                        Log.d(TAG, line);
                        String response = Request.RequestWithSession(RequestAdress.SEND_VERIFY_CODE, line, true);
                        Log.d(TAG, line);
                        FeedBack feedBack = gson.fromJson(response, FeedBack.class);
                        if (feedBack == null) {
                            Tool.toast("服务器无返回");
                        } else {
                            int state = feedBack.getState();
                            if (state == 1) {
                                Tool.toast("发送验证码成功");
                            } else if (state == 5000) {
                                Tool.toast("服务器异常");
                            } else if (state == 3) {
                                Tool.toast("邮箱不存在");
                            } else if (state == 4) {
                                Tool.toast("邮箱为空");
                            } else if (state == 5) {
                                Tool.toast("密码为空");
                            } else if (state == 6) {
                                Tool.toast("邮箱格式不正确");
                            }
                        }
                    }
                }).start();
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //设置新密码
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {


                            Gson gson = new Gson();
                            String nameS = user.getText().toString();
                            String passwordS = password.getText().toString();
                            String numberS = number.getText().toString();
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("managerAccount", nameS);
                            map.put("managerPassword", Tool.encryption(passwordS));
                            map.put("verifyCode", numberS);
                            String line = gson.toJson(map);
                            Log.d(TAG, line);
                            String response = Request.RequestWithSession(RequestAdress.SET_NEW_PASSWORD, line, false);
                            Log.d(TAG, response);
                            FeedBack feedBack = gson.fromJson(response, FeedBack.class);
                            int state = feedBack.getState();
                            if (state == 1) {
                                Tool.toast("修改密码成功");
                                Manager manager = Tool.getCurrentManager();
                                manager.setManagerPassword(password.getText().toString());
                                Tool.setCurrentManager(manager);
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
                            } else if (state == 0){
                                Tool.toast("好像出了点问题");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Tool.toast("好像出了点问题");
                        }
                    }
                }).start();

            }
        });
    }

    /**
     * 编辑框监听
     */
    private void editViewOnClick() {
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
                    change.setClickable(false);
                    change.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    number_button.setClickable(false);
                    number_button.setBackgroundColor(Color.parseColor("#d6d7d7"));
                } else {
                    if (Tool.isEmail(text)) {
                        userImage.setVisibility(View.VISIBLE);
                        userState.setVisibility(View.GONE);
                        userImage.setImageResource(R.drawable.state_true);
                        if (buttonState == 1) {
                            number_button.setClickable(true);
                            number_button.setBackgroundColor(Color.parseColor("#30b0ff"));
                        }
                        if (password.getText().toString().length() > 5 && password.getText().toString().length() < 21 && number.getText().toString().length() != 0) {
                            change.setClickable(true);
                            change.setBackgroundColor(Color.parseColor("#30b0ff"));
                        }
                    } else {
                        userImage.setVisibility(View.GONE);
                        userState.setVisibility(View.VISIBLE);
                        userState.setText("输入的邮箱格式错误");
                        change.setClickable(false);
                        change.setBackgroundColor(Color.parseColor("#d6d7d7"));
                        number_button.setClickable(false);
                        number_button.setBackgroundColor(Color.parseColor("#d6d7d7"));

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
                    change.setClickable(false);
                    change.setBackgroundColor(Color.parseColor("#d6d7d7"));
                } else {
                    if (text.length() < 6) {
                        passwordImage.setVisibility(View.GONE);
                        passwordState.setVisibility(View.VISIBLE);
                        passwordState.setText("输入的密码不能低于6位");
                        change.setClickable(false);
                        change.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    } else if (text.length() > 20) {
                        passwordImage.setVisibility(View.GONE);
                        passwordState.setVisibility(View.VISIBLE);
                        passwordState.setText("输入的密码不能多与20位");
                        change.setClickable(false);
                        change.setBackgroundColor(Color.parseColor("#d6d7d7"));
                    } else {
                        passwordState.setVisibility(View.GONE);
                        passwordImage.setVisibility(View.VISIBLE);
                        passwordImage.setImageResource(R.drawable.state_true);
                        if (Tool.isEmail(user.getText().toString()) && number.getText().toString().length() != 0) {
                            change.setClickable(true);
                            change.setBackgroundColor(Color.parseColor("#30b0ff"));
                        }
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (Tool.isEmail(user.getText().toString()) && password.getText().toString().length() > 5 && password.getText().toString().length() < 21 && number.getText().toString().length() != 0) {
                    change.setClickable(true);
                    change.setBackgroundColor(Color.parseColor("#30b0ff"));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            number_button.setClickable(true);
            number_button.setText("获得验证码");
            buttonState = 1;
            number_button.setBackgroundColor(Color.parseColor("#30b0ff"));
        }

        @Override
        public void onTick(long l) {
            number_button.setClickable(false);
            number_button.setText("剩余" + (int) (l / 1000) + "秒");
            number_button.setBackgroundColor(Color.parseColor("#d6d7d7"));
            buttonState = 0;
        }
    }
}
