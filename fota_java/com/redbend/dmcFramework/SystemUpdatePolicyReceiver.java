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

public class SystemUpdatePolicyReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "SystemUpdatePolicyReceiver";

    public void onReceive(Context context, Intent intent) {
        DmcLog.v("SystemUpdatePolicyReceiver", "onReceive()");
        intent = new Intent(context, (Class)DmcService.class);
        intent.putExtra("serviceStartReason", 7);
        context.startService(intent);
    }
}

