package com.qg.qgnews.ui.activity;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.qg.qgnews.App;
import com.qg.qgnews.R;
import com.qg.qgnews.controller.adapter.DownloadDetialAdapter;
import com.qg.qgnews.controller.adapter.HeartBeatService;
import com.qg.qgnews.model.DownloadDetial;
import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.ui.fragment.Login;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private TextView titleText;

    ActionBar actionBar;

    public static final int REGISTER = 1;

    public static final int FORGETPASSWORD = 2;

    public static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }//申请权限
        setContentView(R.layout.activity_login);
        toolbar = (Toolbar) findViewById(R.id.login_toolbar);
        titleText = (TextView) findViewById(R.id.login_Title);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBar.setDisplayHomeAsUpEnabled(false);


        replaceFragment(new Login(), "登录");
    }

    public void replaceFragment(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_frameLayout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        setTitle(title);
    }

    public void setTitle(String title) {
        titleText.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getSupportFragmentManager().popBackStackImmediate();
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(false);
                setTitle("登录");
                break;
            case R.id.login_menu_set:
                showDialog();
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        if (!titleText.getText().toString().equals("登录")) {
            getSupportFragmentManager().popBackStackImmediate();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
            setTitle("登录");
        } else {
            finish();
        }

    }
    /**
     * 权限请求结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                finish();
            }
        }
    }
    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(App.getActivityStack().lastElement());
        dialog.setTitle("设置ip.port");
        dialog.setCancelable(true);
        View view = LayoutInflater.from(this).inflate(R.layout.set_host, null, false);
        TextInputLayout inputLayout = (TextInputLayout) view.findViewById(R.id.set_port_et);
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                RequestAdress.HOST = inputLayout.getEditText().getText().toString();
                SharedPreferences.Editor editor = getSharedPreferences("ip",MODE_PRIVATE).edit();
                editor.putString("ip",inputLayout.getEditText().getText().toString());
                editor.apply();
            }
        });
        dialog.setView(view);
        dialog.show();
    }
}
