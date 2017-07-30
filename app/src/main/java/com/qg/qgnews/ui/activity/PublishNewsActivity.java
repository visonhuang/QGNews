package com.qg.qgnews.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.FileAdapter;
import com.qg.qgnews.model.News;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private TextView mTitleText;
    private TextView mContentText;
    private ImageView mAddPicImage;
    private ImageView mAddFileImage;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private static final int PLUS_OPEN = 1;
    private static final int PLUS_CLOSE = 0;
    private static int PulsButtonMode = PLUS_CLOSE;
    private static final int UPLOAD_OPEN = 1;
    private static final int UPLOAD_CLOSE = 0;
    private static int UploadButtonMode = UPLOAD_CLOSE;
    private static boolean mIsPublishing;

    private List<String> mFileList = new ArrayList<>();
    private FileAdapter adapter;
    private static StopUploadListener mStopUploadListener;

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
                if(isEmptyText(mTitleText.getText().toString())){
                    Tool.toast("新闻标题不能为空");
                    return;
                }
                if(isEmptyText(mContentText.getText().toString())){
                    Tool.toast("新闻内容不能为空");
                    return;
                }
                if(mIsPublishing == true){
                    Tool.toast("不能重复发布");
                    return;
                }
                mIsPublishing = true;
                publishNews();
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mTitleText = (TextView) findViewById(R.id.news_title_text);
        mContentText = (TextView) findViewById(R.id.title_content_text);
        mAddPicImage = (ImageView) findViewById(R.id.add_pic_image);
        mAddFileImage = (ImageView) findViewById(R.id.add_file_image);
        mFab = (FloatingActionButton) findViewById(R.id.floating_button);
        mUploadFileButton = (FloatingActionButton) findViewById(R.id.activity_publish_upload_file_button);
        mUploadCoverButton = (FloatingActionButton) findViewById(R.id.activity_publish_upload_cover_button);
        mUploadFileLinear = (LinearLayout) findViewById(R.id.activity_public_upload_file_linearlayout);
        mUploadCoverLinear = (LinearLayout) findViewById(R.id.activity_public_upload_cover_linearlayout);
        mFileContainLinear = (LinearLayout) findViewById(R.id.file_container_linear);
        mFab.setOnClickListener(this);
        mAddPicImage.setOnClickListener(this);
        mAddFileImage.setOnClickListener(this);
        mUploadFileButton.setOnClickListener(this);
        mUploadCoverButton.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        adapter = new FileAdapter(mFileList);
        mRecyclerView.setAdapter(adapter);

        mTitleText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        mContentText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1500)});
    }

    private void publishNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String newsTitle = mTitleText.getText().toString();
                String newsBody = mContentText.getText().toString();
                String newsAuthor = "我是新闻作者";
                String newsTime = "我是新闻发布时间";
                String newsUuid = UUID.randomUUID().toString();
                String newsFace = null;
                String filesUuid = UUID.randomUUID().toString();
                News news = new News(1,1,newsTitle,newsBody,newsAuthor,newsTime,newsUuid,
                        newsFace, filesUuid, null);
                String filePath = "/storage/emulated/0/DCIM/Camera/IMG_20170711_232637.jpg";
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);

                String response = Request.upLoadNews(news, null, getFilePathArray(mFileList), new UploadListener() {
                    @Override
                    public void showProgress() {
                        Tool.toast("文件开始上传");
                    }

                    @Override
                    public void freshProgress(int progress) {
                        mProgressBar.setProgress(progress);
                    }

                    @Override
                    public void finishUpload() {
                        Tool.toast("新闻发布完成");
                        mFab.setImageResource(R.drawable.ic_ok);
                        mFab.startAnimation(AnimationUtils.loadAnimation(PublishNewsActivity.this, R.anim.rotate_360));
                        finish();
                    }
                });
            }
        }).start();
    }

    private String[] getFilePathArray(List<String> fileList) {
        String[] filePathArray = new String[fileList.size()];
        int i = 0;
        for(String s : fileList){
            filePathArray[i] = s;
            i++;
        }
        return filePathArray;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floating_button:
                if (UploadButtonMode == UPLOAD_OPEN){
                    hideUpleadLinear();
                    UploadButtonMode = UPLOAD_CLOSE;
                }
                else if (PulsButtonMode == PLUS_CLOSE) {
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_to_45));
                    showButtons();
                    PulsButtonMode = PLUS_OPEN;
                }
                else{
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_back_45));
                    hideButtons();
                    PulsButtonMode = PLUS_CLOSE;
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
            case R.id.add_pic_image:
                break;
            case R.id.add_file_image:
                Intent intent = new Intent(PublishNewsActivity.this, FileSelector.class);
                intent.putExtra(FileSelector.KEY_MODE, FileSelector.MODE_FILE);
                intent.putExtra(FileSelector.KEY_MAX, 10 - mFileList.size());
                startActivityForResult(intent, 1);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_OK){
            return;
        }
        if(requestCode == 1){
            Bundle bundle = data.getBundleExtra("map_key");
            Map<String,File> fileMap = (Map<String, File>) bundle.getSerializable("map_key");
            for (String s : fileMap.keySet()) {
                mFileList.add(s);
            }
        }
        adapter.freshFileList(mFileList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if(mIsPublishing == false){
            new AlertDialog.Builder(PublishNewsActivity.this)
                    .setMessage("确定取消发布新闻？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            PublishNewsActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        }
    }

    public boolean isEmptyText(String text){
        if(TextUtils.isEmpty(text)){
            return true;
        }
        return false;
    }
    
    public static void setStopUploadListener(StopUploadListener listener){
        mStopUploadListener = listener;
    }

    public interface UploadListener{
        void showProgress();
        void freshProgress(int progress);
        void finishUpload();
    }

    public interface StopUploadListener{
        void stopUpload();
    }
    
}
