package com.qg.qgnews.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qg.qgnews.R;


/**
 * Created by linzongzhan on 2017/7/28.
 */

public class NewsMessage extends Fragment {

    /**
     * fragment的构造方法，用于将新闻的数据缓存起来
     */
  //  public NewsMessage ()

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_message,container,false);






        return view;
    }
}
