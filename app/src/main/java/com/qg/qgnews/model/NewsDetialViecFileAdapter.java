package com.qg.qgnews.model;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.Controller;
import com.qg.qgnews.controller.adapter.DownloadDetialAdapter;
import com.qg.qgnews.ui.activity.NewsMessageActivity;
import com.qg.qgnews.util.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/30.
 */

public class NewsDetialViecFileAdapter extends RecyclerView.Adapter<NewsDetialViecFileAdapter.ViewHolder> {
    NewsMessageActivity newsMessageActivity;
    List<ViceFile> viceFileList;
    private int mode = NewsMessageActivity.MODE_VISIT;

    public NewsDetialViecFileAdapter(Context context, List<ViceFile> viceFileList, int mode) {
        this.newsMessageActivity = (NewsMessageActivity) context;
        this.viceFileList = viceFileList;
        this.mode = mode;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.url_tiem, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        holder.viceFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(newsMessageActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(newsMessageActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                }//申请权限
                showDialog(viceFileList.get(holder.getAdapterPosition()));
            }
        });
        holder.downLoadCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.RequestWithString2(RequestAdress.GET_VIEC_FILE_DOWNLOAD_DETAIL, "{\"filesId\":" + viceFileList.get(holder.getAdapterPosition()).getFileId() + "}", new Controller.OnRequestListener() {
                    @Override
                    public void onSuccess(String json) {
                        final List<DownloadDetial> detials = new Gson().fromJson(json, new TypeToken<List<DownloadDetial>>() {
                        }.getType());
                        Tool.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                showDonwloadDetialDialog(detials);
                            }
                        });
                    }

                    @Override
                    public void onFailed(int state) {

                    }
                });

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViceFile viceFile = viceFileList.get(position);
        holder.fileIcon.setImageResource(Tool.getFileIcon(viceFile.getFileName()));
        holder.viceFileName.setText(viceFile.getFileName());
        if (mode == NewsMessageActivity.MODE_VISIT) {
            holder.downLoadCount.setVisibility(View.GONE);
        } else {
            holder.downLoadCount.setText("被下载" + viceFile.getFileDownLoadTime() + "次");
            holder.downLoadCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return viceFileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView viceFileName;
        ImageView fileIcon;
        TextView downLoadCount;

        public ViewHolder(View itemView) {
            super(itemView);
            viceFileName = (TextView) itemView.findViewById(R.id.url);
            fileIcon = (ImageView) itemView.findViewById(R.id.data_type);
            downLoadCount = (TextView) itemView.findViewById(R.id.url_item_download_count);
        }
    }

    private void showDialog(final ViceFile viceFile) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(App.getActivityStack().lastElement());
        dialog.setTitle("下载附件");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                newsMessageActivity.binder.startDownload(viceFile);
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View view = LayoutInflater.from(newsMessageActivity).inflate(R.layout.url_tiem, null, false);
        TextView fileName = (TextView) view.findViewById(R.id.url);
        ImageView fileIcon = (ImageView) view.findViewById(R.id.data_type);
        fileName.setText(viceFile.getFileName());
        fileIcon.setImageResource(Tool.getFileIcon(viceFile.getFileName()));
        dialog.setView(view);
        dialog.show();
    }

    private void showDonwloadDetialDialog(List<DownloadDetial> list) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(App.getActivityStack().lastElement());
        dialog.setTitle("附件下载情况");
        dialog.setCancelable(true);
        View view = LayoutInflater.from(newsMessageActivity).inflate(R.layout.download_count_liat, null, false);
        ListView listView = (ListView) view.findViewById(R.id.download_detial_list);
        dialog.setView(view);
        listView.setAdapter(new DownloadDetialAdapter(App.getActivityStack().lastElement(), R.layout.download_detial_item, list));
        dialog.show();
    }
}
