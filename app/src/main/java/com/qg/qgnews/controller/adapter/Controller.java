package com.qg.qgnews.controller.adapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

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

        void onFailed(int state, String satateInfo);
    }


    public static void RequestNews(final int idFrom, final OnRequestNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FeedBack feedBack = Request.RequestNews(idFrom);
                if (feedBack.getState() == 1) {
                    listener.onSuccess((List<News>) gson.fromJson(feedBack.getData(), new TypeToken<List<News>>() {
                    }.getType()));
                    Tool.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                } else {
                    listener.onFailed(feedBack.getState(), feedBack.getStateInfo());
                }
            }
        }).start();


    }
}
