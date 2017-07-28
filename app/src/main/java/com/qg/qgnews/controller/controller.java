package com.qg.qgnews.controller;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class controller {
    private static controller sController = null;

    public controller() {
    }

    public controller getInstance(){
        if (sController == null) {
            sController = new controller();
        }
        return sController;
    }

}
