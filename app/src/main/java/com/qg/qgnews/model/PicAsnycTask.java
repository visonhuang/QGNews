package com.qg.qgnews.model;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.qg.qgnews.R;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

/**
 * 项目名： ContactBook
 * 创建人： 小吉哥哥
 * 创建人： 2017/4/29.
 */

public class PicAsnycTask extends AsyncTask<Void,Void,Bitmap> {
    private ImageView mIamgeView;
    private LruCache<Integer,Bitmap> mBitmapLruCache;
    News news;
    /**
     * @param IamgeView 要填充得iamgeView
     * @param bitmapLruCache 存放图片的缓存
     */
    public PicAsnycTask(ImageView IamgeView, News news,LruCache<Integer,Bitmap> bitmapLruCache) {
        mIamgeView = IamgeView;
        mBitmapLruCache = bitmapLruCache;
        this.news = news;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        System.out.println(" 进来拿图pain");
        //如果能从缓存中得到图片则返回该图片
        Bitmap pic = mBitmapLruCache.get(news.getNewsId());
        if (pic != null) {
            System.out.println(" na到了"+news.getNewsId());
            return pic;
        } else {
            System.out.println(" 拿不到"+news.getNewsId());
            Tool.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIamgeView.setImageResource(R.drawable.loading);
                }
            });
            //否则从文件中读取，存入缓存，并返回图片
            pic = Request.getCover(news.getNewsFace());
            mBitmapLruCache.put(news.getNewsId(),pic);
            return pic;
        }
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        //将返回的图片设置给iamgeView
        mIamgeView.setImageBitmap(bitmap);
    }
}
