/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Activity
 *  android.app.ActivityManager
 *  android.app.ActivityManager$RunningAppProcessInfo
 *  android.app.Application
 *  android.content.Context
 *  android.content.Intent
 *  android.content.res.Configuration
 *  android.content.res.Resources
 *  android.os.Bundle
 *  android.os.Handler
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 */
package com.redbend.dmcFramework;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import com.redbend.dmClient.R;
import com.redbend.dmcFramework.DMCButton;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcMain;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DmcActivity
extends Activity {
    protected String LOG_TAG = "DmcActivity";
    protected DmcApplication a;
    private ArrayList<DMCButton> buttons;
    private boolean flagOnRotation;
    private int layoutId;
    protected Context mContext = null;

    private void disableAllButtons() {
        DmcLog.v(this.LOG_TAG, "disableAllButtons()");
        Iterator<DMCButton> iterator = this.buttons.iterator();
        while (iterator.hasNext()) {
            iterator.next().setClickable(false);
        }
    }

    private void enableAllButtons() {
        DmcLog.v(this.LOG_TAG, "enableAllButtons()");
        Iterator<DMCButton> iterator = this.buttons.iterator();
        while (iterator.hasNext()) {
            iterator.next().setClickable(true);
        }
    }

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

    public native boolean isVdmCancelRequested();

    protected void onActivityResult(int n, int n2, Intent intent) {
        DmcLog.v(this.LOG_TAG, "onActivityResult()");
        super.onActivityResult(n, n2, intent);
    }

    public void onConfigurationChanged(Configuration configuration) {
        DmcLog.v(this.LOG_TAG, "onConfigurationChanged()");
        if (this.getResources().getConfiguration().orientation == 2 || this.getResources().getConfiguration().orientation == 1) {
            this.flagOnRotation = true;
        }
        super.onConfigurationChanged(configuration);
        this.setContentView(this.layoutId);
    }

    public void onContentChanged() {
        DmcLog.v(this.LOG_TAG, "onContentChanged()");
        if (!this.flagOnRotation) {
            // empty if block
        }
    }

    public void onCreate(Bundle bundle) {
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.mContext = this.getApplicationContext();
        this.overridePendingTransition(0, 0);
        this.a = (DmcApplication)this.getApplication();
        this.a.currentActivity = this;
        this.buttons = new ArrayList<E>();
        this.flagOnRotation = false;
    }

    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed");
            return super.onKeyDown(n, keyEvent);
        }
        return super.onKeyDown(n, keyEvent);
    }

    protected void onPause() {
        DmcLog.v(this.LOG_TAG, "onPause()");
        super.onPause();
    }

    public void onResume() {
        DmcLog.v(this.LOG_TAG, "onResume()");
        super.onResume();
        if (this.a.serviceHandler == null) {
            DmcLog.d(this.LOG_TAG, "Start DmcMain");
            this.startActivity(new Intent(this.getApplicationContext(), (Class)DmcMain.class));
            return;
        }
        this.a = (DmcApplication)this.getApplication();
        this.a.lastResumedActivity = this;
        int n = this.getIntent().getIntExtra("nextStateExtra", -1);
        DmcLog.d(this.LOG_TAG, "onResume() nextState=" + n);
        this.a.sendMessage(13, n);
        this.flagOnRotation = false;
        this.enableAllButtons();
        if (this.isVdmCancelRequested()) {
            DmcLog.d(this.LOG_TAG, "VDM is in process of cancelling the session.");
            this.setVisible(false);
            this.finish();
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    protected void onStop() {
        DmcLog.v(this.LOG_TAG, "onStop() started");
        super.onStop();
        synchronized (this) {
            try {
                String string2 = this.getForegroundProcessName();
                if (string2 != null && string2.contains("com.redbend")) {
                    DmcLog.d(this.LOG_TAG, "DMC Activity paused by another DMC Actitivity. Do nothing.");
                    do {
                        return;
                        break;
                    } while (true);
                }
                DmcLog.d(this.LOG_TAG, "DMC Activity paused by non-DMC activity");
                int n = this.getIntent().getIntExtra("nextStateExtra", -1);
                this.a.sendMessage(12, n);
                return;
            }
            catch (Exception var2_2) {
                DmcLog.e(this.LOG_TAG, "Exception occured while gettting the foreground Activity name.");
                return;
            }
            finally {
            }
        }
    }

    public void setContentView(int n) {
        block5 : {
            Field[] arrfield;
            DmcLog.v(this.LOG_TAG, "setContentView()");
            this.layoutId = n;
            super.setContentView(this.layoutId);
            try {
                arrfield = R.id.class.getDeclaredFields();
                n = 0;
            }
            catch (Throwable var2_3) {
                DmcLog.e(this.LOG_TAG, "Error occured due to Android bug");
            }
            do {
                block6 : {
                    if (n >= arrfield.length) break block5;
                    Object object = super.findViewById(arrfield[n].getInt(null));
                    if (object == null) break block6;
                    if (!(object instanceof Button)) break block6;
                    object = new DMCButton((Button)object);
                    this.buttons.add((DMCButton)((Object)object));
                    object.setTag(arrfield[n].getName());
                    object.setOnClickListener(new View.OnClickListener(){

                        public void onClick(View view) {
                            DmcActivity.this.disableAllButtons();
                            DmcActivity.this.a.sendMessage(2, view.getTag().toString());
                        }
                    });
                }
                ++n;
            } while (true);
        }
    }

}

