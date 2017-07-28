package com.qg.qgnews.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.qg.qgnews.R;
import com.qg.qgnews.util.Tool;

/**
 * Created by 黄伟烽 on 2017/7/27.
 */

public class PublishNewsActivity extends TopBarBaseActivity implements View.OnClickListener{

    private FloatingActionButton mFab;
    private FloatingActionButton mUploadCoverButton;
    private FloatingActionButton mUploadFileButton;
    private LinearLayout mUploadCoverLinear;
    private LinearLayout mUploadFileLinear;
    private static final int PLUS_OPEN = 1;
    private static final int PLUS_CLOSE = 0;
    private static int pulsButtonMode = PLUS_CLOSE;

    @Override
    protected int getContentView() {
        return R.layout.activity_publish_news;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitle("编辑新闻");

        setTopLeftButton(R.drawable.ic_back, new OnClickListener() {
            @Override
            public void onClick() {

            }
        });

        setTopRightButton("发布", 0, new OnClickListener() {
            @Override
            public void onClick() {
                mFab.setImageResource(R.drawable.ic_ok);
                mFab.startAnimation(AnimationUtils.loadAnimation(PublishNewsActivity.this, R.anim.rotate_360));
                Tool.toast("发布成功");
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.floating_button);
        mUploadFileButton = (FloatingActionButton) findViewById(R.id.activity_publish_upload_file_button);
        mUploadCoverButton = (FloatingActionButton) findViewById(R.id.activity_publish_upload_cover_button);
        mUploadFileLinear = (LinearLayout) findViewById(R.id.activity_public_upload_file_linearlayout);
        mUploadCoverLinear = (LinearLayout) findViewById(R.id.activity_public_upload_cover_linearlayout);
        mFab.setOnClickListener(this);
        mUploadFileButton.setOnClickListener(this);
        mUploadCoverButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floating_button:
                if (pulsButtonMode == PLUS_CLOSE) {
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_to_45));
                    showButtons();
                    pulsButtonMode = PLUS_OPEN;
                    //TODO open
                } else {
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_back_45));
                    hideButtons();
                    pulsButtonMode = PLUS_CLOSE;
                    //TODO close
                }
                break;
            case R.id.activity_publish_upload_cover_button:
                break;
            case R.id.activity_publish_upload_file_button:
                break;
            default:
                break;
        }
    }

    private void hideButtons() {
        Animation hide_0, hide_50;
        hide_0 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_0);
        hide_50 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_50);
        mUploadCoverLinear.startAnimation(hide_50);
        mUploadFileLinear.startAnimation(hide_0);

        hide_0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mUploadFileLinear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        hide_50.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mUploadCoverLinear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void showButtons() {
        mUploadFileLinear.setVisibility(View.VISIBLE);
        mUploadCoverLinear.setVisibility(View.VISIBLE);
        mUploadFileLinear.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_50));
        mUploadCoverLinear.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_0));
    }
}
