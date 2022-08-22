package com.ober.anrfree;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by ober on 8/22/22.
 */
class ChildThreadDelayTask implements Runnable {

    private final long mDelayTime;
    private Runnable mCallback;

    private final AtomicBoolean cancelFlag;

    public ChildThreadDelayTask(long delayTime, Runnable callback) {
        mDelayTime = delayTime;
        mCallback = callback;
        cancelFlag = new AtomicBoolean(false);
    }

    public void cancel() {
        cancelFlag.set(true);
        mCallback = null;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(mDelayTime);
        } catch (InterruptedException e) {
            return;
        }
        if(cancelFlag.get()) {
            return;
        }
        if(mCallback != null) {
            mCallback.run();
        }
    }
}
