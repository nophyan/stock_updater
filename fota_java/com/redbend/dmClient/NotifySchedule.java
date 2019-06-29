/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.TimeUtils;

public class NotifySchedule
extends DmcActivity {
    private static final String LOG_TAG = "NotifySchedule";

    @Override
    public void onCreate(Bundle bundle) {
        DmcLog.v("NotifySchedule", "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903055);
    }

    @Override
    public void setContentView(int n) {
        DmcLog.v("NotifySchedule", "setContentView()");
        super.setContentView(n);
        ((TextView)this.findViewById(2131230731)).setText((CharSequence)TimeUtils.getInstallTimeSlot(this.mContext));
    }
}

