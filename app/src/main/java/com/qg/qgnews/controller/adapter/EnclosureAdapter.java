package com.qg.qgnews.controller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.model.ViceFile;

import java.util.List;

/**
 * Created by linzongzhan on 2017/7/28.
 */

public class EnclosureAdapter extends RecyclerView.Adapter<EnclosureAdapter.ViewHolder> {

    private List<ViceFile> mViceFileList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        View layoutView;

        public ViewHolder(View view) {
            super(view);

            layoutView = view;
            name = (TextView) view.findViewById(R.id.enclosure_name);
        }
    }

    public EnclosureAdapter (List<ViceFile> ViceFileList) {
        mViceFileList = ViceFileList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.enclosure,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        //设置点击事件
        viewHolder.layoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = viewHolder.getAdapterPosition();
                ViceFile viceFile = mViceFileList.get(position);

                //下载事件

            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String enclosureName = mViceFileList.get(position).getFileName();
        holder.name.setText(enclosureName);
    }

    @Override
    public int getItemCount() {
        return mViceFileList.size();
    }
}
