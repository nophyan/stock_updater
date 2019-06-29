/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.SharedPreferences
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class AutoSetting
extends DmcActivity {
    private boolean isAutoInstallSet() {
        DmcLog.v(this.LOG_TAG, "isAutoInstallSet()");
        return this.getSharedPreferences("auto_install", 0).getBoolean("flag", true);
    }

    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "AutoSetting";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
    }

    @Override
    public void onResume() {
        DmcLog.v(this.LOG_TAG, "onResume()");
        super.onResume();
        this.setContentView(2130903040);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setContentView(int n) {
        DmcLog.d(this.LOG_TAG, "setContentView()");
        super.setContentView(n);
        TextView textView = (TextView)this.findViewById(2131230721);
        StringBuilder stringBuilder = new StringBuilder().append(this.getString(2131099725));
        n = this.isAutoInstallSet() ? 2131099726 : 2131099727;
        textView.setText((CharSequence)stringBuilder.append(this.getString(n)).toString());
    }
}

