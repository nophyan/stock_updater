/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Intent
 *  android.os.Bundle
 *  android.view.View
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;

public class ShowUnreadInfoMessage
extends DmcActivity {
    private Bundle bundle;
    private String messageText;

    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "ShowUnreadInfoMessage";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.bundle = this.getIntent().getBundleExtra("notificationObjExtra");
        this.messageText = (String)this.bundle.get("msg");
        this.setContentView(2130903051);
    }

    @Override
    public void setContentView(int n) {
        DmcLog.v(this.LOG_TAG, "setContentView()");
        super.setContentView(n);
        TextView textView = (TextView)this.findViewById(2131230720);
        if (this.messageText == null) {
            textView.setText((CharSequence)this.getString(2131099689));
            return;
        }
        textView.setText((CharSequence)this.messageText);
    }
}

