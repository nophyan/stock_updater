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

public class VerifyingPackageProgress
extends DmcActivity {
    private static String LOG_TAG = "VerifyingPackageProgress";

    @Override
    public void onCreate(Bundle bundle) {
        DmcLog.v(LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903077);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(LOG_TAG, "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }
}

