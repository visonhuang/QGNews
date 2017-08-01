package com.qg.qgnews.controller.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.model.DownloadDetial;
import com.qg.qgnews.model.News;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/31.
 */

public class DownloadDetialAdapter extends ArrayAdapter<DownloadDetial> {
    private int itemLayoutId;

    public DownloadDetialAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<DownloadDetial> objects) {
        super(context, resource, objects);
        itemLayoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        DownloadDetial detial = getItem(position);
        View itemView;
        ViewHolder holder;
        if (convertView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(itemLayoutId, parent, false);
            holder = new ViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            itemView = convertView;
            holder = (ViewHolder) itemView.getTag();
        }
        holder.time.setText(detial.getDownloadTime());
        holder.name.setText(detial.getDownloader());
        return itemView;
    }

    class ViewHolder {
        TextView name;
        TextView time;

        public ViewHolder(View itemView) {
            name = (TextView) itemView.findViewById(R.id.downloader_name);
            time = (TextView) itemView.findViewById(R.id.download_time);
        }
    }
}
