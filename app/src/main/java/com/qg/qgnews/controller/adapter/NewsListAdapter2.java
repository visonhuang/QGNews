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

import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.model.News;
import com.qg.qgnews.model.PicAsnycTask;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class NewsListAdapter2 extends ArrayAdapter<News> {
    private int itemLayoutId;

    public NewsListAdapter2(@NonNull Context context, @LayoutRes int resource, @NonNull List<News> objects) {
        super(context, resource, objects);
        itemLayoutId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);
        View itemView;
        ViewHolder holder;
        if (convertView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(itemLayoutId,parent,false);
            holder = new ViewHolder(itemView);
            itemView.setTag(holder);
        } else {
            itemView = convertView;
            holder = (ViewHolder) itemView.getTag();
        }

        new PicAsnycTask(holder.cover,getItem(position), App.bitmapLruCache).execute();
        holder.title.setText(news.getNewsTitle());
        holder.time.setText(news.getNewsTime());
        //TODO 显示新闻
        return itemView;
    }

    class ViewHolder {
        ImageView cover;
        TextView title;
        CardView card;
        TextView time;
        public ViewHolder(View itemView) {
            cover = (ImageView) itemView.findViewById(R.id.news_item_cover);
            title = (TextView) itemView.findViewById(R.id.news_item_title);
            card = (CardView) itemView.findViewById(R.id.news_item_card);
            time = (TextView) itemView.findViewById(R.id.news_item_time);
        }
    }
}
