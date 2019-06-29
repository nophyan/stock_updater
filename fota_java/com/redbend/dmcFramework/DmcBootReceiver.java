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

public class DmcBootReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "DmcBootReceiver";

    public void onReceive(Context context, Intent intent) {
        DmcLog.v("DmcBootReceiver", "onReceive()");
        intent = new Intent(context, (Class)DmcService.class);
        intent.putExtra("serviceStartReason", 4);
        context.startService(intent);
    }
}

