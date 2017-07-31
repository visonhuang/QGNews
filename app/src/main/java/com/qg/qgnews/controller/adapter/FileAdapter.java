package com.qg.qgnews.controller.adapter;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.ui.activity.PublishNewsActivity;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by 黄伟烽 on 2017/7/29.
 */

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder>{

    private List<String> mMyFileList;

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView fileImage;
        private TextView fileNameText;
        private TextView fileSizeText;

        public ViewHolder(View view) {
            super(view);
            fileImage = (ImageView) view.findViewById(R.id.file_image);
            fileNameText = (TextView) view.findViewById(R.id.pic_name);
            fileSizeText = (TextView) view.findViewById(R.id.size_text);
        }
    }

    public FileAdapter(List<String> myFileList) {
        mMyFileList = myFileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.startAnimation(AnimationUtils.loadAnimation(parent.getContext(), R.anim.on_click));
            }
        });
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(App.getActivityStack().lastElement())
                        .setMessage("是否移除该附件")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(PublishNewsActivity.mIsPublishing){
                                    return;
                                }
                                mMyFileList.remove(holder.getAdapterPosition());
                                Tool.toast("移除附件成功");
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return  true;
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String filePath = mMyFileList.get(position);
        File file = new File(filePath);
        holder.fileImage.setImageResource(Tool.getFileIcon(file));
        holder.fileNameText.setText(getFileName(filePath));
        holder.fileSizeText.setText(getFileSize(filePath));
    }

    private String getFileName(String filePath) {
        int index = filePath.lastIndexOf("/");
        String fileName = filePath.substring(index + 1);
        return fileName;
    }

    @Override
    public int getItemCount() {
        return mMyFileList.size();
    }

    public void freshFileList(List<String> fileList){
        mMyFileList = fileList;
    }

    public static String getFileSize(String filename) {
        File file = new File(filename);
        if (!file.exists() || !file.isFile()) {
            System.out.println("文件不存在");
            return null;
        }
        long bLength = file.length();
        if (bLength < 1024){
            return bLength + "B";
        }
        double kbLength = bLength / 1024;
        DecimalFormat df = new DecimalFormat( "0.00");
        if (kbLength < 1024){
            return df.format(kbLength) + "KB";
        }
        double mbLength = kbLength / 1024;
        if (mbLength < 1024){
            return df.format(mbLength) + "MB";
        }
        double gbLength = mbLength / 1024;
        if (gbLength < 1024){
            return df.format(gbLength) + "GB";
        }
        return null;
    }
}