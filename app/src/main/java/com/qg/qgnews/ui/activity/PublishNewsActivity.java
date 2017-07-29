package com.qg.qgnews.ui.activity;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
    private LinearLayout mFileContainLinear;
    private static final int PLUS_OPEN = 1;
    private static final int PLUS_CLOSE = 0;
    private static int PulsButtonMode = PLUS_CLOSE;
    private static final int UPLOAD_OPEN = 1;
    private static final int UPLOAD_CLOSE = 0;
    private static int UploadButtonMode = UPLOAD_CLOSE;

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
        mFileContainLinear = (LinearLayout) findViewById(R.id.file_container_linear);
        mFab.setOnClickListener(this);
        mUploadFileButton.setOnClickListener(this);
        mUploadCoverButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floating_button:
                if (UploadButtonMode == UPLOAD_OPEN){
                    Log.d("8888888888", "8888888888888");
                    hideUpleadLinear();
                    UploadButtonMode = UPLOAD_CLOSE;
                }
                else if (PulsButtonMode == PLUS_CLOSE) {
                    Log.d("8888888888", "33333333333333");
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_to_45));
                    showButtons();
                    PulsButtonMode = PLUS_OPEN;
                    //TODO open
                }
                else{
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_back_45));
                    hideButtons();
                    PulsButtonMode = PLUS_CLOSE;
                    //TODO close
                }
                break;
            case R.id.activity_publish_upload_cover_button:
                break;
            case R.id.activity_publish_upload_file_button:
                mUploadCoverLinear.setVisibility(View.GONE);
                mUploadFileLinear.setVisibility(View.GONE);
                mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter));
                mFileContainLinear.setVisibility(View.VISIBLE);
                mFileContainLinear.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter));
                UploadButtonMode = UPLOAD_OPEN;
                PulsButtonMode = PLUS_CLOSE;
                break;
            default:
                break;
        }
    }

    private void hideUpleadLinear() {
        Animation exit = AnimationUtils.loadAnimation(this, R.anim.exit);
        exit.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mFileContainLinear.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.floating_button_exit));
        mFileContainLinear.startAnimation(exit);

    }

    private void hideButtons() {
        Animation hide_0, hide_50;
        hide_0 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_0);
        hide_50 = AnimationUtils.loadAnimation(this, R.anim.trans_down_offset_50);

        hide_0.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Log.d("8888888888.", "7777777777777");
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

        mUploadCoverLinear.startAnimation(hide_50);
        mUploadFileLinear.startAnimation(hide_0);
    }

    private void showButtons() {
        mUploadFileLinear.setVisibility(View.VISIBLE);
        mUploadCoverLinear.setVisibility(View.VISIBLE);
        mUploadFileLinear.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_50));
        mUploadCoverLinear.startAnimation(AnimationUtils.loadAnimation(this, R.anim.trans_up_offset_0));
    }
}
