/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.TextView
 */
package com.redbend.dmcFramework;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class DmcConfirmationQuery
extends DmcActivity {
    private String confirmationText;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.LOG_TAG = "DmcConfirmationQuery";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        this.confirmationText = this.getIntent().getStringExtra("uiAlertTextExtra");
        this.setContentView(2130903047);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    @Override
    public void setContentView(int n) {
        DmcLog.d(this.LOG_TAG, "setContentView()");
        super.setContentView(n);
        ((TextView)this.findViewById(2131230720)).setText((CharSequence)this.confirmationText);
    }
}

