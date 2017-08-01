package com.qg.qgnews.controller.adapter;

import com.qg.qgnews.model.ViceFile;

/**
 * Created by 小吉哥哥 on 2017/7/30.
 */

public interface DownLoadListener {

    void onProgress(int pro,String fileName);

    void onSuccess(ViceFile v);

    void onFailed();

    void onCanceled();
}
