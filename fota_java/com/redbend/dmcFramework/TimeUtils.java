/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.annotation.SuppressLint
 *  android.app.admin.DevicePolicyManager
 *  android.app.admin.SystemUpdatePolicy
 *  android.content.Context
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.text.format.DateFormat
 */
package com.redbend.dmcFramework;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateFormat;
import com.redbend.dmcFramework.DmcLog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

public class TimeUtils {
    private static final String TAG = "TimeUtils";
    private static final String defaultInstallScheduleHh = "DEF_INSTALL_SCHED_HH";
    private static final String defaultInstallScheduleMm = "DEF_INSTALL_SCHED_MM";
    private static final int defaultTimeInvalide = -1;
    private static final int defaultTimeOfHh = -1;
    private static final int defaultTimeOfInitialInstall = -1;
    private static final int defaultTimeOfMm = -1;
    private static final int indexOfTimeHh = 0;
    private static final int indexOfTimeMm = 1;
    private static final String initialInstallTime = "INITIAL_INSTALL_TIME";
    private static final String installScheduleHh = "INSTALL_SCHED_HH";
    private static final String installScheduleMm = "INSTALL_SCHED_MM";
    private static final String installScheduleName = "install_sched";
    private static final String randomInstallScheduleMm = "RANDOM_INSATLL_SCHED_MM";

    private static int currentSystemUpdatePolicy() {
        try {
            int n = TimeUtils.getCurrentSystemUpdatePolicy();
            return n;
        }
        catch (UnsatisfiedLinkError var1_1) {
            DmcLog.v("TimeUtils", "Exception catched. The activity may have been instanciated by the platform before the library is initialized.");
            return 0;
        }
    }

    public static native int getCurrentSystemUpdatePolicy();

    public static int getDefaultScheduleHour(Context context) {
        return TimeUtils.getDefaultScheduleTime(context)[0] % 24;
    }

