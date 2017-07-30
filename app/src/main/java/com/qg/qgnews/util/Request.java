package com.qg.qgnews.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.activity.PublishNewsActivity;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/27.
 */

public class Request {

    private static BufferedOutputStream ds;
    private static boolean mIsStopUpload;
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
        return new FeedBack(1, gson.toJson(list));
    }

    /**
     * @param URl     请求地址
     * @param content 请求内容
     * @return feedback字符串
     */
    @NonNull
    public static String RequestWithString(String URl, String content) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        BufferedOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            URL url = new URL(URl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            httpURLConnection.setRequestProperty("Charset", "utf-8");
            // 设置DataOutputStream
            ds = new BufferedOutputStream((httpURLConnection.getOutputStream()));
            ds.write(content.getBytes());
               /* close streams */
            ds.flush();
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            reader = new BufferedReader(inputStreamReader);
            tempLine = null;
            resultBuffer = new StringBuffer();

            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
                resultBuffer.append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return resultBuffer.toString();
        }
    }

    /**
     * @param news  新闻体，其中的 newsFace 字段若没有封面测不填，若有则填写任意字符
     * @param cover 没有封面则传null
     * @param files 附件路径数组
     * @return feedback字符串，若有异常则为  ""
     */
    public static String upLoadNews(News news, Bitmap cover,final String[] files, final PublishNewsActivity.UploadListener listener) {
        PublishNewsActivity.setStopUploadListener(new PublishNewsActivity.StopUploadListener() {
            @Override
            public void stopUpload() {
                mIsStopUpload = true;
            }
        });
        Gson gson = new Gson();
        final String end = "\r\n";
        final String twoHyphens = "--";
        final String boundary = "*****";
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        try {
            URL url = new URL(RequestAdress.UPLOAD_NEWS);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            httpURLConnection.setRequestProperty("Charset", "utf-8");
            // 设置DataOutputStream
            ds = new BufferedOutputStream((httpURLConnection.getOutputStream()));
            ds.write((twoHyphens + boundary + end).getBytes());
            ds.write(("Content-Disposition: form-data; " + "name=\"news" + "\"" + end).getBytes());
            ds.write(("Content-Type: text; charset=UTF-8" + end + end).getBytes());


            //上传新闻体
            ds.write((gson.toJson(news) + end + end).getBytes());


            //上传封面
           if (cover != null) {
                ds.write((twoHyphens + boundary + end).getBytes());
                ds.write(("Content-Disposition: form-data; " + "name=\"file" + "\";filename=\"" + "/index.png"
                        + "\"" + end).getBytes());
                ds.write(("Content-Type: application/octet-stream; charset=UTF-8").getBytes());
                ds.write((end + end).getBytes());
                ByteArrayInputStream coverIps = Tool.Bitmap2Bytes(cover);
                byte[] b = new byte[1024];
                int lenth;
                while((lenth=coverIps.read(b)) != -1){
                    if(mIsStopUpload){
                        ds.close();
                        return null;
                    }
                    ds.write(b, 0, lenth);
                }
                Log.d("上传封面", "");
                ds.write(end.getBytes());
                Tool.toast("封面上传完成");
            }

            new AsyncTask<Void, Integer, Boolean>(){

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    listener.showProgress();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    //上传附件
                    long uploadedBytes = 0;
                    long sumBytes = 0;
                    for(String filePath : files){
                        sumBytes += new File(filePath).length();
                    }
                    try{
                        for (int i = 0; i < files.length; i++) {
                            System.out.println("上传文件" + i);
                            String uploadFile = files[i];
                            ds.write((twoHyphens + boundary + end).getBytes());
                            ds.write(("Content-Disposition: form-data; " + "name=\"file" + "\";filename=\"" + uploadFile
                                    + "\"" + end).getBytes());
                            ds.write(("Content-Type: application/octet-stream; charset=UTF-8").getBytes());
                            ds.write((end + end).getBytes());
                            FileInputStream fStream = new FileInputStream(uploadFile);
                            int bufferSize = 1024;
                            byte[] buffer = new byte[bufferSize];
                            int length;
                            while ((length = fStream.read(buffer)) != -1) {
                                if(mIsStopUpload){
                                    ds.close();
                                    return null;
                                }
                                ds.write(buffer, 0, length);
                                uploadedBytes += length;
                                publishProgress((int)(100 * uploadedBytes / sumBytes));
                                Log.d("上传中", "");
                            }
                            ds.write(end.getBytes());
               /* close streams */
                            fStream.close();
                        }
                        ds.write((twoHyphens + boundary + twoHyphens + end).getBytes());
               /* close streams */
                        ds.flush();
                    }catch (Exception e){
                        e.printStackTrace();
                        return false;
                    }
                    return true;
                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    listener.freshProgress(values[0]);
                }

                @Override
                protected void onPreExecute() {
                    listener.finishUpload();
                }
            }.execute();


            //读取反馈
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            reader = new BufferedReader(inputStreamReader);
            tempLine = null;
            resultBuffer = new StringBuffer();
            while ((tempLine = reader.readLine()) != null) {
                resultBuffer.append(tempLine);
                resultBuffer.append("\n");
            }
            System.out.println(resultBuffer.toString());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return resultBuffer.toString();
        }
    }

}
