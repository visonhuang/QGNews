package com.qg.qgnews.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Toast;

import com.qg.qgnews.R;

/**
 * Created by 黄伟烽 on 2017/7/27.
 */

public class PublishNewsActivity extends TopBarBaseActivity {

    private FloatingActionButton mFab;

    @Override
    protected int getContentView() {
        return R.layout.activity_public_news;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("编辑新闻");

        // 因为左上角的按钮 返回 功能用的比较多，所以可以重载一个方法。
        setTopLeftButton();

        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(PublishNewsActivity.this, "陈序员点击了左上角按钮！", Toast.LENGTH_LONG).show();
            }
        });

        setTopRightButton("", 0, new OnClickListener() {
            @Override
            public void onClick() {
                Toast.makeText(PublishNewsActivity.this, "点击了！", Toast.LENGTH_LONG).show();
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.floating_button);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimator(v);
            }
        });
    }

    private void startAnimator(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 360, 20, 0);
        animator.setDuration(500);
        animator.start();
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
    }
}