    public static int getDefaultScheduleMinute(Context context) {
        return TimeUtils.getDefaultScheduleTime(context)[1];
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private static int[] getDefaultScheduleTime(Context arrstring) {
        int n;
        SharedPreferences sharedPreferences = arrstring.getSharedPreferences("install_sched", 0);
        int n2 = sharedPreferences.getInt("DEF_INSTALL_SCHED_HH", -1);
        int n3 = sharedPreferences.getInt("DEF_INSTALL_SCHED_MM", -1);
        DmcLog.d("TimeUtils", "getDefaultScheduleTime: mHour is " + n2 + " mMinute is " + n3);
        if (TimeUtils.currentSystemUpdatePolicy() == 2) {
            n = n2;
            if (n2 == -1) {
                arrstring = TimeUtils.getTimeSlotsOfDay((Context)arrstring);
                arrstring = arrstring[TimeUtils.getRandomNumber(0, arrstring.length - 1)].trim().split(":", 0);
                String[] arrstring2 = arrstring[1].split(" - ", 0);
                n = n2 = Integer.parseInt(arrstring[2]);
                if (n2 == 0) {
                    n = 59;
                }
                n2 = Integer.parseInt(arrstring[0]);
                n3 = TimeUtils.getRandomNumber(Integer.parseInt(arrstring2[0]), n);
                TimeUtils.setScheduleTime(sharedPreferences, "DEF_INSTALL_SCHED_HH", n2);
                TimeUtils.setScheduleTime(sharedPreferences, "DEF_INSTALL_SCHED_MM", n3);
                DmcLog.d("TimeUtils", "Default update time for Windowed updated policy@" + String.format("[%02d:%02d]", n2, n3));
                n = n2;
            }
            do {
                return new int[]{n, n3};
                break;
            } while (true);
        }
        n = n2;
        if (n2 == -1) {
            n = TimeUtils.getRandomNumber(1, 5);
            TimeUtils.setScheduleTime(sharedPreferences, "DEF_INSTALL_SCHED_HH", n);
        }
        DmcLog.d("TimeUtils", "Default update time @" + String.format("[%02d:00 - %02d:00]", n, n + 1));
        return new int[]{n, n3};
    }

    public static int getHrFromIndex(String[] arrstring, int n) {
        if (n < 0 || n > arrstring.length) {
            DmcLog.d("TimeUtils", "Index is out of range: total=" + arrstring.length + "index=" + n);
            return 0;
        }
        return Integer.parseInt(arrstring[n].trim().split(":", 0)[0]);
    }

    public static long getInitialInstallTime(Context context) {
        DmcLog.d("TimeUtils", "getInitialInstallTime");
        return context.getSharedPreferences("install_sched", 0).getLong("INITIAL_INSTALL_TIME", -1);
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Calendar getInstallCalendar(Context object) {
        int n;
        int n2;
        block2 : {
            object = TimeUtils.getInstallScheduleTime((Context)object);
            int n3 = object[0];
            n2 = object[1];
            object = GregorianCalendar.getInstance();
            int n4 = object.get(11);
            n = object.get(12);
            if (n3 != n4 || n2 > n) {
                n = n3;
                if (n3 >= n4) break block2;
            }
            n = n3 + 24;
        }
        object.set(11, n);
        object.set(12, n2);
        DmcLog.i("TimeUtils", "Install time @" + DateFormat.format((CharSequence)"yyyy-MM-dd kk:mm", (Calendar)object).toString());
        return object;
    }

    public static int getInstallScheduleHour(Context context) {
        return TimeUtils.getInstallScheduleTime(context)[0] % 24;
    }

    public static int getInstallScheduleMinute(Context context) {
        return TimeUtils.getInstallScheduleTime(context)[1];
    }

    /*
     * Enabled aggressive block sorting
     */
    private static int[] getInstallScheduleTime(Context arrstring) {
        SharedPreferences sharedPreferences = arrstring.getSharedPreferences("install_sched", 0);
        int n = sharedPreferences.getInt("INSTALL_SCHED_HH", -1);
        int n2 = sharedPreferences.getInt("INSTALL_SCHED_MM", -1);
        String[] arrstring2 = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        DmcLog.d("TimeUtils", "getInstallScheduleTime: mHour is " + n + " mMinute is " + n2);
        int n3 = n;
        if (TimeUtils.getInitialInstallTime((Context)arrstring) != -1) {
            n3 = n;
            if (n == -1) {
                arrstring2.setTimeInMillis(System.currentTimeMillis());
                calendar.setTimeInMillis(TimeUtils.getInitialInstallTime((Context)arrstring));
                n3 = calendar.get(6) - 1 == arrstring2.get(6) ? calendar.get(11) + 24 : calendar.get(11);
                DmcLog.d("TimeUtils", "getInstallScheduleTime: mHour is " + n3);
            }
        }
        n = n3;
        if (n3 == -1) {
            n = TimeUtils.getDefaultScheduleHour((Context)arrstring) + 24;
        }
        if (n2 == -1) {
            n3 = sharedPreferences.getInt("RANDOM_INSATLL_SCHED_MM", -1);
            if (n3 == -1) {
                if (TimeUtils.currentSystemUpdatePolicy() == 2) {
                    arrstring2 = TimeUtils.getTimeSlotsOfDay((Context)arrstring);
                    arrstring = arrstring2[TimeUtils.getTimeSlotIndex(arrstring2, TimeUtils.getDefaultScheduleHour((Context)arrstring), TimeUtils.getDefaultScheduleMinute((Context)arrstring))].trim().split(":", 0);
                    arrstring2 = arrstring[1].split(" - ", 0);
                    n3 = n2 = Integer.parseInt(arrstring[2]);
                    if (n2 == 0) {
                        n3 = 59;
                    }
                    n3 = TimeUtils.getRandomNumber(Integer.parseInt(arrstring2[0]), n3);
                } else {
                    n3 = TimeUtils.getRandomNumber(0, 59);
                }
                TimeUtils.setScheduleTime(sharedPreferences, "RANDOM_INSATLL_SCHED_MM", n3);
                DmcLog.f("TimeUtils", "Random install time @" + String.format("%02d:%02d", n % 24, n3));
                return new int[]{n, n3};
            } else {
                DmcLog.f("TimeUtils", "Stored random time @" + String.format("%02d:%02d", n % 24, n3));
            }
            return new int[]{n, n3};
        } else {
            DmcLog.f("TimeUtils", "Stored install time @" + String.format("%02d:%02d", n % 24, n2));
            n3 = n2;
        }
        return new int[]{n, n3};
    }

    public static long getInstallTimeDelta(Context context) {
        Calendar calendar = TimeUtils.getInstallCalendar(context);
        if (Long.valueOf((context = context.getSharedPreferences("install_sched", 0)).getLong("INITIAL_INSTALL_TIME", -1)) == -1) {
            DmcLog.i("TimeUtils", "Save current system time");
            context = context.edit();
            context.putLong("INITIAL_INSTALL_TIME", calendar.getTimeInMillis());
            context.commit();
        }
        long l = (calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000;
        DmcLog.i("TimeUtils", "Install time @+" + l + " seconds");
        return Math.max(l, 10);
    }

    @SuppressLint(value={"DefaultLocale"})
    public static String getInstallTimeSlot(Context context) {
        if (TimeUtils.currentSystemUpdatePolicy() == 2) {
            String[] arrstring = TimeUtils.getTimeSlotsOfDay(context);
            return arrstring[TimeUtils.getTimeSlotIndex(arrstring, TimeUtils.getInstallScheduleHour(context), TimeUtils.getInstallScheduleMinute(context))].trim();
        }
        int n = TimeUtils.getInstallScheduleHour(context);
        return String.format("%02d:00 - %02d:00", n % 24, (n + 1) % 24);
    }

    public static int getMinFromIndex(String[] arrstring, int n) {
        if (n < 0 || n > arrstring.length) {
            DmcLog.d("TimeUtils", "Index is out of range: total=" + arrstring.length + "index=" + n);
            return 0;
        }
        if (TimeUtils.currentSystemUpdatePolicy() == 2) {
            int n2;
            arrstring = arrstring[n].trim().split(":", 0);
            String[] arrstring2 = arrstring[1].split(" - ", 0);
            n = n2 = Integer.parseInt(arrstring[2]);
            if (n2 == 0) {
                n = 59;
            }
            return TimeUtils.getRandomNumber(Integer.parseInt(arrstring2[0]), n);
        }
        return -1;
    }

    public static int getRandomNumber(int n, int n2) {
        return new Random(new Date().getTime()).nextInt(Math.abs(n - n2) + 1) + Math.min(n, n2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static int getTimeSlotIndex(String[] arrstring, int n, int n2) {
        int n3 = 0;
        int n4 = 0;
        do {
            int n5;
            int n6 = n3;
            if (n4 >= arrstring.length) return n6;
            String[] arrstring2 = arrstring[n4].trim().split(":", 0);
            String[] arrstring3 = arrstring2[1].split(" - ", 0);
            n6 = n5 = Integer.parseInt(arrstring2[2]);
            if (n5 == 0) {
                n6 = 59;
            }
            if (Integer.parseInt(arrstring2[0]) == n) {
                if (n2 == -1) return n4;
                if (n2 >= Integer.parseInt(arrstring3[0]) && n2 <= n6) {
                    return n4;
                }
            }
            ++n4;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static String[] getTimeSlotsOfDay(Context var0) {
        if (TimeUtils.currentSystemUpdatePolicy() == 2 && (var0 = ((DevicePolicyManager)var0.getSystemService("device_policy")).getSystemUpdatePolicy()) != null && (var5_1 = var0.getInstallWindowStart()) != (var1_2 = var0.getInstallWindowEnd())) {
            var13_3 = var5_1 / 60;
            if (var13_3 != 0) {
                var5_1 %= var13_3 * 60;
            }
            if ((var6_4 = var1_2 / 60) != 0) {
                var1_2 %= var6_4 * 60;
            }
        } else {
            var0 = new String[24];
            var1_2 = 0;
            while (var1_2 < var0.length) {
                var0[var1_2] = String.format("   %02d:00 - %02d:00   ", new Object[]{var1_2 % 24, (var1_2 + 1) % 24});
                ++var1_2;
            }
            return var0;
        }
        var9_5 = var13_3;
        var0 = new ArrayList<E>();
        var7_6 = false;
        var8_7 = 0;
        while (var8_7 < 25) {
            if (var7_6 != false) return var0.toArray(new String[var0.size()]);
            var10_11 = 0;
            var11_12 = 0;
            var14_14 = var9_5 % 24;
            var12_13 = (var9_5 + 1) % 24;
            if (var8_7 == 0) {
                var10_11 = var5_1;
            }
            var2_8 = var12_13;
            var3_9 = var7_6;
            var4_10 = var11_12;
            if (var14_14 != var6_4) ** GOTO lbl48
            if (var13_3 != var6_4) ** GOTO lbl41
            if (var5_1 < var1_2) ** GOTO lbl-1000
            var2_8 = var12_13;
            var3_9 = var7_6;
            var4_10 = var11_12;
            if (var8_7 > 0) lbl-1000: // 2 sources:
            {
                var2_8 = var6_4;
                var4_10 = var1_2;
                var3_9 = true;
            }
            ** GOTO lbl48
lbl41: // 1 sources:
            var2_8 = var12_13;
            var3_9 = var7_6;
            var4_10 = var11_12;
            if (var1_2 != 0) {
                var2_8 = var6_4;
                var4_10 = var1_2;
                var3_9 = true;
            }
lbl48: // 5 sources:
            var12_13 = var2_8;
            var7_6 = var3_9;
            var11_12 = var4_10;
            if (var2_8 == var6_4) {
                var12_13 = var2_8;
                var7_6 = var3_9;
                var11_12 = var4_10;
                if (var4_10 == var1_2) {
                    var12_13 = var6_4;
                    var11_12 = var1_2;
                    var7_6 = true;
                }
            }
            var0.add(String.format("   %02d:%02d - %02d:%02d   ", new Object[]{var14_14, var10_11, var12_13, var11_12}));
            ++var9_5;
            ++var8_7;
        }
        return var0.toArray(new String[var0.size()]);
    }

    public static void resetScheduleTime(Context context) {
        context = context.getSharedPreferences("install_sched", 0).edit();
        context.remove("RANDOM_INSATLL_SCHED_MM");
        context.remove("INSTALL_SCHED_HH");
        context.remove("INSTALL_SCHED_MM");
        context.remove("INITIAL_INSTALL_TIME");
        context.remove("DEF_INSTALL_SCHED_HH");
        context.remove("DEF_INSTALL_SCHED_MM");
        context.commit();
    }

    public static void rmInstallScheduleTime(Context context) {
        context = context.getSharedPreferences("install_sched", 0).edit();
        context.remove("RANDOM_INSATLL_SCHED_MM");
        context.remove("INSTALL_SCHED_HH");
        context.remove("INSTALL_SCHED_MM");
        context.remove("INITIAL_INSTALL_TIME");
        context.commit();
    }

    public static void setDefaultScheduleTime(Context context, int n, int n2) {
        TimeUtils.setDefaultScheduleTime(context.getSharedPreferences("install_sched", 0), n, n2);
    }

    private static void setDefaultScheduleTime(SharedPreferences sharedPreferences, int n, int n2) {
        sharedPreferences = sharedPreferences.edit();
        sharedPreferences.putInt("DEF_INSTALL_SCHED_HH", n);
        sharedPreferences.putInt("DEF_INSTALL_SCHED_MM", n2);
        sharedPreferences.putInt("RANDOM_INSATLL_SCHED_MM", n2);
        sharedPreferences.commit();
    }

    public static void setInstallScheduleTime(Context context, int n, int n2) {
        TimeUtils.setInstallScheduleTime(context.getSharedPreferences("install_sched", 0), n, n2);
    }

    private static void setInstallScheduleTime(SharedPreferences sharedPreferences, int n, int n2) {
        sharedPreferences = sharedPreferences.edit();
        sharedPreferences.putInt("INSTALL_SCHED_HH", n);
        sharedPreferences.putInt("INSTALL_SCHED_MM", n2);
        sharedPreferences.putInt("RANDOM_INSATLL_SCHED_MM", n2);
        sharedPreferences.commit();
    }

    private static void setScheduleTime(SharedPreferences sharedPreferences, String string2, int n) {
        sharedPreferences = sharedPreferences.edit();
        sharedPreferences.putInt(string2, n);
        sharedPreferences.commit();
    }
}

