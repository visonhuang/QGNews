package com.qg.qgnews.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.qg.qgnews.R;
import com.qg.qgnews.ui.fragment.Login;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView titleText;

    ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        titleText = (TextView) findViewById(R.id.login_Title);

        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(false);


        replaceFragment(new Login(),"登录");
    }

    public void replaceFragment (Fragment fragment,String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_frameLayout,fragment);
        fragmentTransaction.commit();
        setTitle(title);
    }

    public void setTitle (String title) {
        titleText.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                getSupportFragmentManager().popBackStackImmediate();
                break;
            default:
        }
        return true;
    }
}
