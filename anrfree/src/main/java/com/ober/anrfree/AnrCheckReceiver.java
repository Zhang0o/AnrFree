package com.ober.anrfree;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ober on 8/22/22.
 */
public class AnrCheckReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(AnrFree.DEBUG) {
            Log.d("[AnrFree]", "onReceive " + intent.getAction());
        }
        runDelayTask();
    }

    private static void runDelayTask() {

        final ChildThreadDelayTask childThreadDelayTask = new ChildThreadDelayTask(5000, new Runnable() {
            @Override
            public void run() {
                Log.e("[AnrFree]", "anr detect in receiver");
            }
        });
        new Thread(childThreadDelayTask, "ReceiverCheck");
        AnrFree.getInstance().getMainHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                childThreadDelayTask.cancel();
            }
        }, 1000);
    }

}
