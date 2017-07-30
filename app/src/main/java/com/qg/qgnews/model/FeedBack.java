package com.qg.qgnews.model;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class FeedBack {
    private int state;//状态码
    private String data;//json数据，可能是新闻，用等等，按情况转化

    public FeedBack(int state, String data) {
        this.state = state;
        this.data = data;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
