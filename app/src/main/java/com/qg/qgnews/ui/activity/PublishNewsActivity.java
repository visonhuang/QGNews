package com.qg.qgnews.ui.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.FileAdapter;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.nio.channels.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.qg.qgnews.util.Tool.decodeSampledBitmapFromFile;
import static com.qg.qgnews.util.Tool.toast;

/**
 * Created by 黄伟烽 on 2017/7/27.
 */

public class PublishNewsActivity extends AppCompatActivity implements View.OnClickListener{

    private FloatingActionButton mFab;
    private FloatingActionButton mUploadCoverButton;
    private FloatingActionButton mUploadFileButton;
    private LinearLayout mUploadCoverLinear;
    private LinearLayout mUploadFileLinear;
    private LinearLayout mFileContainLinear;
    private RelativeLayout mFloatLinear;
    private TextView tvTitle;
    private TextView mTitleText;
    private TextView mContentText;
    private ImageView mAddPicImage;
    private ImageView mAddFileImage;
    private RecyclerView mRecyclerView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private static final int PLUS_OPEN = 1;
    private static final int PLUS_CLOSE = 0;
    private  int PulsButtonMode = PLUS_CLOSE;
    private static final int UPLOAD_OPEN = 1;
    private static final int UPLOAD_CLOSE = 0;
    private  int UploadButtonMode = UPLOAD_CLOSE;
    public  static boolean mIsPublishing;
    private static final int GET_FILE = 0;
    private static final int GET_PHOTO = 1;
    private static final int TYPE_CHOOSE_COVER = 0;
    private static final int TYPE_CHOOSE_FILE = 1;
    private   int ChoosePhotoType = TYPE_CHOOSE_COVER;
    private  boolean hasChooseCover;

