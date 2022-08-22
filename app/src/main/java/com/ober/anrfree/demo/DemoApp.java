package com.ober.anrfree.demo;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import com.ober.anrfree.AnrFree;

/**
 * Created by ober on 8/22/22.
 */
public class DemoApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AnrFree.initOnApplicationCreate(this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        AnrFree.getInstance().registerBroadcastReceiverChecker(filter);
    }

}
