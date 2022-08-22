package com.ober.anrfree;

import android.os.SystemClock;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ober on 8/21/22.
 */
class AnrGuardTask implements Runnable {

    private static final boolean IS_DEBUG = AnrFree.DEBUG;

    private static final long TOLERANCE_BLOCK_TIME = 8000;

    private final MainThreadFrameUpdater mMainUpdater;

    private final Object Lock = new Object();

    private AtomicBoolean isForeground = new AtomicBoolean();

    private AtomicBoolean runningFlag = new AtomicBoolean();

    public AnrGuardTask(MainThreadFrameUpdater updater) {
        mMainUpdater = updater;
    }

    public void setAppBackgroundState(boolean isForeground) {
        this.isForeground.set(isForeground);
    }

    public void setRunningFlag(boolean runningFlag) {
        this.runningFlag.set(runningFlag);
    }

    private boolean runningFlag() {
        return runningFlag.get();
    }

    @Override
    public void run() {
        if(IS_DEBUG) {
            Log.i("[AnrGuardTask]", "start run");
        }
        while (true) {
            if(!runningFlag.get()) {
                break;
            }

            if(mMainUpdater.isRunning() && isForeground.get()) {
                long curTime = SystemClock.elapsedRealtime();
                long lastMainUpdateTime = mMainUpdater.getLastUpdateTime();
                if(curTime - lastMainUpdateTime > TOLERANCE_BLOCK_TIME) {
                    if(IS_DEBUG) {
                        Log.e("[AnrGuardTask]", "ANR block time=" + (curTime - lastMainUpdateTime));
                        System.exit(0);
                    }
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.w("[AnrGuardTask]", "Interrupted");
                break;
            }
        }
    }
}
