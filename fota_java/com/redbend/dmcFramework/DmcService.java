/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Service
 *  android.content.BroadcastReceiver
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.content.res.AssetManager
 *  android.net.ConnectivityManager
 *  android.net.NetworkInfo
 *  android.net.NetworkInfo$State
 *  android.os.Bundle
 *  android.os.Handler
 *  android.os.IBinder
 *  android.os.Message
 *  android.os.Process
 */
package com.redbend.dmcFramework;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Process;
import com.redbend.dmcFramework.DmClientBaseService;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcChoiceListQuery;
import com.redbend.dmcFramework.DmcConfirmationQuery;
import com.redbend.dmcFramework.DmcInfoMessage;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcMain;
import com.redbend.dmcFramework.DmcUtils;
import com.redbend.dmcFramework.TimeUtils;
import com.redbend.vdm.comm.CommFactory;
import com.redbend.vdm.comm.VdmComm;
import com.redbend.vdm.comm.VdmCommException;
import com.redbend.vdm.comm.VdmCommFactory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Locale;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class DmcService
extends Service {
    public static final int ACTIVITY_LOADED = 13;
    public static final int ACTIVITY_PAUSED = 12;
    public static final int BUTTON_CLICKED = 2;
    public static final int CLOSE_APP = 18;
    public static final int CONNECTIVITY_CHANGE_CONNECTED = 21;
    public static final int EXIT_APP = 5;
    public static final int KILL_APP = 19;
    public static final int LIST_ITEM_SELECTED = 8;
    private static final String LOG_TAG = "DmcService";
    public static final int NOTIF_UPDATE_FAIL = 14;
    public static final int NOTIF_UPDATE_SUCCESS = 13;
    public static final int NOTIF_UPDATE_TIME = 10;
    public static final int SCREEN_CHOICELISTQUERY = 7;
    public static final int SCREEN_CONFQUERY = 6;
    public static final int SCREEN_INFOMSG = 3;
    public static final int SERVICE_STARTED_BY_ALARM = 11;
    public static final int SERVICE_STARTED_FROM_BOOT = 10;
    public static final int SERVICE_STARTED_FROM_MENU = 1;
    public static final int SERVICE_STARTED_FROM_NOTIFICATION_ICON = 9;
    public static final int SERVICE_STARTED_FROM_SYSTEMTIMECHANGE = 20;
    public static final int SERVICE_STARTED_FROM_SYSTEM_UPDATE_POLICY_CHANGE = 22;
    public static final int SERVICE_STARTED_FROM_WAPPUSH = 4;
    public static final int STARTED_BY_ALARM = 5;
    public static final int STARTED_FROM_BOOT = 4;
    public static final int STARTED_FROM_MENU = 1;
    public static final int STARTED_FROM_NOTIFICATION = 3;
    public static final int STARTED_FROM_SYSTEMTIMECHANGE = 6;
    public static final int STARTED_FROM_SYSTEM_UPDATE_POLICY_CHANGE = 7;
    public static final int STARTED_FROM_WAPPUSH = 2;
    public static final int START_APPLICATION_SERVICE_ACTIONS = 5000;
    public static final int UPDATE_MENU_SELECTED = 17;
    public static final int WIFI_OFF = 15;
    public static final int WIFI_ON = 14;
    public static final String alarmIdExtra = "alarmIdExtra";
    public static final String clientServiceClassExtra = "clientServiceClassExtra";
    public static final String clientServiceExtra = "clientService";
    public static final String nextStateExtra = "nextStateExtra";
    public static final String notificationIdExtra = "notificationIdExtra";
    public static final String notificationObjExtra = "notificationObjExtra";
    public static final String startServiceNotif = "serviceStartNotif";
    public static final String startServiceReasonExtra = "serviceStartReason";
    public static final String uiAlertBundleExtra = "uiAlertBundleExtra";
    public static final String uiAlertTextExtra = "uiAlertTextExtra";
    public static final String wapPushMessageExtra = "wapPushMessageExtra";
    private DmcApplication a;
    private DmClientBaseService clientService;
    private Bundle cls;
    private VdmComm comm = null;
    private BroadcastReceiver mDmcConnectivityReceiver = null;
    private Handler mMainHandler;
    public DmcUtils u = null;

    /*
     * Enabled aggressive block sorting
     */
    private void dispatch(Message object) {
        if (object.what >= 5000) {
            this.clientService.dispatch((Message)object);
            return;
        }
        switch (object.what) {
            case 1: {
                object = (Integer)object.obj;
                DmcLog.d("DmcService", "SERVICE_STARTED_FROM_MENU " + object);
                if (object.intValue() != -1 && object.intValue() != 0) {
                    this.notificationIconClicked(object.intValue());
                    return;
                }
                this.applicationIconClicked();
                return;
            }
            case 2: {
                this.buttonClicked((String)object.obj);
                return;
            }
            case 8: {
                this.listItemSelected((Integer)object.obj);
                return;
            }
            case 7: {
                Intent intent = new Intent(this.getApplicationContext(), (Class)DmcChoiceListQuery.class);
                intent.addFlags(67108864);
                intent.putExtra("uiAlertBundleExtra", (Bundle)object.obj);
                intent.putExtra("nextStateExtra", ((Bundle)object.obj).getInt("nextState"));
                this.startActivity(intent);
                return;
            }
            case 6: {
                Intent intent = new Intent(this.getApplicationContext(), (Class)DmcConfirmationQuery.class);
                intent.addFlags(67108864);
                intent.putExtra("uiAlertTextExtra", ((Bundle)object.obj).getString("displayText"));
                intent.putExtra("nextStateExtra", ((Bundle)object.obj).getInt("nextState"));
                this.startActivity(intent);
                return;
            }
            case 3: {
                Intent intent = new Intent(this.getApplicationContext(), (Class)DmcInfoMessage.class);
                intent.addFlags(67108864);
                intent.putExtra("uiAlertTextExtra", ((Bundle)object.obj).getString("displayText"));
                intent.putExtra("nextStateExtra", ((Bundle)object.obj).getInt("nextState"));
                this.startActivity(intent);
                return;
            }
            case 4: {
                object = (byte[])object.obj;
                this.wapPushReceived((byte[])object, object.length);
                return;
            }
            case 9: {
                object = (Integer)object.obj;
                if (object.intValue() != 10 && object.intValue() != 13 && object.intValue() != 14) {
                    if (!this.a.isDmcMainStarted) {
                        SharedPreferences.Editor editor = this.getSharedPreferences("notif", 0).edit();
                        editor.putInt("notif_type", object.intValue());
                        editor.commit();
                        object = new Intent(this.getApplicationContext(), (Class)DmcMain.class);
                        object.addFlags(67108864);
                        DmcLog.d("DmcService", "start DmcMain");
                        this.startActivity((Intent)object);
                        return;
                    }
                    DmcLog.d("DmcService", "notification click");
                    this.notificationIconClicked(object.intValue());
                    return;
                }
                DmcLog.d("DmcService", "notification click");
                this.notificationIconClicked(object.intValue());
                return;
            }
            case 11: {
                object = (Integer)object.obj;
                if (object.intValue() != -1) {
                    this.AlarmExpired(object.intValue());
                    return;
                }
            }
            default: {
                return;
            }
            case 20: {
                this.systemTimeChanged();
                return;
            }
            case 5: {
                DmcLog.d("DmcService", "EXIT_APP");
                this.stopSelf();
                if (this.a.lastResumedActivity != null) {
                    DmcLog.d("DmcService", "finish lastResumedActivity");
                    this.a.lastResumedActivity.moveTaskToBack(true);
                    this.a.lastResumedActivity.finish();
                    return;
                }
                DmcLog.e("DmcService", "exit_app() called with null lastResumedActivity");
                return;
            }
            case 18: {
                DmcLog.d("DmcService", "CLOSE_APP");
                if (this.a.lastResumedActivity != null) {
                    DmcLog.d("DmcService", "movaTaskToBack lastResumedActivity");
                    this.a.lastResumedActivity.moveTaskToBack(true);
                    return;
                }
                DmcLog.e("DmcService", "close_app() called with null lastResumedActivity");
                return;
            }
            case 19: {
                DmcLog.d("DmcService", "KILL_APP");
                this.u.releaseWifiLock();
                this.u.releaseWakeLock();
                this.stopSelf();
                if (this.a.currentActivity != null) {
                    DmcLog.d("DmcService", "moveTaskToback currentActivity");
                    this.a.currentActivity.moveTaskToBack(true);
                    this.a.currentActivity = null;
                }
                Process.killProcess((int)Process.myPid());
                return;
            }
            case 10: {
                this.bootCompleted();
                return;
            }
            case 12: {
                this.activityPaused((Integer)object.obj);
                return;
            }
            case 13: {
                this.activityLoaded((Integer)object.obj);
                return;
            }
            case 14: {
                this.wifiOn();
                return;
            }
            case 15: {
                this.wifiOff();
                return;
            }
            case 21: {
                this.connectivityChangeConnected((Integer)object.obj);
                return;
            }
            case 17: {
                this.updateMenuSelected((Integer)object.obj);
                return;
            }
            case 22: 
        }
        this.systemUpdatePolicyChanged();
    }

    public native void AlarmExpired(int var1);

    public String GetPkgName() {
        return this.u.GetPackageName();
    }

    public void acquireWakeLock(int n) {
        DmcLog.i("DmcService", String.format("acquire wakelock %d mills", n));
        this.u.acquireWakeLock(n);
    }

    public void acquireWifiLock() {
        DmcLog.i("DmcService", String.format("acquire WifiLock", new Object[0]));
        this.u.acquireWifiLock();
    }

    public native void activityLoaded(int var1);

    public native void activityPaused(int var1);

    public void appClose() {
        this.a.sendMessage(18, null);
    }

    public void appExit() {
        this.a.sendMessage(5, null);
    }

    public void appKill() {
        this.a.sendMessage(19, null);
    }

    public native void applicationIconClicked();

    public native void bootCompleted();

    public native void buttonClicked(String var1);

    public native void connectivityChangeConnected(int var1);

    public void deleteAlarm(int n) {
        this.u.deleteAlarm(n);
    }

    public String getDevId() {
        return this.u.getIMEI();
    }

    public long getInstallAlarmDelta() {
        DmcLog.v("DmcService", "getInstallAlarmDelta()");
        return TimeUtils.getInstallTimeDelta(this.getApplicationContext());
    }

    public int getInstallWindowEnd() {
        DmcLog.i("DmcService", String.format("getInstallWindowEnd", new Object[0]));
        return this.u.getInstallWindowEnd();
    }

    public int getInstallWindowStart() {
        DmcLog.i("DmcService", String.format("getInstallWindowStart", new Object[0]));
        return this.u.getInstallWindowStart();
    }

    public String getSystemLang() {
        return Locale.getDefault().getLanguage();
    }

    public int getSystemUpdatePolicy() {
        DmcLog.i("DmcService", String.format("getSystemUpdatePolicy", new Object[0]));
        return this.u.getSystemUpdatePolicy();
    }

    public boolean isCallInProgress() {
        return this.u.isCallInProgress();
    }

    public boolean isNetworkAvailable() {
        return this.u.isNetworkAvailable();
    }

    public boolean isRoaming() {
        if (new File("/data/local/tmp/redbend/is_roaming").exists()) {
            DmcLog.d("DmcService", "is_roaming has been found.");
            return true;
        }
        return this.u.isRoaming();
    }

    public boolean isWirelessNetworkConnected() {
        return this.u.isWirelessNetworkConnected();
    }

    public native void listItemSelected(int var1);

    public native void notificationIconClicked(int var1);

    public IBinder onBind(Intent intent) {
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void onCreate() {
        DmcLog.d("DmcService", "onCreate()");
        this.u = new DmcUtils(this.getApplicationContext());
        if (!this.u.isOwnerUser()) {
            DmcLog.d("DmcService", "The user is not owner. DMService is not started.");
            Process.killProcess((int)Process.myPid());
        }
        System.loadLibrary("dmc");
        try {
            this.comm = new VdmComm(new VdmCommFactory());
            this.comm.setConnectionTimeout(40);
        }
        catch (VdmCommException var1_1) {
            DmcLog.e("DmcService", "Could not initialize VdmComm");
            var1_1.printStackTrace();
        }
        if (new File(this.getFilesDir() + "/tree.xml").exists()) return;
        DmcLog.e("DmcService", "Setting default tree.xml");
        Object object = this.getAssets();
        try {
            object = object.open("tree.xml");
            FileOutputStream fileOutputStream = this.openFileOutput("tree.xml", 0);
            byte[] arrby = new byte[object.available()];
            object.read(arrby);
            object.close();
            fileOutputStream.write(arrby);
            fileOutputStream.close();
            return;
        }
        catch (Exception var1_3) {
            DmcLog.e("DmcService", "Could not set default tree.xml");
            return;
        }
    }

    public void onDestroy() {
        DmcLog.v("DmcService", "onDestroy()");
        super.onDestroy();
        this.unregisterConnectivityReceiver();
    }

    /*
     * Exception decompiling
     */
    public int onStartCommand(Intent var1_1, int var2_3, int var3_4) {
        // This method has failed to decompile.  When submitting a bug report, please provide this stack trace, and (if you hold appropriate legal rights) the relevant class file.
        // org.benf.cfr.reader.util.CannotPerformDecode: reachable test BLOCK was exited and re-entered.
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.Misc.getFarthestReachableInRange(Misc.java:143)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.examineSwitchContiguity(SwitchReplacer.java:385)
        // org.benf.cfr.reader.bytecode.analysis.opgraph.op3rewriters.SwitchReplacer.replaceRawSwitches(SwitchReplacer.java:65)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisInner(CodeAnalyser.java:425)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysisOrWrapFail(CodeAnalyser.java:220)
        // org.benf.cfr.reader.bytecode.CodeAnalyser.getAnalysis(CodeAnalyser.java:165)
        // org.benf.cfr.reader.entities.attributes.AttributeCode.analyse(AttributeCode.java:91)
        // org.benf.cfr.reader.entities.Method.analyse(Method.java:354)
        // org.benf.cfr.reader.entities.ClassFile.analyseMid(ClassFile.java:751)
        // org.benf.cfr.reader.entities.ClassFile.analyseTop(ClassFile.java:683)
        // org.benf.cfr.reader.Main.doJar(Main.java:129)
        // org.benf.cfr.reader.Main.main(Main.java:181)
        throw new IllegalStateException("Decompilation failed");
    }

    public void registerConnectivityReceiver() {
        DmcLog.v("DmcService", "registerConnectivityReceiver()");
        this.mDmcConnectivityReceiver = new DmcConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        this.registerReceiver(this.mDmcConnectivityReceiver, intentFilter, null, null);
    }

    public void releaseWakeLock() {
        DmcLog.i("DmcService", String.format("release wakelock", new Object[0]));
        this.u.releaseWakeLock();
    }

    public void releaseWifiLock() {
        DmcLog.i("DmcService", String.format("release WifiLock", new Object[0]));
        this.u.releaseWifiLock();
    }

    public void removeInstallScheduleFromPreferences() {
        DmcLog.v("DmcService", "removeInstallScheduleFromPreferences()");
        TimeUtils.rmInstallScheduleTime(this.getApplicationContext());
    }

    public void resetScheduleTime() {
        DmcLog.v("DmcService", "resetScheduleTime()");
        TimeUtils.resetScheduleTime(this.getApplicationContext());
    }

    public void screenChoiceListQuery(String string2, int n, String[] arrstring, int n2, boolean bl, int n3) {
        this.cls = new Bundle(6);
        this.cls.putString("displayText", string2);
        this.cls.putInt("itemsCount", n);
        this.cls.putStringArray("items", arrstring);
        this.cls.putInt("DefaultSelection", n2);
        this.cls.putBoolean("isMultiSelect", bl);
        this.cls.putInt("nextState", n3);
        this.a.sendMessage(7, (Object)this.cls);
    }

    public void screenConfirmationQuery(String string2, int n) {
        this.cls = new Bundle(2);
        this.cls.putString("displayText", string2);
        this.cls.putInt("nextState", n);
        this.a.sendMessage(6, (Object)this.cls);
    }

    public void screenInfoMessage(String string2, int n) {
        this.cls = new Bundle(2);
        this.cls.putString("displayText", string2);
        this.cls.putInt("nextState", n);
        this.a.sendMessage(3, (Object)this.cls);
    }

    public void setAlarm(int n, int n2, int n3) {
        this.u.setAlarm(n, n2, n3);
    }

    public void setAlarmByInterval(int n, int n2, boolean bl) {
        this.u.setAlarmByInterval(n, n2, bl);
    }

    public native void setService(VdmComm var1);

    public void startActivity(Intent intent) {
        DmcLog.v("DmcService", "startActivity()");
        if (this.u.isCurrentUserOwner()) {
            intent.addFlags(intent.getFlags() | 268435456);
            super.startActivity(intent);
            return;
        }
        DmcLog.d("DmcService", "The user is not owner. Activity is not started.");
    }

    public native void systemTimeChanged();

    public native void systemUpdatePolicyChanged();

    public void unregisterConnectivityReceiver() {
        DmcLog.v("DmcService", "unregisterConnectivityReceiver()");
        if (this.mDmcConnectivityReceiver != null) {
            this.unregisterReceiver(this.mDmcConnectivityReceiver);
            this.mDmcConnectivityReceiver = null;
        }
    }

    public native void updateMenuSelected(int var1);

    public native void wapPushReceived(byte[] var1, int var2);

    public native void wifiOff();

    public native void wifiOn();

}

