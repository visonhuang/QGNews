package com.qg.qgnews.controller.adapter;

/**
 * Created by 小吉哥哥 on 2017/7/30.
 */

public interface DownLoadListener {

    void onProgress(int pro);

    void onSuccess();

    void onFailed();

    void onCanceled();
}
