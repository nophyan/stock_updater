/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.KeyEvent
 */
package com.redbend.dmClient;

import android.os.Bundle;
import android.view.KeyEvent;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class InRoaming
extends DmcActivity {
    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "InRoaming";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903050);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }
}

