/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.Application
 *  android.app.Fragment
 *  android.app.FragmentManager
 *  android.app.FragmentTransaction
 *  android.content.Intent
 *  android.os.Bundle
 *  android.preference.EditTextPreference
 *  android.preference.Preference
 *  android.preference.PreferenceActivity
 *  android.preference.PreferenceFragment
 */
package com.redbend.dmClient;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

public class MaintenanceMode
extends PreferenceActivity {
    public static EditTextPreference prefDmServerHost;
    public static EditTextPreference prefProxyHost;
    protected String LOG_TAG = "MaintenanceMode";
    protected DmcApplication a;
    protected boolean finishOnStop = false;

    private String getForegroundProcessName() throws Exception {
        Object object;
        block1 : {
            Integer n;
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = null;
            Field field = ActivityManager.RunningAppProcessInfo.class.getDeclaredField("processState");
            Iterator iterator = ((ActivityManager)this.getSystemService("activity")).getRunningAppProcesses().iterator();
            do {
                object = runningAppProcessInfo;
                if (!iterator.hasNext()) break block1;
                object = (ActivityManager.RunningAppProcessInfo)iterator.next();
            } while (object.importance != 100 || object.importanceReasonCode != 0 || (n = Integer.valueOf(field.getInt(object))) == null || n != 2);
            object = object.processName;
        }
        return object;
    }

    protected void onCreate(Bundle bundle) {
        DmcLog.v(this.LOG_TAG, "onCreate()");
        this.a = (DmcApplication)this.getApplication();
        super.onCreate(bundle);
        this.overridePendingTransition(0, 0);
        this.getFragmentManager().beginTransaction().replace(16908290, (Fragment)new prefFragment()).commit();
        int n = this.getIntent().getIntExtra("nextStateExtra", -1);
        this.a.sendMessage(13, n);
    }

    protected void onPause() {
        DmcLog.v(this.LOG_TAG, "onPause()");
        super.onPause();
    }

    public void onResume() {
        DmcLog.v(this.LOG_TAG, "onResume()");
        super.onResume();
        String string2 = this.getIntent().getStringExtra("dmServerUrl");
        prefDmServerHost.setText(string2);
        string2 = this.getIntent().getStringExtra("proxyServerUrl");
        if (!string2.equalsIgnoreCase("NONE")) {
            prefProxyHost.setText(string2);
            return;
        }
        prefProxyHost.setText("");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    protected void onStop() {
        DmcLog.v(this.LOG_TAG, "onStop()");
        synchronized (this) {
            try {
                String string2 = this.getForegroundProcessName();
                if (string2 != null && string2.contains("com.redbend")) {
                    DmcLog.d(this.LOG_TAG, "DMC Activity paused by another DMC Actitivity. Do nothing.");
                } else {
                    DmcLog.d(this.LOG_TAG, "DMC Activity paused by non-DMC activity, so it should cancel the DM session and exit.");
                    int n = this.getIntent().getIntExtra("nextStateExtra", -1);
                    this.a.sendMessage(12, n);
                }
            }
            catch (Exception var2_2) {
                DmcLog.e(this.LOG_TAG, "Exception occured while gettting the foreground Activity name.");
            }
        }
        this.a.sendMessage(5015, prefDmServerHost.getText());
        this.a.sendMessage(5016, prefProxyHost.getText());
        this.a.sendMessage(2, "");
        super.onStop();
        if (this.finishOnStop) {
            this.finish();
        }
    }

    public static class prefFragment
    extends PreferenceFragment {
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);
            this.addPreferencesFromResource(2130968576);
            MaintenanceMode.prefDmServerHost = (EditTextPreference)this.findPreference((CharSequence)"dmsAddress");
            MaintenanceMode.prefProxyHost = (EditTextPreference)this.findPreference((CharSequence)"pxyAddress");
        }
    }

}

