package com.qg.qgnews.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qg.qgnews.R;
import com.qg.qgnews.ui.activity.LoginActivity;

/**
 * Created by 黄伟烽 on 2017/8/2.
 */

public class AdverFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adver1, container, false);
        LinearLayout linear = (LinearLayout) view.findViewById(R.id.linear_background);
        int number = getArguments().getInt("number");
        if(number == 1){
            linear.setBackgroundResource(R.drawable.adver_background1);
        }
        else if(number == 2){
            linear.setBackgroundResource(R.drawable.adver_background2);
        }
        else if(number == 3){
            linear.setBackgroundResource(R.drawable.adver_backgound3);
            Button button = (Button) view.findViewById(R.id.enter_button);
            button.setVisibility(View.VISIBLE);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            });
        }
        return view;
    }

    public static AdverFragment newInstance(int index) {
        AdverFragment f = new AdverFragment();
        Bundle args = new Bundle();
        args.putInt("number", index);
        f.setArguments(args);
        return f;
    }
}
