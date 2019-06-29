/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.BroadcastReceiver
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.os.Handler
 *  android.os.Message
 */
package com.redbend.dmcFramework;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.ScreenLockReceiver;

public abstract class DmcApplication
extends Application {
    public static final String LOG_TAG = "DmcApplication";
    private boolean bootFlag = false;
    public DmcActivity currentActivity;
    public boolean isDmcMainStarted = false;
    public DmcActivity lastResumedActivity;
    private BroadcastReceiver lockReceiver;
    public Handler serviceHandler;

    public abstract String getClientServiceClassName();

    public boolean isBootCompleted() {
        return this.bootFlag;
    }

    public void onCreate() {
        DmcLog.v("DmcApplication", "onCreate()");
        super.onCreate();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_OFF");
        intentFilter.addAction("android.intent.action.USER_PRESENT");
        this.lockReceiver = new ScreenLockReceiver();
        this.registerReceiver(this.lockReceiver, intentFilter);
    }

    public void onTerminate() {
        DmcLog.d("DmcApplication", "onTerminate()");
        super.onTerminate();
        this.unregisterLockReceiver();
    }

    public void sendMessage(int n, Object object) {
        if (this.serviceHandler == null) {
            DmcLog.d("DmcApplication", "serviceHandler is not ready, skip");
            return;
        }
        Message message = this.serviceHandler.obtainMessage();
        message.obj = object;
        message.what = n;
        this.serviceHandler.sendMessage(message);
    }

    public void setBootFlag(boolean bl) {
        DmcLog.v("DmcApplication", "setBootFlag() started" + bl);
        this.bootFlag = bl;
    }

    public void unregisterLockReceiver() {
        this.unregisterReceiver(this.lockReceiver);
    }
}

