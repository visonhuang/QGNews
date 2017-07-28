package com.qg.qgnews.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.R;

import java.io.File;
import java.util.List;

/**
 * 项目名： DownLoader
 * 创建人： 小吉哥哥
 * 创建人： 2017/4/11.
 */

public class UrlListAdapter extends ArrayAdapter<File> {
    private int layoutId;

    public UrlListAdapter(Context context, int resource, List<File> objects) {
        super(context, resource, objects);
        layoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        File file = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.url);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.data_type);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText("\t\t" + file.getPath().substring(file.getPath().lastIndexOf('/') + 1));
        String[] fileInfo = file.toString().split("\\.");
        if (file.isDirectory()) {
            viewHolder.imageView.setImageResource(R.drawable.ic_folder);
        } else if (fileInfo.length == 2 && fileInfo[1].equals("mp3")) {
            viewHolder.imageView.setImageResource(R.drawable.ic_mp3_two);
        } else if (fileInfo.length == 2 && fileInfo[1].equals("txt")) {
            viewHolder.imageView.setImageResource(R.drawable.ic_txt);
        } else if (fileInfo.length == 2 && fileInfo[1].equals("dat")) {
            viewHolder.imageView.setImageResource(R.drawable.ic_dat);
        } else if (fileInfo.length == 2 && fileInfo[1].equals("rmvb")) {
            viewHolder.imageView.setImageResource(R.drawable.ic_rmvb);
        } else if (fileInfo.length == 2 && fileInfo[1].equals("apk")) {
            viewHolder.imageView.setImageResource(R.drawable.ic_apk);
        } else {
            viewHolder.imageView.setImageResource(R.drawable.ic_unknow);
        }
        return view;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
