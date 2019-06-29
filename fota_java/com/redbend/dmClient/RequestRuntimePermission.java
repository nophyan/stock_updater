/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Bundle
 *  android.view.KeyEvent
 *  android.view.View
 *  android.view.View$OnClickListener
 *  android.widget.Button
 *  android.widget.TextView
 */
package com.redbend.dmClient;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.redbend.dmClient.ipl.DmcIplImpl;
import com.redbend.dmClient.ipl.IplFactory;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcUtils;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class RequestRuntimePermission
extends DmcActivity {
    private Button btView;
    private String[] requestedPermissions = null;

    @Override
    public void onCreate(Bundle bundle) {
        this.LOG_TAG = "RequestRuntimePermission";
        DmcLog.v(this.LOG_TAG, "onCreate()");
        super.onCreate(bundle);
        this.setContentView(2130903058);
        this.btView = (Button)this.findViewById(2131230723);
        this.btView.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view) {
                RequestRuntimePermission.this.requestPermissions(RequestRuntimePermission.this.requestedPermissions, 0);
            }
        });
    }

    @Override
    public boolean onKeyDown(int n, KeyEvent keyEvent) {
        if (n == 4) {
            DmcLog.d(this.LOG_TAG, "back button pressed, ignore");
            return false;
        }
        return super.onKeyDown(n, keyEvent);
    }

    public void onRequestPermissionsResult(int n, String[] arrstring, int[] arrn) {
        this.finish();
    }

    @Override
    public void onResume() {
        int n;
        DmcLog.v(this.LOG_TAG, "onResume()");
        super.onResume();
        int n2 = 0;
        int n3 = 4;
        Object object = new DmcUtils(this.getApplicationContext());
        int n4 = object.getRuntimePermissionState();
        DmcLog.v(this.LOG_TAG, "currentPermissions: " + n4);
        DmcIplImpl dmcIplImpl = IplFactory.create(this.mContext);
        if (!dmcIplImpl.isExternalStorageUsed()) {
            n3 = 3;
        }
        for (n = 0; n < n3; ++n) {
            int n5 = n2;
            if ((n4 & 1) == 0) {
                n5 = n2 + 1;
            }
            n4 >>>= 1;
            n2 = n5;
        }
        this.requestedPermissions = new String[n2];
        n4 = object.getRuntimePermissionState();
        object = this.getString(2131099734);
        Object object2 = (String)object + "\n\n";
        String string2 = this.getString(2131099736) + "\n";
        n = 0;
        String string3 = string2;
        object = object2;
        if ((n4 & 2) == 0) {
            object = (String)object2 + string2;
            this.requestedPermissions[0] = "android.permission.RECEIVE_WAP_PUSH";
            string3 = "";
            n = 1;
        }
        n3 = n;
        object2 = object;
        if ((n4 & 4) == 0) {
            object2 = (String)object + string3;
            this.requestedPermissions[n] = "android.permission.RECEIVE_SMS";
            n3 = n + 1;
        }
        n = n3;
        object = object2;
        if ((n4 & 1) == 0) {
            object = (String)object2 + this.getString(2131099735) + "\n";
            this.requestedPermissions[n3] = "android.permission.READ_PHONE_STATE";
            n = n3 + 1;
        }
        n3 = n;
        object2 = object;
        if ((n4 & 8) == 0) {
            n3 = n;
            object2 = object;
            if (dmcIplImpl.isExternalStorageUsed()) {
                object2 = (String)object + this.getString(2131099737);
                this.requestedPermissions[n] = "android.permission.WRITE_EXTERNAL_STORAGE";
                n3 = n + 1;
            }
        }
        if (n3 == 0) {
            this.finish();
        }
        ((TextView)this.findViewById(2131230720)).setText((CharSequence)object2);
    }

    @Override
    public void setContentView(int n) {
        DmcLog.v(this.LOG_TAG, "setContentView()");
        super.setContentView(n);
    }

}

