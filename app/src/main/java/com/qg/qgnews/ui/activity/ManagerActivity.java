package com.qg.qgnews.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.qg.qgnews.R;

public class ManagerActivity extends TopBarBaseActivity {

    @Override
    protected int getContentView() {
        return R.layout.activity_manager;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                finish();
            }
        });
    }
}
