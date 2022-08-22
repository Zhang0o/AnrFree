package com.ober.anrfree.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.ober.anrfree.R;
import com.ober.anrfree.demo.util.BroadcastSleep;
import com.ober.anrfree.demo.util.MainThreadSleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.work_0).setOnClickListener(this);
        findViewById(R.id.work_1).setOnClickListener(this);
        findViewById(R.id.work_2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.work_0:
                MainThreadSleep.mainThreadSleepTask(9000);
                break;
            case R.id.work_1:
                BroadcastSleep.registerOffscreenBlockReceiver(this);
                break;
            case R.id.work_2:
                BroadcastSleep.registerScreenOnBlockReceiver(this);
                break;
        }
    }
}