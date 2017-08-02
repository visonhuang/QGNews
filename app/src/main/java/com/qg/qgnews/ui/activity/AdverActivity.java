package com.qg.qgnews.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.AdverPagerAdapter;

public class AdverActivity extends AppCompatActivity {

    private ImageView mPointImage;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adver);
        SharedPreferences pre = getSharedPreferences("isFirst", MODE_PRIVATE);
        boolean isFirst = pre.getBoolean("isFirst", false);
        if(!isFirst){
            SharedPreferences.Editor editor = getSharedPreferences("isFirst", MODE_PRIVATE).edit();
            editor.putBoolean("isFirst", true);
            editor.apply();
        }else {
            Intent intent = new Intent(AdverActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        final ImageView mPointImage = (ImageView) findViewById(R.id.progress_point);
        mViewPager = (ViewPager) findViewById(R.id.adver_view_pager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    mPointImage.setImageResource(R.drawable.ic_one);
                }
                else if(position == 1){
                    mPointImage.setImageResource(R.drawable.ic_two);
                }
                else if(position == 2){
                    mPointImage.setImageResource(R.drawable.ic_three);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        AdverPagerAdapter adapter = new AdverPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
    }
}
