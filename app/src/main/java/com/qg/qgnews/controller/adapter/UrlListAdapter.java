package com.qg.qgnews.controller.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.ui.activity.FileSelector;
import com.qg.qgnews.util.Tool;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 项目名： DownLoader
 * 创建人： 小吉哥哥
 * 创建人： 2017/4/11.
 */

public class UrlListAdapter extends ArrayAdapter<File> {
    private int layoutId;
    private int mode;
    private Map<String, File> selectedFiles;
    private int maxSelected;

    public UrlListAdapter(Context context, int resource, List<File> objects, int mode, Map<String, File> map, int maxSelected) {
        super(context, resource, objects);
        this.mode = mode;
        layoutId = resource;
        selectedFiles = map;
        this.maxSelected = maxSelected;
    }

    public interface OnSelectedFilesChangedListener {
        void onChanged();
    }

    private OnSelectedFilesChangedListener onSelectedFilesChangedListener = null;

    public void setOnSelectedFilesChangedListener(OnSelectedFilesChangedListener onSelectedFilesChangedListener) {
        this.onSelectedFilesChangedListener = onSelectedFilesChangedListener;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final File file = getItem(position);
        View view;
        final ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.url);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.data_type);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.url_item_checkBox);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(file.getPath().substring(file.getPath().lastIndexOf('/') + 1));

        //设置图标
        String[] fileInfo = file.toString().split("\\.");
        viewHolder.imageView.setImageResource(Tool.getFileIcon(file));

        //按模式显示
        if (mode == FileSelector.MODE_FILE) {
            if (file.isDirectory()) {
                viewHolder.checkBox.setVisibility(View.GONE);
            } else {
                viewHolder.checkBox.setVisibility(View.VISIBLE);
            }
            if (!file.isDirectory()) {
                if (selectedFiles.get(file.getPath()) != null) {
                    viewHolder.checkBox.setChecked(true);
                } else {
                    viewHolder.checkBox.setChecked(false);
                }
            }

            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (viewHolder.checkBox.isChecked()) {
                        if (selectedFiles.size() >= maxSelected) {
                            Tool.toast("已达到上限");
                            viewHolder.checkBox.setChecked(false);
                            return;
                        }
                        selectedFiles.put(file.getPath(), file);
                    } else {
                        selectedFiles.remove(file.getPath());
                    }
                    if (onSelectedFilesChangedListener != null) {
                        onSelectedFilesChangedListener.onChanged();
                    }
                }
            });
        }
        return view;
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
        CheckBox checkBox;
    }
}
