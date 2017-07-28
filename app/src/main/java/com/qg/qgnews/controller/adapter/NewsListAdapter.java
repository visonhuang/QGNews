package com.qg.qgnews.controller.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.model.News;

import java.util.List;

/**
 * Created by 小吉哥哥 on 2017/7/28.
 */

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    List<News> list;
    Context context;

    public NewsListAdapter(List<News> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.OnItemClick(holder.itemView, holder.getAdapterPosition(), list.get(holder.getAdapterPosition()));
                }
            }
        });
        return holder;
    }

    public interface OnItemClickListener {
        void OnItemClick(View v, int pos, News news);
    }

    private OnItemClickListener onItemClickListener = null;

    /**
     * 设置item 点击时间监听器
     *
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cover;
        TextView title;
        CardView card;

        public ViewHolder(View itemView) {
            super(itemView);
            cover = (ImageView) itemView.findViewById(R.id.news_item_cover);
            title = (TextView) itemView.findViewById(R.id.news_item_title);
            card = (CardView) itemView.findViewById(R.id.news_item_card);
        }
    }
}
