package com.ober.anrfree.demo.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.Random;

/**
 * Created by ober on 8/21/22.
 */
public class MainThreadSleep {

    public static void mainThreadSleepTask(final long time) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                Log.i("[AnrDemo]", "start work MainThreadSleep");
                long endTime = System.currentTimeMillis() + time;
                while (true) {
                    if(System.currentTimeMillis() > endTime) {
                        break;
                    }
                    try {
                        Thread.sleep(new Random().nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.i("[AnrDemo]", "end work MainThreadSleep");
            }
        };
        new Handler(Looper.getMainLooper()).post(r);
    }

}
