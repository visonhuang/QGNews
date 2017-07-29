package com.qg.qgnews.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 黄伟烽 on 2017/7/29.
 */

public class FileAdapter extends RecyclerView.Adapter{

    private List<String> fileList;

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView fileImage;


        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
