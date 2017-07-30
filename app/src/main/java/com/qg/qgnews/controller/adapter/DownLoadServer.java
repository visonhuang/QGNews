package com.qg.qgnews.controller.adapter;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

public class DownLoadServer extends Service {
    private DownLoadBinder binder = new DownLoadBinder();

    public DownLoadServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    class DownLoadBinder extends Binder {
        public void startDownload(String url) {

        }

        public void cancelDownload() {

        }
    }


    class DownLoadFileTask extends AsyncTask<String, Integer, Integer> {
        public static final int SUCCESS = 0;
        public static final int FAILED = 1;
        public static final int CANCEL = 2;
        private boolean isCanceled = false;
        private int lastProgress;
        private DownLoadListener downLoadListener;

        public DownLoadFileTask(DownLoadListener downLoadListener) {
            this.downLoadListener = downLoadListener;
        }

        @Override
        protected Integer doInBackground(String... params) {
            return null;
        }
    }
}
