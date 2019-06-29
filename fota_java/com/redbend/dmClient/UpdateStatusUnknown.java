/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 */
package com.redbend.dmClient;

import android.os.Bundle;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class UpdateStatusUnknown
extends DmcActivity {
    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "UpdateStatusUnknown";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903074);
    }
}

