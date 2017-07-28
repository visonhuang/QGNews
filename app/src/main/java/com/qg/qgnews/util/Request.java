package com.qg.qgnews.util;

import com.google.gson.Gson;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class Request {
    /**
     * @param idFrom 从这个id往下请求十条新闻
     * @return 新闻列表，最大十条
     */
    public static FeedBack RequestNews(int idFrom) {
        Gson gson = new Gson();
        //模拟数据
        List<News> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new News());
        }
        return new FeedBack(1,"",gson.toJson(list));
    }
}
