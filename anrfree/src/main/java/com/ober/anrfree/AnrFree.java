package com.ober.anrfree;

import android.app.Application;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by ober on 8/21/22.
 */
public class AnrFree {

    public static final boolean DEBUG = true;

    private static AnrFree Instance;

    public static void initOnApplicationCreate(Application app) {
        Instance = new AnrFree(app);
        Instance.init(app);
    }

    public void registerBroadcastReceiverChecker(IntentFilter intentFilter) {
        mApp.registerReceiver(new AnrCheckReceiver(), intentFilter);
    }

    public static AnrFree getInstance() {
        return Instance;
    }

    public Handler getMainHandler() {
        return mMainHandler;
    }

    private Application mApp;
    private Handler mMainHandler;

    private MainThreadFrameUpdater mMainThreadFrameUpdater;
    private AppBackgroundDetector mAppBackgroundDetector;

    private AnrFree(Application app) {
        mApp = app;
        mMainHandler = new Handler(Looper.getMainLooper());
        mMainThreadFrameUpdater = new MainThreadFrameUpdater();
        mAppBackgroundDetector = new AppBackgroundDetector();
    }

    private Thread mGuardThread;
    private AnrGuardTask mGuardTask;

    private final AppBackgroundDetector.OnBackgroundStateChangeListener onBackgroundStateChangeListener = new AppBackgroundDetector.OnBackgroundStateChangeListener() {
        @Override
        public void onBackgroundStateChange(boolean b) {
            Log.i("[AnrFree]", "OnBackgroundStateChange " + b);
            if(b) {
                startWorkOnForeground();
            } else {
                stopWorkOnBackground();
            }
        }
    };

    private void init(Application app) {
        mAppBackgroundDetector.setStateChangeListener(onBackgroundStateChangeListener);
        mAppBackgroundDetector.initOnApplicationCreate(app);
        mMainThreadFrameUpdater.startUpdate();
        if(mAppBackgroundDetector.isForeground()) {
            startWorkOnForeground();
        }
    }

    private void startWorkOnForeground() {
        stopWorkOnBackground();
        mGuardTask = new AnrGuardTask(mMainThreadFrameUpdater);
        mGuardThread = new Thread(mGuardTask, "AnrGuard");

        mGuardTask.setAppBackgroundState(true);
        mGuardTask.setRunningFlag(true);

        mMainThreadFrameUpdater.update();

        mGuardThread.start();
    }

    private void stopWorkOnBackground() {
        if(mGuardTask != null && mGuardThread != null) {
            mGuardTask.setRunningFlag(false);
            mGuardTask.setAppBackgroundState(false);
            mGuardThread.interrupt();
            mGuardTask = null;
            mGuardThread = null;
        }
    }
}
