/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.ProgressDialog
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.KeyEvent
 */
package com.redbend.dmClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class Connecting
extends DmcActivity {
    private final String LOG_TAG = "Connecting";
    private ProgressDialog dialog;

    @Override
    public void onCreate(Bundle bundle) {
        DmcLog.v("Connecting", "onCreate()");
        super.onCreate(bundle);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d("Connecting", "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    @Override
    public void onPause() {
        DmcLog.d("Connecting", "onPause()");
        super.onPause();
    }

    @Override
    public void onResume() {
        DmcLog.d("Connecting", "onResume()");
        super.onResume();
        this.dialog = ProgressDialog.show((Context)this, (CharSequence)"", (CharSequence)this.getString(2131099650), (boolean)true);
    }

    @Override
    protected void onStop() {
        this.dialog.cancel();
        super.onStop();
    }
}

