/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.preference.DialogPreference
 *  android.preference.EditTextPreference
 *  android.preference.Preference
 *  android.util.AttributeSet
 */
package com.redbend.dmClient;

import android.content.Context;
import android.preference.DialogPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.util.AttributeSet;
import com.redbend.dmClient.ClientService;
import com.redbend.dmcFramework.DmcLog;

public class DmcMaintenanceResetDialog
extends DialogPreference {
    private static final String LOG_TAG = "MtResetDialog";

    public DmcMaintenanceResetDialog(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void onDialogClosed(boolean bl) {
        DmcLog.v("MtResetDialog", "Dialog Closed");
        if (bl) {
            String string2;
            String string3 = ClientService.getOriginalDmsAddress();
            if (!string3.equals("NONE")) {
                ((EditTextPreference)this.findPreferenceInHierarchy("dmsAddress")).setText(string3);
            }
            string3 = string2 = ClientService.getOriginalProxy();
            if (string2.equalsIgnoreCase("NOPROXY")) {
                string3 = "";
            }
            if (!string3.equals("NONE")) {
                ((EditTextPreference)this.findPreferenceInHierarchy("pxyAddress")).setText(string3);
            }
        }
        super.onDialogClosed(bl);
    }
}

