/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.text.format.DateFormat
 *  android.view.KeyEvent
 *  android.view.View
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.TimeUtils;
import java.util.Calendar;
import java.util.Locale;

public class UpdateNowAutomatic
extends DmcActivity {
    private static final String LOG_TAG = "UpdateNowAutomatic";

    private String getInstallDate() {
        String string2;
        DmcLog.v("UpdateNowAutomatic", "getInstallDate()");
        Locale locale = Locale.getDefault();
        String string3 = string2 = DateFormat.format((CharSequence)"MMM dd", (Calendar)TimeUtils.getInstallCalendar(this.mContext)).toString();
        if (locale.getLanguage().equalsIgnoreCase("ja")) {
            string3 = string2 + this.getString(2131099733);
        }
        DmcLog.v("UpdateNowAutomatic", "getInstallDate() returning: " + string3);
        return string3;
    }

    @Override
    public void onCreate(Bundle bundle) {
        DmcLog.v("UpdateNowAutomatic", "onCreate()");
        super.onCreate(bundle);
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d("UpdateNowAutomatic", "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    @Override
    public void onResume() {
        DmcLog.v("UpdateNowAutomatic", "onResume()");
        super.onResume();
        this.setContentView(2130903071);
    }

    @Override
    public void setContentView(int n) {
        DmcLog.v("UpdateNowAutomatic", "setContentView()");
        super.setContentView(n);
        TextView textView = (TextView)this.findViewById(2131230721);
        String string2 = TimeUtils.getInstallTimeSlot(this.mContext);
        textView.setText((CharSequence)(this.getInstallDate() + " " + string2));
    }
}

