/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.dmcFramework;

import com.redbend.dmcFramework.DmcLog;

public class Hex {
    private static final String LOG_TAG = "HEX";

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public byte[] Decode(String string2) {
        int n;
        DmcLog.v("HEX", "Decode() started");
        int n2 = n = string2.length() / 2;
        if (string2.length() % 2 == 1) {
            n2 = n + 1;
        }
        byte[] arrby = new byte[n2];
        n2 = 0;
        while (n2 < string2.length()) {
            String string3;
            try {
                string3 = string2.substring(n2, n2 + 2);
            }
            catch (Exception var4_6) {
                string3 = string2.substring(n2);
            }
            try {
                arrby[n2 / 2] = (byte)Integer.parseInt(string3, 16);
            }
            catch (Exception var4_7) {
                DmcLog.e("HEX", "Exception: " + var4_7.toString());
            }
            n2 += 2;
        }
        return arrby;
    }
}

