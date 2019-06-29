/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 */
package com.redbend.dmClient;

import android.content.Context;
import android.content.SharedPreferences;
import com.redbend.dmcFramework.DmcLog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DeviceUtils {
    private static final String LOG_TAG = "DEVICE_UTILS";

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static String getFileText(String object) {
        object = new File((String)object);
        object = new InputStreamReader(new FileInputStream((File)object));
        char[] arrc = new char[1024];
        try {
            int n = object.read(arrc);
            object.close();
            return String.valueOf(arrc, 0, n).trim();
        }
        catch (IOException var0_1) {
            try {
                var0_1.printStackTrace();
                do {
                    return "";
                    break;
                } while (true);
            }
            catch (FileNotFoundException var0_2) {
                var0_2.printStackTrace();
                return "";
            }
        }
    }

    public static int getIntFromFile(String object) throws FileNotFoundException, IOException {
        object = new InputStreamReader(new FileInputStream(new File((String)object)));
        char[] arrc = new char[1024];
        int n = object.read(arrc);
        object.close();
        return Integer.parseInt(String.valueOf(arrc, 0, n).trim());
    }

    public static boolean isAutoInstall(Context context) {
        DmcLog.v("DEVICE_UTILS", "isAutoInstall()");
        context = context.getSharedPreferences("auto_install", 0);
        DmcLog.d("DEVICE_UTILS", "auto_install " + context.getBoolean("flag", true));
        return context.getBoolean("flag", true);
    }

    public static boolean isRestrainAutoInstall(Context context) {
        DmcLog.v("DEVICE_UTILS", "isRestrainAutoInstall()");
        context = context.getSharedPreferences("restrain_auto_install", 0);
        DmcLog.d("DEVICE_UTILS", "restrain_auto_install " + context.getBoolean("flag", true));
        return context.getBoolean("flag", true);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setAutoInstall(Context context, int n) {
        DmcLog.v("DEVICE_UTILS", "setAutoInstall()");
        context = context.getSharedPreferences("auto_install", 0).edit();
        if (n == 1) {
            DmcLog.d("DEVICE_UTILS", "auto_install set to true");
            context.putBoolean("flag", true);
        } else {
            DmcLog.d("DEVICE_UTILS", "auto_install set to false");
            context.putBoolean("flag", false);
        }
        context.commit();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void setRestrainAutoInstall(Context context, int n) {
        DmcLog.v("DEVICE_UTILS", "setRestrainAutoInstall()");
        context = context.getSharedPreferences("restrain_auto_install", 0).edit();
        if (n == 1) {
            DmcLog.d("DEVICE_UTILS", "restain_auto_install set to true");
            context.putBoolean("flag", true);
        } else {
            DmcLog.d("DEVICE_UTILS", "restain_auto_install set to false");
            context.putBoolean("flag", false);
        }
        context.commit();
    }
}

