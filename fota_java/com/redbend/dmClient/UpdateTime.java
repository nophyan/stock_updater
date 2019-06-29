/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.TimeUtils;

public class UpdateTime
extends DmcActivity {
    private static final String LOG_TAG = "UpdateTime";

    @Override
    public void onCreate(Bundle bundle) {
        DmcLog.v("UpdateTime", "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903075);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d("UpdateTime", "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    @Override
    public void onResume() {
        super.onResume();
        TextView textView = (TextView)this.findViewById(2131230739);
        String string2 = TimeUtils.getInstallTimeSlot(this.mContext);
        textView.setText((CharSequence)String.format(this.getString(2131099729), string2));
    }
}

