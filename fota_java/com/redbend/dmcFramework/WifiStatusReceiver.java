/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.net.wifi.WifiManager
 */
package com.redbend.dmcFramework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;

public class WifiStatusReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "WifiStatusReceiver";

    /*
     * Enabled aggressive block sorting
     */
    public void onReceive(Context context, Intent intent) {
        DmcLog.v("WifiStatusReceiver", "onReceive()");
        if (!((DmcApplication)context.getApplicationContext()).isBootCompleted()) return;
        {
            int n = ((WifiManager)context.getSystemService("wifi")).getWifiState();
            if (n == 1) {
                ((DmcApplication)context.getApplicationContext()).sendMessage(15, null);
                return;
            } else {
                if (n != 3) return;
                {
                    ((DmcApplication)context.getApplicationContext()).sendMessage(14, null);
                    return;
                }
            }
        }
    }
}

