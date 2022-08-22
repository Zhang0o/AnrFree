package com.ober.anrfree;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import java.util.HashSet;

/**
 * Created by ober on 8/21/22.
 */
public class AppBackgroundDetector implements Application.ActivityLifecycleCallbacks {

    public interface OnBackgroundStateChangeListener {
        @MainThread
        void onBackgroundStateChange(boolean b);
    }

    private boolean mIsForeground;

    private OnBackgroundStateChangeListener mStateChangeListener;

    private HashSet<Activity> startedActivitySet;

    @MainThread
    public boolean isForeground() {
        return mIsForeground;
    }

    @MainThread
    public void setStateChangeListener(OnBackgroundStateChangeListener listener) {
        mStateChangeListener = listener;
    }

    @MainThread
    public void initOnApplicationCreate(Application application) {
        startedActivitySet = new HashSet<>();
        mIsForeground = ProcessLifecycleOwner.get().getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        application.registerActivityLifecycleCallbacks(this);
    }

    private void onStateChanged(boolean foreground) {
        if(foreground) {
            if(!mIsForeground) {
                mIsForeground = true;
                if(mStateChangeListener != null) {
                    mStateChangeListener.onBackgroundStateChange(true);
                }
            }
        } else {
            if(mIsForeground) {
                mIsForeground = false;
                if(mStateChangeListener != null) {
                    mStateChangeListener.onBackgroundStateChange(false);
                }
            }
        }
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        int oldCount = startedActivitySet.size();
        startedActivitySet.add(activity);
        if(oldCount == 0) {
            onStateChanged(true);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {

    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if(startedActivitySet.remove(activity)) {
            if(startedActivitySet.size() == 0) {
                onStateChanged(false);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if(startedActivitySet.remove(activity)) {
            if(startedActivitySet.size() == 0) {
                onStateChanged(false);
            }
        }
    }
}
