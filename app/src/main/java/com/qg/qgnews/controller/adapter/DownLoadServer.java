package com.qg.qgnews.controller.adapter;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.gson.Gson;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.model.DownloadDetial;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.model.ViceFile;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadServer extends Service {
    private DownLoadBinder binder = new DownLoadBinder();
    private DownLoadFileTask downLoadFileTask;

    public DownLoadServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class DownLoadBinder extends Binder {
        public void startDownload(ViceFile viceFile) {
            downLoadFileTask = new DownLoadFileTask(listener, viceFile);
            downLoadFileTask.execute(viceFile.getFilePath(), viceFile.getFileName());
            startForeground(1, getNotification("下载" + viceFile.getFileName(), 0));
            Tool.toast("开始下载");
        }

        public void cancelDownload() {
            if (downLoadFileTask != null) {
                downLoadFileTask.cancelDownload();
            }
        }
    }

    private DownLoadListener listener = new DownLoadListener() {
        @Override
        public void onProgress(int pro, String fileName) {
            getNotificationManager().notify(1, getNotification(fileName + "下载中", pro));
        }

        @Override
        public void onSuccess(ViceFile v) {
            downLoadFileTask = null;
            //关闭通知
            stopForeground(true);
            DownloadDetial downloadDetial = new DownloadDetial();
            downloadDetial.setFileId(v.getFileId());
            Log.d("取出===========",Tool.getCurrentManager().toString());
            if (App.isManager) {
                downloadDetial.setDownloader(Tool.getCurrentManager().getManagerAccount());

            } else {
                downloadDetial.setDownloader(Tool.getUUID());
            }
            Log.d("下载情况汇报",new Gson().toJson(downloadDetial));
            Controller.RequestWithString2(RequestAdress.POST_NEWS_DOWNLOAD_DETAIL, new Gson().toJson(downloadDetial), new Controller.OnRequestListener() {
                @Override
                public void onSuccess(String json) {
                    Log.d("下载汇报完毕，返回","");
                }

                @Override
                public void onFailed(int state) {

                }
            });
            Tool.toast("下载完成");
        }

        @Override
        public void onFailed() {
            downLoadFileTask = null;
            //关闭通知
            stopForeground(true);
            Tool.toast("下载失败");
        }

        @Override
        public void onCanceled() {
            downLoadFileTask = null;
            //关闭通知
            stopForeground(true);
            Tool.toast("下载取消");
        }
    };

    private Notification getNotification(String title, int progress) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_back);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_back));
        builder.setContentTitle(title);
        builder.setContentText(progress + "%");
        builder.setProgress(100, progress, false);
        return builder.build();
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }


    class DownLoadFileTask extends AsyncTask<String, Integer, Integer> {
        public static final int SUCCESS = 0;
        public static final int FAILED = 1;
        public static final int CANCEL = 2;
        public static final int EXIST = 3;
        private boolean isCanceled = false;
        private int lastProgress;
        private DownLoadListener downLoadListener;
        private String fileName;
        private ViceFile viceFile;

        public DownLoadFileTask(DownLoadListener downLoadListener, ViceFile viceFile) {
            this.downLoadListener = downLoadListener;
            this.viceFile = viceFile;
        }

        @Override
        protected Integer doInBackground(String... params) {
            fileName = params[1];
            File saveFile = null;
            try {
                URL url = new URL(params[0]);
                URLConnection urlConnection = url.openConnection();
                HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setDoInput(true);
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();

                //文件已存在，结束
                saveFile = new File(Tool.getFileSavePath() + "//" + params[1]);
                if (saveFile.exists()) {
                    return EXIST;
                }
                //获得文件长度
                long contentLen = httpURLConnection.getContentLength();
                //文件长度异常，失败
                if (contentLen == 0 || contentLen == -1) {
                    return FAILED;
                }
                int len;
                //已下
                long donelen = 0;
                //文件保存流
                FileOutputStream outputStream = new FileOutputStream(saveFile);
                byte[] bytes = new byte[1024];
                while ((len = inputStream.read(bytes)) != -1) {
                    if (isCanceled) {
                        return CANCEL;
                    }
                    outputStream.write(bytes, 0, len);
                    donelen += len;
                    //发布进度
                    publishProgress((int) (100 * ((double) donelen / (double) contentLen)));
                }
                outputStream.close();
                return SUCCESS;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (isCanceled && saveFile != null) {
                    saveFile.delete();
                }
            }
            return FAILED;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (downLoadListener != null && values[0] > lastProgress) {
                downLoadListener.onProgress(values[0], fileName);
                lastProgress = values[0];
            }

        }


        @Override
        protected void onPostExecute(Integer integer) {
            switch (integer) {
                case SUCCESS:
                    downLoadListener.onSuccess(viceFile);
                    break;
                case CANCEL:
                    downLoadListener.onCanceled();
                    break;
                case FAILED:
                    downLoadListener.onFailed();
                    break;
                case EXIST:
                    Tool.toast("文件已存在");
                    break;
                default:
                    break;
            }
        }

        public void cancelDownload() {
            isCanceled = true;
        }
    }
}
