package com.qg.qgnews.controller;

import java.util.Collections;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class controller {
    private static Controller sController = null;

    public Controller() {
    }

    public Controller getInstance(){
        if (sController == null) {
            sController = new Controller();
        }
        return sController;
    }

}
