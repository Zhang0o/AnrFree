package com.ober.anrfree;

import android.os.SystemClock;
import android.util.Log;
import android.view.Choreographer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ober on 8/21/22.
 */
class MainThreadFrameUpdater {

    private final Object TimeLock = new Object();

    private long mLastUpdateTime;

    private final Choreographer mChoreographer;

    private AtomicBoolean isRunning = new AtomicBoolean();

    public MainThreadFrameUpdater() {
        mChoreographer = Choreographer.getInstance();
    }

    public void startUpdate() {
        isRunning.set(true);
        update();
        tryScheduleNext();
    }

    public void stopUpdate() {
        isRunning.set(false);
    }

    public long getLastUpdateTime() {
        synchronized (TimeLock) {
            return mLastUpdateTime;
        }
    }

    public void update() {
        synchronized (TimeLock) {
            mLastUpdateTime = SystemClock.elapsedRealtime();
        }
    }

    public boolean isRunning() {
        return isRunning.get();
    }

    private void tryScheduleNext() {
        if(isRunning.get()) {
            mChoreographer.postFrameCallback(new UpdateTask(this));
        }
    }

    private static class UpdateTask implements Choreographer.FrameCallback {
        private final MainThreadFrameUpdater mParent;
        UpdateTask(MainThreadFrameUpdater parent) {
            mParent = parent;
        }

        @Override
        public void doFrame(long frameTimeNanos) {
            mParent.update();
            mParent.tryScheduleNext();
        }
    }
}
