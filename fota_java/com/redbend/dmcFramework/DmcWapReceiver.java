/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.telephony.SmsMessage
 */
package com.redbend.dmcFramework;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;
import com.redbend.dmcFramework.Hex;

public class DmcWapReceiver
extends BroadcastReceiver {
    private static final String LOG_TAG = "DMC_WAP_RECEIVER";

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public void onReceive(Context var1_1, Intent var2_2) {
        block5 : {
            DmcLog.v("DMC_WAP_RECEIVER", "onReceive()");
            var5_3 = null;
            var4_4 = null;
            DmcLog.d("DMC_WAP_RECEIVER", "onReceive() input param intent.getAction():" + var2_2.getAction());
            var6_5 = var2_2.getExtras();
            if (!var2_2.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) ** GOTO lbl27
            var6_5 = (Object[])var6_5.get("pdus");
            var2_2 = var5_3;
            if (var6_5 == null) ** GOTO lbl28
            var2_2 = var5_3;
            if (var6_5.length > 0) {
                var5_3 = new SmsMessage[var6_5.length];
                var3_6 = 0;
                do {
                    var2_2 = var4_4;
                    if (var3_6 < var6_5.length) {
                        var5_3[var3_6] = SmsMessage.createFromPdu((byte[])((byte[])var6_5[var3_6]), (String)"3gpp");
                        if (var5_3[var3_6].getMessageBody().startsWith("WAPPUSH ")) {
                            var2_2 = var5_3[var3_6].getMessageBody().replaceFirst("WAPPUSH ", "");
                            var4_4 = new Hex().Decode((String)var2_2);
                            DmcLog.i("DMC_WAP_RECEIVER", "SMS message received: " + (String)var2_2 + "(" + var4_4.length + ")");
                        }
                        ++var3_6;
                        continue;
                    } else {
                        ** GOTO lbl-1000
                    }
                    break;
                } while (true);
            }
            ** GOTO lbl28
lbl-1000: // 2 sources:
            {
                break block5;
            }
lbl27: // 1 sources:
            var2_2 = (byte[])var6_5.get("data");
        }
        if (var2_2 == null) {
            DmcLog.e("DMC_WAP_RECEIVER", "msg is empty");
            return;
        }
        var4_4 = new byte[](var1_1, (Class)DmcService.class);
        var4_4.putExtra("serviceStartReason", 2);
        var4_4.putExtra("wapPushMessageExtra", (byte[])var2_2);
        var1_1.startService((Intent)var4_4);
    }
}

