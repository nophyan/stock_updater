/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 */
package com.redbend.dmClient;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;

public class Notification
extends Activity {
    private static String LOG_TAG = "Notification";

    public void onCreate(Bundle bundle) {
        DmcLog.v(LOG_TAG, "onCreate()");
        int n = this.getIntent().getIntExtra("notificationIdExtra", -1);
        super.onCreate(bundle);
        bundle = new Intent(this.getApplicationContext(), (Class)DmcService.class);
        bundle.putExtra("serviceStartReason", 3);
        bundle.putExtra("notificationIdExtra", n);
        this.startService((Intent)bundle);
    }
}

