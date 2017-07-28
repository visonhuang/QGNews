package com.qg.qgnews.util;

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
    public List<News> RequestNews(int idFrom) {
        List<News> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new News());
        }
        return list;
    }
}
