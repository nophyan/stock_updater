/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.redbend.dmcFramework;

import android.util.Log;

public class DmcLog {
    public static final int LOG_DEBUG = 8;
    public static final int LOG_ERROR = 1;
    public static final int LOG_INFO = 4;
    public static final int LOG_SUPPRESS = 0;
    private static final String LOG_TAG = "FOTA ";
    public static final int LOG_VERBOSE = 16;
    public static final int LOG_WARNING = 2;
    public static final int output_mode = 1;

    public static void d(String string2, String string3) {
    }

    public static void e(String string2, String string3) {
        Log.e((String)("FOTA " + string2), (String)string3);
    }

    public static void f(String string2, String string3) {
        Log.i((String)("FOTA " + string2), (String)string3);
    }

    public static void i(String string2, String string3) {
    }

    public static void v(String string2, String string3) {
    }

    public static void w(String string2, String string3) {
    }
}

