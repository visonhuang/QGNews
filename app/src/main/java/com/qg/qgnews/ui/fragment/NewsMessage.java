package com.qg.qgnews.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.EnclosureAdapter;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.ViceFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by linzongzhan on 2017/7/28.
 */

public class NewsMessage extends Fragment {
    private static final String TAG = "NewsMessage";

    private ImageView mNewsImage;

    private TextView mNewsText;

    private RecyclerView mNewsEnclosure;

    private View view;

    private News news;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_news_message,container,false);
        initView();
        getMessage();
        enclosureAda();
        new Imageload().execute();





        return view;
    }

    /**
     * 实例化控件
     */
    private void initView () {
        mNewsImage = (ImageView) view.findViewById(R.id.news_image);
        mNewsText = (TextView) view.findViewById(R.id.news_text);
        mNewsEnclosure = (RecyclerView) view.findViewById(R.id.news_enclosure);
    }

    /**
     * 获得News
     */
    private void getMessage () {
        Bundle bundle = getArguments();
        news = (News) bundle.getSerializable("message");
    }

    /**
     * 加载图片
     */
    class Imageload extends AsyncTask<Void,Bitmap,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                URL url = new URL(news.getNewsFace());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = inputStream.read(buffer)) != -1) {
                    byteArrayOutputStream.write(buffer,0,len);
                }
                byte[] bitmapCopy = byteArrayOutputStream.toByteArray();
                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapCopy,0,bitmapCopy.length);
                
                publishProgress(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "doInBackground: 获取图片时字节流转图片失败");
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);

            mNewsImage.setImageBitmap(values[0]);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    /**
     * 为附件的recyleView添加数据
     */
    private void enclosureAda () {
        List<ViceFile> enclosureList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mNewsEnclosure.setLayoutManager(linearLayoutManager);
        EnclosureAdapter adapter = new EnclosureAdapter(enclosureList);
        mNewsEnclosure.setAdapter(adapter);
    }

}
