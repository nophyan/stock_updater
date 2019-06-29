/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.ActivityManager
 *  android.app.AlarmManager
 *  android.app.AlarmManager$AlarmClockInfo
 *  android.app.Notification
 *  android.app.Notification$BigTextStyle
 *  android.app.Notification$Builder
 *  android.app.Notification$Style
 *  android.app.NotificationManager
 *  android.app.PendingIntent
 *  android.app.admin.DevicePolicyManager
 *  android.app.admin.SystemUpdatePolicy
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.wifi.WifiManager
 *  android.net.wifi.WifiManager$WifiLock
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.os.Process
 *  android.os.SystemClock
 *  android.os.UserHandle
 *  android.os.UserManager
 *  android.telephony.PhoneStateListener
 *  android.telephony.ServiceState
 *  android.telephony.TelephonyManager
 */
package com.redbend.dmcFramework;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.app.admin.SystemUpdatePolicy;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.Process;
import android.os.SystemClock;
import android.os.UserHandle;
import android.os.UserManager;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import com.redbend.dmcFramework.DmcAlarmReceiver;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DmcUtils {
    private static final String LOG_TAG = "DmcUtils";
    private static PowerManager.WakeLock mWakeLock;
    private static WifiManager.WifiLock mWifiLock;
    private Context ctx;
    private PhoneStateListener mPhoneStateListener;
    private ServiceState mServiceState = null;
    private Timer timer;

    public DmcUtils(Context context) {
        this.mPhoneStateListener = new PhoneStateListener(){

            public void onServiceStateChanged(ServiceState serviceState) {
                DmcUtils.this.mServiceState = serviceState;
                DmcLog.d("DmcUtils", "onServiceStateChanged is " + DmcUtils.this.mServiceState.getState());
            }
        };
        DmcLog.d("DmcUtils", "DmcUtils()");
        this.ctx = context;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean isInService(TelephonyManager telephonyManager) {
        boolean bl = false;
        boolean bl2 = false;
        this.mServiceState = null;
        telephonyManager.listen(this.mPhoneStateListener, 1);
        this.waitServiceStateNotified();
        telephonyManager.listen(this.mPhoneStateListener, 0);
        if (this.mServiceState != null) {
            int n = this.mServiceState.getState();
            if (n == 0) {
                DmcLog.d("DmcUtils", "ServiceState is STATE_IN_SERVICE");
                bl = true;
            } else {
                DmcLog.d("DmcUtils", "ServiceState is not STATE_IN_SERVICE, state " + n);
                bl = bl2;
            }
            this.mServiceState = null;
        }
        return bl;
    }

    private boolean isNetworkConnected(int n) {
        boolean bl = false;
        NetworkInfo networkInfo = ((ConnectivityManager)this.ctx.getSystemService("connectivity")).getActiveNetworkInfo();
        if (networkInfo == null) {
            return false;
        }
        if (!networkInfo.isConnected()) {
            return false;
        }
        if (networkInfo.getType() == n) {
            bl = true;
        }
        DmcLog.d("DmcUtils", "isNetworkConnected with type " + n + ", returning:" + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void waitServiceStateNotified() {
        long l = SystemClock.elapsedRealtime();
        while (SystemClock.elapsedRealtime() < 3000 + l) {
            synchronized (this) {
                try {
                    this.wait(1);
                }
                catch (InterruptedException var3_2) {
                    DmcLog.e("DmcUtils", "sleep was interrupted");
                }
                if (this.mServiceState == null) continue;
            }
        }
    }

    public String GetPackageName() {
        return this.ctx.getPackageName();
    }

    public void acquireWakeLock(int n) {
        DmcLog.i("DmcUtils", String.format("acquire wakelock %d mills", n));
        if (mWakeLock == null) {
            mWakeLock = ((PowerManager)this.ctx.getSystemService("power")).newWakeLock(1, "DmcUtils");
            mWakeLock.setReferenceCounted(false);
        }
        if (n < 0) {
            mWakeLock.acquire();
            return;
        }
        mWakeLock.acquire((long)n);
    }

    public void acquireWifiLock() {
        DmcLog.i("DmcUtils", String.format("acquire WifiLock", new Object[0]));
        if (mWifiLock == null) {
            mWifiLock = ((WifiManager)this.ctx.getSystemService("wifi")).createWifiLock(1, "DmcUtils");
            mWifiLock.setReferenceCounted(false);
        }
        mWifiLock.acquire();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void deleteAlarm(int n) {
        DmcLog.d("DmcUtils", "deleteAlarm: " + n);
        Intent intent = new Intent(this.ctx, (Class)DmcAlarmReceiver.class);
        intent.putExtra("alarmIdExtra", n);
        intent = PendingIntent.getBroadcast((Context)this.ctx, (int)n, (Intent)intent, (int)536870912);
        if (intent != null) {
            DmcLog.d("DmcUtils", "Alarm " + n + " is deleted.");
            ((AlarmManager)this.ctx.getSystemService("alarm")).cancel((PendingIntent)intent);
            intent.cancel();
        } else {
            DmcLog.i("DmcUtils", "deleteAlarm " + n + " is not set");
        }
        if (this.timer != null && n == 0) {
            DmcLog.d("DmcUtils", "Cancel and delete timer object");
            this.timer.cancel();
            this.timer.purge();
            this.timer = null;
        }
    }

    public String getIMEI() {
        return ((TelephonyManager)this.ctx.getSystemService("phone")).getDeviceId();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getInstallWindowEnd() {
        DmcLog.i("DmcUtils", String.format("getInstallWindowEnd", new Object[0]));
        try {
            SystemUpdatePolicy systemUpdatePolicy = ((DevicePolicyManager)this.ctx.getSystemService("device_policy")).getSystemUpdatePolicy();
            if (systemUpdatePolicy != null) {
                DmcLog.i("DmcUtils", "InstallWindowEnd: " + systemUpdatePolicy.getInstallWindowEnd());
                return systemUpdatePolicy.getInstallWindowEnd();
            }
            DmcLog.i("DmcUtils", "PolicyType: None");
            return 0;
        }
        catch (NullPointerException var1_2) {
            DmcLog.e("DmcUtils", "NullPointerException");
            return 0;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getInstallWindowStart() {
        DmcLog.i("DmcUtils", String.format("getInstallWindowStart", new Object[0]));
        try {
            SystemUpdatePolicy systemUpdatePolicy = ((DevicePolicyManager)this.ctx.getSystemService("device_policy")).getSystemUpdatePolicy();
            if (systemUpdatePolicy != null) {
                DmcLog.i("DmcUtils", "InstallWindowStart: " + systemUpdatePolicy.getInstallWindowStart());
                return systemUpdatePolicy.getInstallWindowStart();
            }
            DmcLog.i("DmcUtils", "PolicyType: None");
            return 0;
        }
        catch (NullPointerException var1_2) {
            DmcLog.e("DmcUtils", "NullPointerException");
            return 0;
        }
    }

    public int getRuntimePermissionState() {
        int n = 0;
        if (this.ctx.checkSelfPermission("android.permission.READ_PHONE_STATE") == 0) {
            n = 1;
        }
        int n2 = n;
        if (this.ctx.checkSelfPermission("android.permission.RECEIVE_WAP_PUSH") == 0) {
            n2 = n | 2;
        }
        n = n2;
        if (this.ctx.checkSelfPermission("android.permission.RECEIVE_SMS") == 0) {
            n = n2 | 4;
        }
        n2 = n;
        if (this.ctx.checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            n2 = n | 8;
        }
        DmcLog.d("DmcUtils", "checkRuntimePermission :" + n2);
        return n2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getSystemUpdatePolicy() {
        DmcLog.i("DmcUtils", String.format("getSystemUpdatePolicy", new Object[0]));
        try {
            SystemUpdatePolicy systemUpdatePolicy = ((DevicePolicyManager)this.ctx.getSystemService("device_policy")).getSystemUpdatePolicy();
            if (systemUpdatePolicy != null) {
                DmcLog.i("DmcUtils", "PolicyType: " + systemUpdatePolicy.getPolicyType());
                return systemUpdatePolicy.getPolicyType();
            }
            DmcLog.i("DmcUtils", "PolicyType: None");
            return 0;
        }
        catch (NullPointerException var1_2) {
            DmcLog.e("DmcUtils", "NullPointerException");
            return 0;
        }
    }

    public boolean isCallInProgress() {
        boolean bl = false;
        if (((TelephonyManager)this.ctx.getSystemService("phone")).getCallState() != 0) {
            bl = true;
        }
        DmcLog.d("DmcUtils", "IsCallInProgress returning:" + bl);
        return bl;
    }

    public boolean isCurrentUserOwner() {
        boolean bl = false;
        boolean bl2 = false;
        boolean bl3 = false;
        boolean bl4 = false;
        ActivityManager activityManager = (ActivityManager)this.ctx.getSystemService("activity");
        boolean bl5 = bl;
        boolean bl6 = bl2;
        boolean bl7 = bl3;
        int n = Integer.parseInt(activityManager.getClass().getMethod("getCurrentUser", new Class[0]).invoke((Object)activityManager, new Object[0]).toString());
        bl5 = bl;
        bl6 = bl2;
        bl7 = bl3;
        DmcLog.d("DmcUtils", "isCurrentUserOwner:userId=" + n);
        if (n == 0) {
            bl4 = true;
        }
        bl5 = bl4;
        bl6 = bl4;
        bl7 = bl4;
        try {
            DmcLog.d("DmcUtils", "isCurrentUserOwner:isOwner=" + bl4);
            return bl4;
        }
        catch (Exception var9_6) {
            DmcLog.e("DmcUtils", "Exception occurred: " + var9_6.getMessage());
            return bl5;
        }
        catch (IllegalArgumentException var9_7) {
            DmcLog.e("DmcUtils", "IllegalArgumentException occurred: " + var9_7.getMessage());
            return bl6;
        }
        catch (IllegalAccessException var9_8) {
            DmcLog.e("DmcUtils", "IllegalAccessException occurred: " + var9_8.getMessage());
            return bl7;
        }
    }

    public boolean isNetworkAvailable() {
        boolean bl = false;
        boolean bl2 = this.isNetworkConnected(0);
        TelephonyManager telephonyManager = (TelephonyManager)this.ctx.getSystemService("phone");
        boolean bl3 = bl;
        if (bl2) {
            bl3 = bl;
            if (telephonyManager.getDataState() == 2) {
                bl3 = this.isInService(telephonyManager);
            }
        }
        DmcLog.d("DmcUtils", "isMobileNetworkAvailable available:" + bl3 + ", connected:" + bl2);
        if (bl3) {
            return bl2;
        }
        return false;
    }

    public boolean isOwnerUser() {
        UserHandle userHandle = Process.myUserHandle();
        UserManager userManager = (UserManager)this.ctx.getSystemService("user");
        if (userManager != null) {
            if (userManager.getSerialNumberForUser(userHandle) != 0) {
                DmcLog.d("DmcUtils", "not owner user");
                return false;
            }
            DmcLog.d("DmcUtils", "owner user");
            return true;
        }
        DmcLog.d("DmcUtils", "unknown user");
        return false;
    }

    public boolean isRoaming() {
        boolean bl = ((TelephonyManager)this.ctx.getSystemService("phone")).isNetworkRoaming();
        DmcLog.d("DmcUtils", "isRoaming returning:" + bl);
        return bl;
    }

    public boolean isWirelessNetworkConnected() {
        boolean bl = this.isNetworkConnected(1);
        DmcLog.d("DmcUtils", "isWirelessNetworkConnected returning:" + bl);
        return bl;
    }

    public void releaseWakeLock() {
        DmcLog.i("DmcUtils", String.format("release wakelock", new Object[0]));
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }

    public void releaseWifiLock() {
        DmcLog.i("DmcUtils", String.format("release WifiLock", new Object[0]));
        if (mWifiLock != null && mWifiLock.isHeld()) {
            mWifiLock.release();
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public int setAlarm(int n, int n2, int n3) {
        AlarmManager alarmManager = (AlarmManager)this.ctx.getSystemService("alarm");
        DmcLog.d("DmcUtils", "setAlarm inKey=" + n + " initial=" + n2 + " interval=" + n3);
        Intent intent = new Intent(this.ctx, (Class)DmcAlarmReceiver.class);
        intent.putExtra("alarmIdExtra", n);
        intent = PendingIntent.getBroadcast((Context)this.ctx, (int)n, (Intent)intent, (int)134217728);
        alarmManager.cancel((PendingIntent)intent);
        long l = (long)n2 * 1000;
        if (n3 != 0) {
            alarmManager.setRepeating(0, l, (long)n3 * 1000, (PendingIntent)intent);
        } else {
            alarmManager.setExactAndAllowWhileIdle(0, l, (PendingIntent)intent);
        }
        DmcLog.i("DmcUtils", String.format("alarm: 0x%x set, initial: %d, interval: %d, at: %s", n, n2, n3, new Date(l)));
        return 0;
    }

    public void setAlarmByInterval(int n, int n2, boolean bl) {
        Object object = (AlarmManager)this.ctx.getSystemService("alarm");
        DmcLog.d("DmcUtils", "setAlarmByInterval inKey=" + n + " inInterval=" + n2 + " isRepeat=" + bl);
        Intent intent = new Intent(this.ctx, (Class)DmcAlarmReceiver.class);
        intent.putExtra("alarmIdExtra", n);
        intent = PendingIntent.getBroadcast((Context)this.ctx, (int)n, (Intent)intent, (int)134217728);
        if (intent != null) {
            if (bl) {
                object.setRepeating(2, SystemClock.elapsedRealtime() + (long)(n2 * 1000), (long)(n2 * 1000), (PendingIntent)intent);
                return;
            }
            if (n == 0 && n2 <= 5) {
                DmcLog.d("DmcUtils", "Setting CustomTimerTask inKey=" + n + " inInterval=" + n2);
                if (this.timer != null) {
                    DmcLog.d("DmcUtils", "cancel and delete timer object");
                    this.timer.cancel();
                    this.timer.purge();
                    this.timer = null;
                }
                this.timer = new Timer();
                object = new CustomTimerTask();
                object.inKey = n;
                object.inInterval = n2;
                object.handler = new Handler(this.ctx.getMainLooper());
                this.timer.schedule((TimerTask)object, n2 * 1000);
                return;
            }
            if (n == 2) {
                DmcLog.d("DmcUtils", "calling setAlarmClock() for ALARM_TDM");
                object.setAlarmClock(new AlarmManager.AlarmClockInfo(System.currentTimeMillis() + (long)(n2 * 1000), (PendingIntent)intent), (PendingIntent)intent);
                return;
            }
            DmcLog.d("DmcUtils", "calling setExactAndAllowWhileIdle()");
            object.setExactAndAllowWhileIdle(2, SystemClock.elapsedRealtime() + (long)(n2 * 1000), (PendingIntent)intent);
            return;
        }
        DmcLog.i("DmcUtils", "Alarm " + n + " not set");
    }

    /*
     * Enabled aggressive block sorting
     */
    public void showNotification(int n, int n2, boolean bl, String string2, String string3, String string4, boolean bl2, boolean bl3, boolean bl4) {
        DmcLog.v("DmcUtils", "showNotification()");
        Notification.Builder builder = new Notification.Builder(this.ctx.getApplicationContext());
        NotificationManager notificationManager = (NotificationManager)this.ctx.getSystemService("notification");
        Intent intent = new Intent(this.ctx.getApplicationContext(), (Class)DmcService.class);
        intent.putExtra("notificationIdExtra", n);
        intent.putExtra("serviceStartReason", 3);
        intent = bl4 ? PendingIntent.getBroadcast((Context)this.ctx, (int)n, (Intent)intent, (int)268435456) : PendingIntent.getService((Context)this.ctx, (int)n, (Intent)intent, (int)268435456);
        string2 = builder.setContentTitle((CharSequence)string3).setContentText((CharSequence)string4).setWhen(System.currentTimeMillis()).setContentIntent((PendingIntent)intent).setWhen(System.currentTimeMillis()).setTicker((CharSequence)string2).setSmallIcon(n2).setStyle((Notification.Style)new Notification.BigTextStyle().bigText((CharSequence)string4).setSummaryText(null)).build();
        if (bl2) {
            string2.flags |= 16;
        }
        if (!bl3) {
            string2.flags |= 32;
        }
        if (bl) {
            notificationManager.notify(n, (Notification)string2);
            return;
        }
        notificationManager.cancel(n);
    }

    class CustomTimerTask
    extends TimerTask {
        public Handler handler;
        public int inInterval;
        public int inKey;

        CustomTimerTask() {
        }

        @Override
        public void run() {
            this.handler.post(new Runnable(){

                @Override
                public void run() {
                    DmcLog.d("DmcUtils", "CustomTimerTask expired for inKey=" + CustomTimerTask.this.inKey + " interval=" + CustomTimerTask.this.inInterval);
                    Intent intent = new Intent(DmcUtils.this.ctx, (Class)DmcService.class);
                    intent.putExtra("serviceStartReason", 5);
                    intent.putExtra("alarmIdExtra", CustomTimerTask.this.inKey);
                    DmcUtils.this.ctx.startService(intent);
                    if (DmcUtils.this.timer != null) {
                        DmcLog.d("DmcUtils", "cancel and delete timer object");
                        DmcUtils.this.timer.cancel();
                        DmcUtils.this.timer.purge();
                        DmcUtils.this.timer = null;
                    }
                }
            });
        }

    }

}

