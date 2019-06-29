/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 */
package com.redbend.dmcFramework;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;
import com.redbend.dmcFramework.DmcUtils;

public class DmcAlarmReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "DMC_ALARM_RECEIVER";
    private final int WAKELOCK_DURATION = 20000;
    public DmcUtils u = null;

    public void onReceive(Context context, Intent intent) {
        DmcLog.v("DMC_ALARM_RECEIVER", "onReceive()");
        int n = intent.getIntExtra("alarmIdExtra", -1);
        this.u = new DmcUtils(context);
        if (n != -1) {
            this.u.acquireWakeLock(20000);
            Intent intent2 = new Intent(context, (Class)DmcService.class);
            intent2.putExtra("serviceStartReason", 5);
            intent2.putExtra("alarmIdExtra", intent.getIntExtra("alarmIdExtra", -1));
            context.startService(intent2);
        }
    }
}

