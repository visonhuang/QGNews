package com.qg.qgnews.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
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
    public static String session = "0";
    public static StringBuffer resultBuffer;

    /**
     * @return 新闻列表，最大十条
     */
    public static FeedBack RequestNews() {
        Gson gson = new Gson();

        return gson.fromJson(RequestWithNoString(RequestAdress.REQUEST_NEWS), FeedBack.class);
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
        StringBuffer resultBuffer = new StringBuffer("{\"state\":0}");
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
            // Tool.saveSessionId(httpURLConnection);
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            reader = new BufferedReader(inputStreamReader);
            tempLine = null;
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
        }
        return resultBuffer.toString();
    }

    /**
     * @param news  新闻体，其中的 newsFace 字段若没有封面测不填，若有则填写任意字符
     * @param cover 没有封面则传null
     * @param files 附件路径数组
     * @return feedback字符串，若有异常则为  ""
     */
    public static String upLoadNews(final News news, final Bitmap cover, final String[] files, final PublishNewsActivity.UploadListener listener) {

        new AsyncTask<Void, Integer, Boolean>() {

            @Override
            protected void onPreExecute() {
                listener.showProgress();
            }

            @Override
            protected Boolean doInBackground(Void... params) {

                InputStream inputStream = null;
                BufferedReader reader = null;
                InputStreamReader inputStreamReader = null;
                try {
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

                    String tempLine = null;

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
                        while ((lenth = coverIps.read(b)) != -1) {
                            if (mIsStopUpload) {
                                ds.close();
                                return null;
                            }
                            ds.write(b, 0, lenth);
                        }
                        Log.d("上传封面", "");
                        ds.write(end.getBytes());
                        Tool.toast("封面上传完成");
                    }


                    //上传附件
                    long uploadedBytes = 0;
                    long sumBytes = 0;
                    for (String filePath : files) {
                        sumBytes += new File(filePath).length();
                        Log.d("fileleng", sumBytes + "");
                    }

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
                            if (mIsStopUpload) {
                                ds.close();
                                return null;
                            }
                            ds.write(buffer, 0, length);
                            ds.flush();
                            uploadedBytes += length;
                            publishProgress((int) (100 * uploadedBytes / sumBytes));

                            Log.d("fileleng", (int) (100 * uploadedBytes / sumBytes) + "");
                            Log.d("上传中", "");
                        }
                        ds.write(end.getBytes());
                   /* close streams */
                        fStream.close();
                    }
                    ds.write((twoHyphens + boundary + twoHyphens + end).getBytes());
                   /* close streams */
                    ds.flush();
                    Tool.toast("文件上传完成");

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
//                        System.out.println(resultBuffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
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
                    return true;
                }
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                listener.freshProgress(values[0]);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                listener.finishUpload();
                listener.dealResult(resultBuffer.toString());
            }

        }.execute();

        return null;
    }


    /**
     * @param URl 请求地址
     * @return
     */
    public static String RequestWithNoString(String URl) {
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

    public static Bitmap getCover(String url) {
        Log.d("图片链接",url);
        InputStream inputStream = null;
        try {
            URLConnection urlConnection = new URL(url).openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("GET");
            inputStream = httpURLConnection.getInputStream();
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return BitmapFactory.decodeResource(App.context.getResources(), R.drawable.no_face);
    }

    public static FeedBack RequestWithString2(String URl, String content) {
        Log.d("asdasd", content);
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        BufferedOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        StringBuffer resultBuffer = new StringBuffer("{\"state\":0}");
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
            // Tool.saveSessionId(httpURLConnection);
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.setLenient(true);
            Gson gson = new Gson();
            return gson.fromJson(reader, FeedBack.class);
        } catch (Exception e) {
            Tool.toast("好像出了点问题");
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
        }
        return new FeedBack(0, "");
    }

    public static String RequestWithSession(String URl, String content, boolean isGetSession) {
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




            if (!isGetSession) {
                Log.d("asdasd", session);
                httpURLConnection.setRequestProperty("Cookie", session);
            }




            // 设置DataOutputStream
            ds = new BufferedOutputStream((httpURLConnection.getOutputStream()));
            ds.write(content.getBytes());
               /* close streams */
            ds.flush();

            // Tool.saveSessionId(httpURLConnection);
            String cookieValue = httpURLConnection.getHeaderField("Set-Cookie");
            inputStream = httpURLConnection.getInputStream();




            if (isGetSession) {
                session = cookieValue.substring(0, cookieValue.indexOf(";"));
                System.out.println(session);
            }




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

    /** 发送心跳包
     * @param URl
     */
    public static void heartBeat(String URl) {

        String boundary = "*****";
        BufferedOutputStream ds = null;
        try {
            URL url = new URL(URl);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            httpURLConnection.setConnectTimeout(2000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            httpURLConnection.setRequestProperty("Charset", "utf-8");
            ds = new BufferedOutputStream((httpURLConnection.getOutputStream()));
            ds.write((new Gson().toJson(Tool.getCurrentManager())).getBytes());
            ds.flush();
            System.out.println("发送心跳==="+new Gson().toJson(Tool.getCurrentManager()));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }


    }
    public static FeedBack RequestWithString3(String URl, String content) {
        Log.d("asdasd", content);
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        BufferedOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        StringBuffer resultBuffer = new StringBuffer("{\"state\":0}");
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
            // Tool.saveSessionId(httpURLConnection);
            inputStream = httpURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));
            reader.setLenient(true);
            Gson gson = new Gson();
            return gson.fromJson(reader, FeedBack.class);
        } catch (Exception e) {
            Tool.toast("好像出了点问题");
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
        }
        return new FeedBack(0, "");
    }
}
