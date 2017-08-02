package com.qg.qgnews.controller.adapter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.IntDef;
import android.util.Log;

import com.qg.qgnews.model.RequestAdress;
import com.qg.qgnews.util.Request;

public class HeartBeatService extends Service {
    public HeartBeatService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Log.d("==========","asdasdwdqwd12312312");
                    Request.heartBeat(RequestAdress.HEART_BEAT);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
