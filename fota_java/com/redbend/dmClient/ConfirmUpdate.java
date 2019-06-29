/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.Menu
 *  android.view.MenuItem
 */
package com.redbend.dmClient;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import java.io.File;

public class ConfirmUpdate
extends DmcActivity {
    private static final int MEMU_MAINTENANCE = 1;
    public static final String dmServerUrlExtra = "dmServerUrl";
    public static final String proxyServerUrlExtra = "proxyServerUrl";

    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "ConfirmUpdate";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903046);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        DmcLog.v(this.LOG_TAG, "onCreateOptionsMenu()");
        super.onCreateOptionsMenu(menu);
        if (new File("/data/local/tmp/redbend/mnt").exists()) {
            // empty if block
        }
        return true;
    }

    public boolean onKeyUp(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed");
            this.a.sendMessage(2, "BACK");
            return false;
        }
        return super.onKeyUp(n, keyEvent);
    }

    public boolean onMenuItemSelected(int n, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            default: {
                return super.onMenuItemSelected(n, menuItem);
            }
            case 1: 
        }
        this.a.sendMessage(2, "MENU");
        return true;
    }
}

