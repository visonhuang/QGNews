package com.qg.qgnews.controller.adapter;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class Controller {
    private static Controller controller;
    private static Gson gson = new Gson();

    public static Controller getInstance() {
        if (controller == null) {
            controller = new Controller();
        }
        return controller;
    }

    public interface OnRequestNewsListener {
        void onSuccess(List<News> list);

        void onFailed(int state);
    }

    public interface OnRequestListener {
        void onSuccess(String json);

        void onFailed(int state);
    }

    //刷新新闻
    public static void RequestNews(final OnRequestNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FeedBack feedBack = Request.RequestNews();
                try {
                    if (feedBack.getState() == 1) {
                        listener.onSuccess((List<News>) gson.fromJson(feedBack.getData(), new TypeToken<List<News>>() {
                        }.getType()));
                        Tool.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    } else {
                        listener.onFailed(feedBack.getState());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Tool.toast("服务器异常");
                }

            }
        }).start();
    }
//
//    public static void SearchNews(final OnRequestNewsListener listener){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                FeedBack feedBack = Request.SearchNews();
//            }
//        }).start();
//    }

    //详情请求
    public static void RequestNewsDetial(final int newsId, final OnRequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonString = "{\"newsId\":" + newsId + "}";
                try {
                    FeedBack feedBack = Request.RequestWithString2(RequestAdress.GET_NEWS_DETIAL, jsonString);
                    if (feedBack.getState() == 1) {
                        listener.onSuccess(feedBack.getData());
                    } else {
                        listener.onFailed(feedBack.getState());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailed(-1);
                }
            }
        }).start();
    }

    //带字符串请求
    public static void RequestWithString(final String url, final String json, final OnRequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FeedBack feedBack = gson.fromJson(Request.RequestWithString(url, json), FeedBack.class);
                    if (feedBack.getState() == 1) {
                        listener.onSuccess(feedBack.getData());
                    } else {
                        listener.onFailed(feedBack.getState());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFailed(-1);
                }

            }
        }).start();
    }


    public static void RequestWithString2(final String url, final String json, final OnRequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FeedBack feedBack = Request.RequestWithString2(url, json);
                if (feedBack.getState() == 1) {
                    listener.onSuccess(feedBack.getData());
                } else {
                    listener.onFailed(feedBack.getState());
                }
            }
        }).start();
    }
    public static void RequestWithString3(final String url, final String json, final OnRequestListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FeedBack feedBack = Request.RequestWithString3(url, json);
                if (feedBack.getState() == 1) {
                    listener.onSuccess(feedBack.getData());
                } else {
                    listener.onFailed(feedBack.getState());
                }
            }
        }).start();
    }
}
