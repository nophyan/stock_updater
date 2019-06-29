/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.Application
 *  android.app.ProgressDialog
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Bundle
 *  android.os.Process
 */
package com.redbend.dmcFramework;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;
import com.redbend.dmcFramework.DmcUtils;

public class DmcMain
extends Activity {
    protected String LOG_TAG = "DmcMain";
    private DmcApplication a;
    private ProgressDialog dialog;

    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "DmcMain";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        if (!new DmcUtils((Context)this).isOwnerUser()) {
            DmcLog.d(this.LOG_TAG, "The user is not owner. Calling finish().");
            this.finish();
            DmcLog.d(this.LOG_TAG, "The user is not owner. DMClient is not started.");
            Process.killProcess((int)Process.myPid());
        }
    }

    public void onResume() {
        this.LOG_TAG = "DmcMain";
        DmcLog.v(this.LOG_TAG, "onResume()");
        super.onResume();
        this.dialog = ProgressDialog.show((Context)this, (CharSequence)"", (CharSequence)this.getString(2131099650), (boolean)true);
        this.a = (DmcApplication)this.getApplication();
        Intent intent = new Intent(this.getApplicationContext(), (Class)DmcService.class);
        intent.putExtra("serviceStartReason", 1);
        SharedPreferences sharedPreferences = this.getSharedPreferences("notif", 0);
        DmcLog.d(this.LOG_TAG, "sp.getInt" + sharedPreferences.getInt("notif_type", 0));
        if (sharedPreferences.getInt("notif_type", 0) != 0) {
            intent.putExtra("serviceStartNotif", sharedPreferences.getInt("notif_type", 0));
            sharedPreferences = sharedPreferences.edit();
            sharedPreferences.putInt("notif_type", 0);
            sharedPreferences.commit();
        }
        this.a.isDmcMainStarted = true;
        this.startService(intent);
    }

    protected void onStop() {
        this.LOG_TAG = "DmcMain";
        DmcLog.v(this.LOG_TAG, "onStop()");
        this.dialog.cancel();
        super.onStop();
    }
}

