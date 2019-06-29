/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 */
package com.redbend.dmcFramework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;

public class ScreenLockReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "ScreenLockReceiver";

    public void onReceive(Context context, Intent intent) {
        DmcLog.v("ScreenLockReceiver", "onReceive()");
        if (!((DmcApplication)context.getApplicationContext()).isBootCompleted()) {
            DmcLog.d("ScreenLockReceiver", "Screen lock/unlock event received before DmcService is up&running. It is ignored.");
        }
    }
}