    private List<String> mFileList = new ArrayList<>();
    private FileAdapter mAdapter;
    private static StopUploadListener mStopUploadListener;
    private TopBarBaseActivity.OnClickListener onClickListenerTopLeft;
    private TopBarBaseActivity.OnClickListener onClickListenerTopRight;
    private String mMenuStr;
    private Bitmap mCoverBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_publish_news);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setBackgroundColor(Color.parseColor("#00000000"));
        //初始化设置 Toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setTitle("编辑新闻");

        setTopLeftButton(R.drawable.ic_back, new TopBarBaseActivity.OnClickListener() {
            @Override
            public void onClick() {
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
        });

        setTopRightButton("发布",new TopBarBaseActivity.OnClickListener(){

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
                    Tool.toast("新闻正发布中...");
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
        mFloatLinear = (RelativeLayout) findViewById(R.id.floating_linear);
        mFab.setOnClickListener(this);
        mAddPicImage.setOnClickListener(this);
        mAddFileImage.setOnClickListener(this);
        mUploadFileButton.setOnClickListener(this);
        mUploadCoverButton.setOnClickListener(this);
        mContentText.setOnClickListener(this);
        mTitleText.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new FileAdapter(mFileList);
        mRecyclerView.setAdapter(mAdapter);

        mTitleText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        mContentText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1500)});

        mTitleText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    hideUpleadLinear();
                    return;
                }
            }
        });

        mContentText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    hideUpleadLinear();
                    return;
                }
            }
        });

        mFloatLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float startY = 0, endY = 0;

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        endY = event.getY();
                        float dy = endY - startY;
                        if (dy > 20){
                            hideUpleadLinear();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void publishNews() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int managerId = Tool.getCurrentManager().getManagerId();
                String newsTitle = mTitleText.getText().toString();
                String newsBody = mContentText.getText().toString();
                String newsAuthor = Tool.getCurrentManager().getManagerName();
                String newsTime = "我是新闻发布时间";
                String newsUuid = UUID.randomUUID().toString();
                String newsFace = null;
                if(mCoverBitmap != null){
                    newsFace = "有封面";
                }
                String filesUuid = UUID.randomUUID().toString();
                News news = new News(1,managerId,newsTitle,newsBody,newsAuthor,newsTime,newsUuid,
                        newsFace, filesUuid, null);

                Request.upLoadNews(news, mCoverBitmap, getFilePathArray(mFileList), new UploadListener() {
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
                    }

                    @Override
                    public void dealResult(String response) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }).start();
                        try{
                            FeedBack feedBack = new Gson().fromJson(response, FeedBack.class);
                            int status = feedBack.getState();
                            if(status == 1){
                                Tool.toast("新闻发布完成");
                                mFab.setImageResource(R.drawable.ic_ok);
                                mFab.startAnimation(AnimationUtils.loadAnimation(PublishNewsActivity.this, R.anim.rotate_360));
                                finish();
                            }
                            else if(status == 5000){
                                Tool.toast("新闻发布失败");
                            }
                            mIsPublishing = false;
                        }catch (Exception e){
                            e.printStackTrace();
                            Tool.toast("服务器异常");
                        }

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
                    mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.floating_button_exit));
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
                ChoosePhotoType = TYPE_CHOOSE_COVER;
                if(ContextCompat.checkSelfPermission(PublishNewsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PublishNewsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    openAlbum();
                }
                break;
            case R.id.activity_publish_upload_file_button:
                ChoosePhotoType = TYPE_CHOOSE_FILE;
                mUploadCoverLinear.setVisibility(View.GONE);
                mUploadFileLinear.setVisibility(View.GONE);
                mFab.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter));
                mFileContainLinear.setVisibility(View.VISIBLE);
                mFileContainLinear.startAnimation(AnimationUtils.loadAnimation(this, R.anim.enter));
                UploadButtonMode = UPLOAD_OPEN;
                PulsButtonMode = PLUS_CLOSE;
                break;
            case R.id.add_pic_image:
                ChoosePhotoType = TYPE_CHOOSE_FILE;
                if(ContextCompat.checkSelfPermission(PublishNewsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PublishNewsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }else {
                    openAlbum();
                }
                break;
            case R.id.add_file_image:
                if(ContextCompat.checkSelfPermission(PublishNewsActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(PublishNewsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                }else {
                    openFileSeletor();
                }
                break;

            case R.id.title_content_text:
            case R.id.news_title_text:
                hideUpleadLinear();
                break;
            default:
                break;
        }
    }

    private void openFileSeletor() {
        Intent intent = new Intent(PublishNewsActivity.this, FileSelector.class);
        intent.putExtra(FileSelector.KEY_MODE, FileSelector.MODE_FILE);
        intent.putExtra(FileSelector.KEY_MAX, 10 - mFileList.size());
        startActivityForResult(intent, GET_FILE);
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, GET_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openAlbum();
                }
                break;
            case 2:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    openFileSeletor();
                }
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

        mFileContainLinear.startAnimation(exit);
        UploadButtonMode = UPLOAD_CLOSE;
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
        switch (requestCode){
            case GET_FILE:
                Bundle bundle = data.getBundleExtra("map_key");
                Map<String,File> fileMap = (Map<String, File>) bundle.getSerializable("map_key");
                for (String s : fileMap.keySet()) {
                    mFileList.add(s);
                }
                mAdapter.freshFileList(mFileList);
                mAdapter.notifyDataSetChanged();
                break;
            case GET_PHOTO:
                if(resultCode == RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else {
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            default:
                break;
        }
    }

    // 4.4以下系统使用这个方法处理选取相册图片返回的Intent
    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        dealWithImagePath(imagePath);
    }

    // 4.4及以上系统使用这个方法处理选取相册图片返回的Intent
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        Log.d("88888888888","handleImageOnKitKat");
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        dealWithImagePath(imagePath);
    }

    // 通过Uri和selection来获取真实的图片路径
    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void dealWithImagePath(String imagePath){
        if(ChoosePhotoType == TYPE_CHOOSE_FILE){
            mFileList.add(imagePath);
            mAdapter.notifyDataSetChanged();
            Tool.toast("添加文件成功");
            return;
        }
        if(ChoosePhotoType == TYPE_CHOOSE_COVER){
            if (hasChooseCover == false){
                Tool.toast("封面选择成功");
            }else {
                Tool.toast("封面修改成功");
            }
            hasChooseCover = true;
            mCoverBitmap = Tool.decodeSampledBitmapFromFile(imagePath, 540/2, 375/2);
        }
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
        void dealResult(String response);
    }

    public interface StopUploadListener{
        void stopUpload();
    }

    protected void setTopLeftButton(int iconResId, TopBarBaseActivity.OnClickListener onClickListener){
        mToolbar.setNavigationIcon(iconResId);
        this.onClickListenerTopLeft = onClickListener;
    }

    protected void setTopRightButton(String mMenuStr, TopBarBaseActivity.OnClickListener onClickListener){
        this.mMenuStr = mMenuStr;
        this.onClickListenerTopRight = onClickListener;
    }

    protected void setTitle(String title){
        if (!TextUtils.isEmpty(title)){
            tvTitle.setText(title);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(mMenuStr)){
            getMenuInflater().inflate(R.menu.menu_activity_base_top_bar, menu);
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!TextUtils.isEmpty(mMenuStr)){
            menu.findItem(R.id.menu_1).setTitle(mMenuStr);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onClickListenerTopLeft.onClick();
        }
        else if (item.getItemId() == R.id.menu_1){
            onClickListenerTopRight.onClick();
        }
        return true;
    }


}
