package com.ober.anrfree.demo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

/**
 * Created by ober on 8/22/22.
 */
public class BroadcastSleep {


    public static void registerOffscreenBlockReceiver(Context context) {
        BroadcastReceiver b = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("[AnrDemo]", "onReceive ACTION_SCREEN_OFF and block");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        context.registerReceiver(b, filter);
    }

    public static void registerScreenOnBlockReceiver(Context context) {
        BroadcastReceiver b = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("[AnrDemo]", "onReceive ACTION_SCREEN_ON and block");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(b, filter);
    }
}
