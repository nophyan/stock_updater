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

public class DmcTimeChangeReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "DmcTimeChangeReceiver";

    public void onReceive(Context context, Intent object) {
        DmcLog.v("DmcTimeChangeReceiver", "onReceive()");
        object = object.getAction();
        if (object.equals("android.intent.action.TIME_SET") || object.equals("android.intent.action.TIMEZONE_CHANGED")) {
            object = new Intent(context, (Class)DmcService.class);
            object.putExtra("serviceStartReason", 6);
            context.startService((Intent)object);
        }
    }
}

