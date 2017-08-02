package com.qg.qgnews.ui.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.qg.qgnews.R;
import com.qg.qgnews.model.DownloadDetial;
import com.qg.qgnews.model.FeedBack;
import com.qg.qgnews.model.Status;
import com.qg.qgnews.model.ViceFile;
import com.qg.qgnews.model.VisitorDownload;
import com.qg.qgnews.util.Request;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerFileListActivity extends AppCompatActivity {

    private ListView listView;

    private ListView listViewMore1;

    private SwipeRefreshLayout refreshLayout;

    private List<ViceFile> viceFiless = new ArrayList<>();

    private List<VisitorDownload> visitorDownloads = new ArrayList<>();

    private static final int MORE = 1;

    private static final int MORE2 = 2;

    private Handler handler = new Handler() {
        public void handleMessage (Message message) {
            switch (message.what) {
                case MORE:
                    //刷新附件下载详情
                    AdapterFile adapterFile = new AdapterFile(ManagerFileListActivity.this,R.layout.manager_file_list,viceFiless);

                    listView.setAdapter(adapterFile);


                    break;
                case MORE2:
                    //刷新附件下载更详细的信息
                    AdapterFileMore adapterFileMore = new AdapterFileMore(ManagerFileListActivity.this,R.layout.manager_file_list_more,visitorDownloads);

                    listViewMore1.setAdapter(adapterFileMore);

                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_file_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.file_list_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_back);

        TextView title = (TextView) findViewById(R.id.file_list_title);
        title.setText("附件列表");

        listView = (ListView) findViewById(R.id.file_list_listview);

        setViewOnClick();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //请求文件下载数据
                Gson gson = new Gson();
                Map<String,Integer> map = new HashMap<String, Integer>();
                map.put("managerId", Tool.getCurrentManager().getManagerId());
                String line = gson.toJson(map);

                String respose = Request.RequestWithString("http://192.168.43.141:8080/admin/downloadtime",line);
                FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                if (feedBack == null) {
                    Tool.toast("服务器无返回");
                } else {
                    int state = feedBack.getState();
                    if (state == 1) {

                    }
                    Status.statusResponse(state);
                }

            }
        }).start();

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.file_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //请求文件下载数据
                        Gson gson = new Gson();
                        Map<String,Integer> map = new HashMap<String, Integer>();
                        map.put("managerId", Tool.getCurrentManager().getManagerId());
                        String line = gson.toJson(map);

                        String respose = Request.RequestWithString("http:/192.168.43.141:8080/admin/downloadtime",line);
                        FeedBack feedBack = gson.fromJson(respose,FeedBack.class);
                        if (feedBack == null) {
                            Tool.toast("服务器无返回");
                            Tool.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refreshLayout.setRefreshing(false);
                                }
                            });
                        } else {
                            int state = feedBack.getState();
                            if (state == 1) {

                            }
                            Status.statusResponse(state);
                        }

                    }
                }).start();
            }
        });



    }

    private void setViewOnClick () {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                VisitorDownload visitorDownload = visitorDownloads.get(i);
                int id = visitorDownload.getFileId();

                AlertDialog.Builder builder = new AlertDialog.Builder(ManagerFileListActivity.this);
                View view1 = LayoutInflater.from(ManagerFileListActivity.this).inflate(R.layout.file_downloa_message,null);
                listViewMore1 = (ListView) findViewById(R.id.file_list_view_more);


                builder.setTitle("下载详情");
                builder.setView(view1);
                builder.create().show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //请求文件下载更详细的信息

                        Gson gson = new Gson();
                        Map<String,Integer> map = new HashMap<String, Integer>();
                        map.put("filesId",id);
                        String line = gson.toJson(map);

                        String response = Request.RequestWithString("http:/192.168.43.141:8080/admin/downloaddetial",line);
                        FeedBack feedBack = gson.fromJson(response,FeedBack.class);
                        if (feedBack == null) {
                            Tool.toast("服务器无返回");
                        } else {
                            int state = feedBack.getState();
                            if (state == 1) {

                            }
                            Status.statusResponse(state);
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
        }
        return true;
    }

    /**
     * 文件下载详情适配器
     */
    class AdapterFile extends ArrayAdapter<ViceFile> {

        private int resourceId;

        private TextView fileName;

        private TextView fileDownTimes;


        public AdapterFile(@NonNull Context context, @LayoutRes int resource, @NonNull List<ViceFile> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

            //  DownloadDetial downloadDetial = getItem(position);

            ViceFile viceFile = getItem(position);
            fileName = (TextView) view.findViewById(R.id.file_name);
            fileDownTimes = (TextView) view.findViewById(R.id.file_downtimes);

            fileName.setText("文件名：" + viceFile.getFileName());
            fileDownTimes.setText("下载次数：" + viceFile.getFileDownLoadTime());



            return view;
        }

    }

    /**
     * 更详细的附件下载信息
     */
    class AdapterFileMore extends ArrayAdapter<VisitorDownload> {

        private int resourceId;

        public AdapterFileMore(@NonNull Context context, @LayoutRes int resource, @NonNull List<VisitorDownload> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = LayoutInflater.from(ManagerFileListActivity.this).inflate(resourceId,parent,false);
            VisitorDownload visitorDownload = getItem(position);

            TextView visitorName = (TextView) view.findViewById(R.id.visitor_name);
            TextView visitorDownTime = (TextView) view.findViewById(R.id.visitor_downtime);

            visitorName.setText("下载者姓名：" + visitorDownload.getDownloader());
            visitorDownTime.setText("下载事间：" + visitorDownload.getDownloadTime());

            return view;
        }
    }
}
