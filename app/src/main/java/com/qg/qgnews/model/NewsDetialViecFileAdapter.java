package com.qg.qgnews.model;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.util.Tool;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/30.
 */

public class NewsDetialViecFileAdapter extends RecyclerView.Adapter<NewsDetialViecFileAdapter.ViewHolder> {
    Context context;
    List<ViceFile> viceFileList;

    public NewsDetialViecFileAdapter(Context context, List<ViceFile> viceFileList) {
        this.context = context;
        this.viceFileList = viceFileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.url_tiem, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        holder.viceFileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(viceFileList.get(holder.getAdapterPosition()));
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ViceFile viceFile = viceFileList.get(position);
        holder.fileIcon.setImageResource(Tool.getFileIcon(viceFile.getFileName()));
        holder.viceFileName.setText(viceFile.getFileName());
    }

    @Override
    public int getItemCount() {
        return viceFileList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView viceFileName;
        ImageView fileIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            viceFileName = (TextView) itemView.findViewById(R.id.url);
            fileIcon = (ImageView) itemView.findViewById(R.id.data_type);
        }
    }

    private void showDialog(ViceFile viceFile) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(App.getActivityStack().lastElement());
        dialog.setTitle("下载附件");
        dialog.setCancelable(false);
        dialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        View view = LayoutInflater.from(context).inflate(R.layout.url_tiem, null, false);
        TextView fileName = (TextView) view.findViewById(R.id.url);
        ImageView fileIcon = (ImageView) view.findViewById(R.id.data_type);
        fileName.setText(viceFile.getFileName());
        fileIcon.setImageResource(Tool.getFileIcon(viceFile.getFileName()));
        dialog.setView(view);
        dialog.show();
    }
}
