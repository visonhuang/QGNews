package com.qg.qgnews.ui.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.util.Tool;

/**
 * Created by linzongzhan on 2017/7/30.
 */

public class ForgetPassword extends Fragment {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_forget_password,container,false);

        time = new TimeCount(60000,1000);





        return view;
    }

    /**
     * 实例化控件
     */
    private void initView () {
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
    private void viewOnClick () {

        number_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                time.start();
                //发送验证码
                Tool.toast("验证码");
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册
                Tool.toast("注册");
            }
        });
    }

    /**
     * 编辑框监听
     */
    private void editViewOnClick () {
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
                } else {
                    if (Tool.isEmail(text)) {
                        userImage.setVisibility(View.VISIBLE);
                        userState.setVisibility(View.GONE);
                        userImage.setImageResource(R.drawable.state_true);
                    } else {
                        userImage.setVisibility(View.GONE);
                        userState.setVisibility(View.VISIBLE);
                        userState.setText("输入的邮箱格式错误");
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
                    } else if (text.length() > 20) {
                        passwordImage.setVisibility(View.GONE);
                        passwordState.setVisibility(View.VISIBLE);
                        passwordState.setText("输入的密码不能多与20位");
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
    }



    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            number_button.setClickable(true);
            number_button.setText("获得验证码");
        }

        @Override
        public void onTick(long l) {
            number_button.setClickable(false);
            number_button.setText("剩余" + l + "秒");
        }
    }
}
