package com.qg.qgnews.controller.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.model.Manager;

import java.util.List;

/**
 * Created by linzongzhan on 2017/7/30.
 */

public class ManagerPersonAdapter extends ArrayAdapter<Manager> {

    private int resourceId;


    public ManagerPersonAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Manager> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Manager manager = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name = (TextView) view.findViewById(R.id.manager_item_name);

        TextView dead = (TextView) view.findViewById(R.id.manager_item_status_dead);
        TextView wait = (TextView) view.findViewById(R.id.manager_item_status_wait);
        TextView pass = (TextView) view.findViewById(R.id.manager_item_status_pass);
        TextView nonactivated = (TextView) view.findViewById(R.id.manager_item_status_nonactivated);

        name.setText(manager.getManagerName());
        String managerStatus = manager.getManagerStatus();
        if (managerStatus.equals("待审批")) {
            wait.setVisibility(View.VISIBLE);
            dead.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
            nonactivated.setVisibility(View.GONE);
        } else if (managerStatus.equals("未激活")) {
            nonactivated.setVisibility(View.VISIBLE);
            dead.setVisibility(View.GONE);
            wait.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
        } else if (managerStatus.equals("正常")) {
            pass.setVisibility(View.VISIBLE);
            dead.setVisibility(View.GONE);
            wait.setVisibility(View.GONE);
            nonactivated.setVisibility(View.GONE);
        } else if (managerStatus.equals("被封号")) {
            dead.setVisibility(View.VISIBLE);
            wait.setVisibility(View.GONE);
            pass.setVisibility(View.GONE);
            nonactivated.setVisibility(View.GONE);
        }

        return view;
    }
}
