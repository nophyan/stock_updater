/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.ProgressBar
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class ProgressBarScreen
extends DmcActivity {
    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "ProgressBarScreen";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903057);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public void updateProgress(int n) {
        try {
            ProgressBar progressBar = (ProgressBar)this.findViewById(2131230733);
            if (progressBar.getProgress() != n) {
                progressBar.setProgress(n);
            }
            ((TextView)this.findViewById(2131230734)).setText((CharSequence)("" + n + "%"));
            return;
        }
        catch (RuntimeException var2_3) {
            DmcLog.e(this.LOG_TAG, "The screen is not initialized, but the updates from SM layer already coming");
            return;
        }
    }
}

