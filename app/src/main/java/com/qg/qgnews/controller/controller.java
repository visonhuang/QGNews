package com.qg.qgnews.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;
import com.qg.qgnews.util.Request;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class controller {
    private static controller sController = null;
    private static Gson gson = new Gson();
    public interface OnRequestNewsListener{
        void onSuccess(List<News> list);
        void onFailed(int state,String stateInfo);
    }
    public controller() {
    }

    public static controller getInstance() {
        if (sController == null) {
            sController = new controller();
        }
        return sController;
    }


    /** 请求新闻
     * @param idFrom
     * @param listener
     */
    public void RequestNews(final int idFrom, final OnRequestNewsListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FeedBack feedBack = Request.RequestNews(idFrom);
                if (feedBack.getState() == 1) {
                    listener.onSuccess((List<News>) gson.fromJson(feedBack.getData(),new TypeToken<List<News>>(){}.getType()));
                } else {
                    listener.onFailed(feedBack.getState(),feedBack.getStateInfo());
                }
            }
        }).start();
    }
}
