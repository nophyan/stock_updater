/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.content.Context
 *  android.content.Intent
 *  android.os.Bundle
 *  android.os.Message
 */
package com.redbend.dmClient;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import com.redbend.dmClient.AutoSetting;
import com.redbend.dmClient.AutoSettingInformation;
import com.redbend.dmClient.BatteryLow;
import com.redbend.dmClient.ConfirmConfigChange;
import com.redbend.dmClient.ConfirmUpdate;
import com.redbend.dmClient.Connecting;
import com.redbend.dmClient.DeviceUtils;
import com.redbend.dmClient.DiskInsufficient;
import com.redbend.dmClient.HttpError;
import com.redbend.dmClient.InRoaming;
import com.redbend.dmClient.InitSchedSetting;
import com.redbend.dmClient.InvalidPackage;
import com.redbend.dmClient.MaintenanceMode;
import com.redbend.dmClient.NetworkNotAvailable;
import com.redbend.dmClient.NoStorageSpace;
import com.redbend.dmClient.NotifySchedule;
import com.redbend.dmClient.PkgNoValid;
import com.redbend.dmClient.ProgressBarScreen;
import com.redbend.dmClient.RequestRuntimePermission;
import com.redbend.dmClient.RestDownload;
import com.redbend.dmClient.SchedSetting;
import com.redbend.dmClient.SetActualInstallTime;
import com.redbend.dmClient.ShowUnreadInfoMessage;
import com.redbend.dmClient.StartUpdate;
import com.redbend.dmClient.UpdateCancelled;
import com.redbend.dmClient.UpdateComplete;
import com.redbend.dmClient.UpdateError;
import com.redbend.dmClient.UpdateFail;
import com.redbend.dmClient.UpdateFailed;
import com.redbend.dmClient.UpdateInProgress;
import com.redbend.dmClient.UpdateMenu;
import com.redbend.dmClient.UpdateNow;
import com.redbend.dmClient.UpdateNowAutoInstall;
import com.redbend.dmClient.UpdateNowAutomatic;
import com.redbend.dmClient.UpdateNowOnAlarm;
import com.redbend.dmClient.UpdateNowPostpone;
import com.redbend.dmClient.UpdateStatusUnknown;
import com.redbend.dmClient.UpdateTime;
import com.redbend.dmClient.UsimReadError;
import com.redbend.dmClient.VerifyingPackageProgress;
import com.redbend.dmClient.WaitDialog;
import com.redbend.dmClient.WirelessDownloadInterruption;
import com.redbend.dmClient.WirelessNotConnected;
import com.redbend.dmClient.ipl.DmcIplImpl;
import com.redbend.dmClient.ipl.IplFactory;
import com.redbend.dmcFramework.DmClientBaseService;
import com.redbend.dmcFramework.DmcActivity;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;
import com.redbend.dmcFramework.DmcUtils;
import java.io.Serializable;

public class ClientService
extends DmClientBaseService {
    public static final int DMS_CONFIGURED = 5015;
    private static final String LOG_TAG = "ClientService";
    public static final int NOTIF_COMPLETE_FAIL_ONPOSTDL = 11;
    public static final int NOTIF_COMPLETE_UPDATE_CANCEL = 12;
    public static final int NOTIF_DOWNLOAD_AVAILABLE = 2;
    public static final int NOTIF_DOWNLOAD_COMPLETE = 1;
    public static final int NOTIF_DOWNLOAD_UNSUCCESS = 3;
    public static final int NOTIF_FOTA_COMPLETION = 5;
    public static final int NOTIF_FOTA_INITIATION = 4;
    public static final int NOTIF_INSUFFICIENT_STORAGE = 8;
    public static final int NOTIF_LOSS_OF_WIFI_CONNECTION = 6;
    public static final int NOTIF_RUNTIME_PERMISSION = 15;
    public static final int NOTIF_UPDATE_FAIL = 14;
    public static final int NOTIF_UPDATE_SUCCESS = 13;
    public static final int NOTIF_UPDATE_TIME = 10;
    public static final int NOTIF_WIFI_NON_CONNECTED_DISCONNECTED = 7;
    public static final int NOTIF_WIFI_NON_CONNECTED_DL = 9;
    public static final int PROXY_CONFIGURED = 5016;
    public static final int SCREEN_AUTO_SETTING = 5030;
    public static final int SCREEN_AUTO_SETTING_INFORMATION = 5051;
    public static final int SCREEN_BATTERY_LOW = 5004;
    public static final int SCREEN_CONFIG_CHANGE = 5032;
    public static final int SCREEN_CONFIRM_UPDATE = 5002;
    public static final int SCREEN_CONNECTING = 5027;
    public static final int SCREEN_DP_VALIDATION_START = 5040;
    public static final int SCREEN_DP_VALIDATION_STOP = 5041;
    public static final int SCREEN_HAND_OFF = 5048;
    public static final int SCREEN_HTTP_ERROR = 5050;
    public static final int SCREEN_INIT_SCHED_SETTING = 5045;
    public static final int SCREEN_IN_PROGRESS = 5019;
    public static final int SCREEN_IN_ROAMING = 5026;
    public static final int SCREEN_MAINTENANCE = 5028;
    public static final int SCREEN_NETWORK_NOT_AVAILABLE = 5014;
    public static final int SCREEN_NOTIFY_SCHEDULE = 5035;
    public static final int SCREEN_NO_STORAGE_SPACE = 5023;
    public static final int SCREEN_PKG_NO_VALID = 5038;
    public static final int SCREEN_PROGRESS_BAR = 5006;
    public static final int SCREEN_REQUEST_RUNTIME_PERMISSION = 5049;
    public static final int SCREEN_REST_DOWNLOAD = 5021;
    public static final int SCREEN_SCHED_SETTING = 5031;
    public static final int SCREEN_SET_ACTUAL_INSTALL = 5034;
    public static final int SCREEN_UPDATE_ALARM = 5036;
    public static final int SCREEN_UPDATE_CANCELLED = 5008;
    public static final int SCREEN_UPDATE_COMPLETE = 5024;
    public static final int SCREEN_UPDATE_DISK_INSUFFICIENT = 5047;
    public static final int SCREEN_UPDATE_ERROR = 5017;
    public static final int SCREEN_UPDATE_FAIL = 5044;
    public static final int SCREEN_UPDATE_FAILED = 5013;
    public static final int SCREEN_UPDATE_INVALID_PACKAGE = 5046;
    public static final int SCREEN_UPDATE_MENU = 5029;
    public static final int SCREEN_UPDATE_NOW = 5011;
    public static final int SCREEN_UPDATE_NOW_AUTO = 5033;
    public static final int SCREEN_UPDATE_NOW_AUTOMATIC = 5052;
    public static final int SCREEN_UPDATE_NOW_ON_ALARM = 5012;
    public static final int SCREEN_UPDATE_NOW_POSTPONE = 5053;
    public static final int SCREEN_UPDATE_RECORD = 5025;
    public static final int SCREEN_UPDATE_STATUS_UNKNOWN = 5018;
    public static final int SCREEN_UPDATE_TIME = 5043;
    public static final int SCREEN_USIM_READ_ERROR = 5039;
    public static final int SCREEN_VERIFYING_PACKAGE_PROGRESS = 5042;
    public static final int SCREEN_WIRELESS_DOWNLOAD_INTERRUPTION = 5022;
    public static final int SCREEN_WIRELESS_NOT_CONNECTED = 5020;
    public static final int STATE_HAND_OFF_END = 59;
    public static final int STATE_HAND_OFF_START = 58;
    public static final int UPDATE_PROGRESS_BAR = 5007;
    public static final String infoMessageTextExtra = "infoMessageText";
    private Context mContext;
    private DmcIplImpl mIpl = null;
    private DmcUtils mUtils = null;

    public ClientService(DmcService dmcService) {
        super(dmcService);
        DmcLog.v("ClientService", "ClientService(DmcService)");
        this.setClientService();
        this.mContext = dmcService.getApplicationContext();
        this.mIpl = IplFactory.create(this.mContext);
        this.mUtils = new DmcUtils(this.mContext);
    }

    public static native String getDMServerAddress();

    public static native String getOriginalDmsAddress();

    public static native String getOriginalProxy();

    public static native String getProxyAddress();

    private void launchMaintenanceMode(Object object) {
        String string2 = ClientService.getDMServerAddress();
        String string3 = ClientService.getProxyAddress();
        object = (Integer)object;
        Intent intent = new Intent(this.dmcApplication.getApplicationContext(), (Class)MaintenanceMode.class);
        intent.putExtra("dmServerUrl", string2);
        intent.putExtra("proxyServerUrl", string3);
        intent.putExtra("nextStateExtra", object.intValue());
        this.dmcService.startActivity(intent);
    }

    private void startErrorScreen(Class<?> intent, Object object) {
        DmcLog.d("ClientService", "startErrorScreen()");
        object = (Integer)object;
        intent = new Intent(this.dmcService.getApplicationContext(), intent);
        intent.addFlags(67108864);
        intent.addFlags(2097152);
        intent.putExtra("nextStateExtra", object.intValue());
        this.dmcService.startActivity(intent);
    }

    private void startSimpleScreen(Class<?> intent, Object object) {
        DmcLog.v("ClientService", "startSimpleScreen()");
        object = (Integer)object;
        intent = new Intent(this.dmcService.getApplicationContext(), intent);
        intent.putExtra("nextStateExtra", object.intValue());
        this.dmcService.startActivity(intent);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void dispatch(Message object) {
        if (object.what < 5000) {
            DmcLog.e("ClientService", "Error - calling application dispatcher with generic dispatcher action " + object.what);
            return;
        }
        switch (object.what) {
            case 5002: {
                this.startSimpleScreen(ConfirmUpdate.class, object.obj);
                return;
            }
            case 5004: {
                this.startErrorScreen(BatteryLow.class, object.obj);
                return;
            }
            case 5006: {
                this.startSimpleScreen(ProgressBarScreen.class, object.obj);
                return;
            }
            case 5007: {
                object = (Integer)object.obj;
                try {
                    ((ProgressBarScreen)((DmcApplication)this.dmcService.getApplication()).currentActivity).updateProgress(object.intValue());
                    return;
                }
                catch (ClassCastException var1_2) {
                    DmcLog.w("ClientService", "Screen not loaded yet");
                    return;
                }
            }
            case 5008: {
                this.startErrorScreen(UpdateCancelled.class, object.obj);
                return;
            }
            case 5011: {
                this.startSimpleScreen(UpdateNow.class, object.obj);
                return;
            }
            case 5012: {
                this.startSimpleScreen(UpdateNowOnAlarm.class, object.obj);
                return;
            }
            case 5013: {
                this.startSimpleScreen(UpdateFailed.class, object.obj);
                return;
            }
            case 5046: {
                this.startSimpleScreen(InvalidPackage.class, object.obj);
                return;
            }
            case 5047: {
                this.startSimpleScreen(DiskInsufficient.class, object.obj);
                return;
            }
            case 5024: {
                this.startSimpleScreen(UpdateComplete.class, object.obj);
                return;
            }
            case 5044: {
                this.startSimpleScreen(UpdateFail.class, object.obj);
                return;
            }
            case 5014: {
                this.startSimpleScreen(NetworkNotAvailable.class, object.obj);
                return;
            }
            case 5015: {
                this.setDMServerAddress((String)object.obj);
                return;
            }
            case 5016: {
                this.setProxyAddress((String)object.obj);
                return;
            }
            case 5017: {
                this.startSimpleScreen(UpdateError.class, object.obj);
                return;
            }
            case 5018: {
                this.startSimpleScreen(UpdateStatusUnknown.class, object.obj);
                return;
            }
            case 5019: {
                this.startSimpleScreen(UpdateInProgress.class, object.obj);
                return;
            }
            case 5020: {
                this.startSimpleScreen(WirelessNotConnected.class, object.obj);
                return;
            }
            case 5021: {
                this.startSimpleScreen(RestDownload.class, object.obj);
                return;
            }
            case 5022: {
                this.startSimpleScreen(WirelessDownloadInterruption.class, object.obj);
                return;
            }
            case 5023: {
                this.startSimpleScreen(NoStorageSpace.class, object.obj);
                return;
            }
            case 5025: {
                Intent intent = new Intent(this.dmcService.getApplicationContext(), (Class)ShowUnreadInfoMessage.class);
                intent.putExtra("notificationObjExtra", (Bundle)object.obj);
                intent.putExtra("nextStateExtra", ((Bundle)object.obj).getInt("nextState"));
                this.dmcService.startActivity(intent);
                return;
            }
            case 5026: {
                this.startSimpleScreen(InRoaming.class, object.obj);
                return;
            }
            case 5027: {
                this.startSimpleScreen(Connecting.class, object.obj);
                return;
            }
            case 5028: {
                this.launchMaintenanceMode(object.obj);
                return;
            }
            case 5029: {
                this.startSimpleScreen(UpdateMenu.class, object.obj);
                return;
            }
            case 5030: {
                this.startSimpleScreen(AutoSetting.class, object.obj);
                return;
            }
            case 5031: {
                this.startSimpleScreen(SchedSetting.class, object.obj);
                return;
            }
            case 5045: {
                this.startSimpleScreen(InitSchedSetting.class, object.obj);
                return;
            }
            case 5032: {
                this.startSimpleScreen(ConfirmConfigChange.class, object.obj);
                return;
            }
            case 5033: {
                this.startSimpleScreen(UpdateNowAutoInstall.class, object.obj);
                return;
            }
            case 5034: {
                this.startSimpleScreen(SetActualInstallTime.class, object.obj);
                return;
            }
            case 5035: {
                this.startSimpleScreen(NotifySchedule.class, object.obj);
                return;
            }
            case 5036: {
                this.startSimpleScreen(StartUpdate.class, object.obj);
                return;
            }
            case 5038: {
                this.startSimpleScreen(PkgNoValid.class, object.obj);
                return;
            }
            case 5039: {
                this.startSimpleScreen(UsimReadError.class, object.obj);
                return;
            }
            case 5042: {
                object = new Intent(this.dmcService.getApplicationContext(), (Class)VerifyingPackageProgress.class);
                this.dmcService.startActivity((Intent)object);
                return;
            }
            case 5043: {
                this.startSimpleScreen(UpdateTime.class, object.obj);
                return;
            }
            case 5048: {
                if ((Integer)object.obj == 58) {
                    WaitDialog.show(this.mContext, this.mContext.getString(2131099650));
                    return;
                }
                if ((Integer)object.obj == 59) {
                    WaitDialog.dismiss();
                    return;
                }
            }
            default: {
                return;
            }
            case 5049: {
                this.startSimpleScreen(RequestRuntimePermission.class, object.obj);
                return;
            }
            case 5050: {
                this.startSimpleScreen(HttpError.class, object.obj);
                return;
            }
            case 5051: {
                this.startSimpleScreen(AutoSettingInformation.class, object.obj);
                return;
            }
            case 5052: {
                this.startSimpleScreen(UpdateNowAutomatic.class, object.obj);
                return;
            }
            case 5053: 
        }
        this.startSimpleScreen(UpdateNowPostpone.class, object.obj);
    }

    public String getDLProxyAddress() {
        return this.mIpl.getDLProxyAddress();
    }

    public String getDMProxyAddress() {
        return this.mIpl.getDMProxyAddress();
    }

    public String getDeviceId() {
        return this.mIpl.getDeviceId();
    }

    public String getFirmwareVersion() {
        return this.mIpl.getFirmwareVersion();
    }

    public String getIMSI() {
        return this.mIpl.getIMSI();
    }

    public String getMSISDN() {
        return this.mIpl.getMSISDN();
    }

    public String getManName() {
        return this.mIpl.getManName();
    }

    public String getModelName() {
        return this.mIpl.getModelName();
    }

    public long getPackageStorageSize(String string2) {
        return this.mIpl.getPackageStorageSize(string2);
    }

    public String getUpdatePackagePathPreDownload(long l) {
        return this.mIpl.getUpdatePackagePathPreDownload(l);
    }

    public int getUpdateResult() {
        return this.mIpl.getUpdateResult();
    }

    public String getUserAgentName() {
        return this.mIpl.getUserAgentName();
    }

    public boolean isAutoInstall() {
        return DeviceUtils.isAutoInstall(this.mContext);
    }

    public boolean isBatterySufficient() {
        return this.mIpl.isBatterySufficient();
    }

    public boolean isExternalStorageUsed() {
        DmcLog.i("ClientService", "isExternalStorageUsed");
        return this.mIpl.isExternalStorageUsed();
    }

    public boolean isPostDownloadRequired(String string2) {
        return this.mIpl.isPostDownloadRequired(string2);
    }

    public boolean isProxyAddressNeeded() {
        return this.mIpl.isProxyAddressNeeded();
    }

    public boolean isRestrainAutoInstall() {
        return DeviceUtils.isRestrainAutoInstall(this.mContext);
    }

    public boolean isRuntimePermissionGranted() {
        DmcLog.v("ClientService", "isRuntimePermissionGranted()");
        int n = this.mUtils.getRuntimePermissionState();
        boolean bl = false;
        int n2 = 7;
        if (this.mIpl.isExternalStorageUsed()) {
            n2 = 15;
        }
        if ((n & n2) == n2) {
            bl = true;
        }
        return bl;
    }

    public native boolean isWiFiOnly();

    public int onPostDownload() {
        return this.mIpl.onPostDownload();
    }

    public int postUpdate() {
        return this.mIpl.postUpdate();
    }

    public void screenAutoSetting(int n) {
        this.dmcApplication.sendMessage(5030, n);
    }

    public void screenAutoSettingInformation(int n) {
        this.dmcApplication.sendMessage(5051, n);
    }

    public void screenBatteryLow(int n) {
        this.dmcApplication.sendMessage(5004, n);
    }

    public void screenConfirmConfigChange(int n) {
        this.dmcApplication.sendMessage(5032, n);
    }

    public void screenConfirmUpdate(int n) {
        this.dmcApplication.sendMessage(5002, n);
    }

    public void screenConnecting(int n) {
        this.dmcApplication.sendMessage(5027, n);
    }

    public void screenDPValidationStart(int n) {
        this.dmcApplication.sendMessage(5040, n);
    }

    public void screenDPValidationStop(int n) {
        this.dmcApplication.sendMessage(5041, n);
    }

    public void screenHandOff(int n) {
        this.dmcApplication.sendMessage(5048, n);
    }

    public void screenHttpError(int n) {
        this.dmcApplication.sendMessage(5050, n);
    }

    public void screenInProgress(int n) {
        this.dmcApplication.sendMessage(5019, n);
    }

    public void screenInRoaming(int n) {
        this.dmcApplication.sendMessage(5026, n);
    }

    public void screenInitSchedSetting(int n) {
        this.dmcApplication.sendMessage(5045, n);
    }

    public void screenMaintenanceMode(int n) {
        this.dmcApplication.sendMessage(5028, n);
    }

    public void screenNetworkNotAvailable(int n) {
        this.dmcApplication.sendMessage(5014, n);
    }

    public void screenNoStorageSpace(int n) {
        this.dmcApplication.sendMessage(5023, n);
    }

    public void screenNotifySchedule(int n) {
        this.dmcApplication.sendMessage(5035, n);
    }

    public void screenPkgNoValid(int n) {
        this.dmcApplication.sendMessage(5038, n);
    }

    public void screenProgressBar(int n) {
        this.dmcApplication.sendMessage(5006, n);
    }

    public void screenRequestRuntimePermission(int n) {
        this.dmcApplication.sendMessage(5049, n);
    }

    public void screenRestDownload(int n) {
        this.dmcApplication.sendMessage(5021, n);
    }

    public void screenSchedSetting(int n) {
        this.dmcApplication.sendMessage(5031, n);
    }

    public void screenSetInstallAlarm(int n) {
        this.dmcApplication.sendMessage(5034, n);
    }

    public void screenUpdateAlarm(int n) {
        this.dmcApplication.sendMessage(5036, n);
    }

    public void screenUpdateCancelled(int n) {
        this.dmcApplication.sendMessage(5008, n);
    }

    public void screenUpdateComplete(int n) {
        this.dmcApplication.sendMessage(5024, n);
    }

    public void screenUpdateDiskInsufficient(int n) {
        this.dmcApplication.sendMessage(5047, n);
    }

    public void screenUpdateError(int n) {
        this.dmcApplication.sendMessage(5017, n);
    }

    public void screenUpdateFail(int n) {
        this.dmcApplication.sendMessage(5044, n);
    }

    public void screenUpdateFailed(int n) {
        this.dmcApplication.sendMessage(5013, n);
    }

    public void screenUpdateInvalidPackage(int n) {
        this.dmcApplication.sendMessage(5046, n);
    }

    public void screenUpdateMenu(int n) {
        this.dmcApplication.sendMessage(5029, n);
    }

    public void screenUpdateNow(int n) {
        this.dmcApplication.sendMessage(5011, n);
    }

    public void screenUpdateNowAuto(int n) {
        this.dmcApplication.sendMessage(5033, n);
    }

    public void screenUpdateNowAutomatic(int n) {
        this.dmcApplication.sendMessage(5052, n);
    }

    public void screenUpdateNowOnAlarm(int n) {
        this.dmcApplication.sendMessage(5012, n);
    }

    public void screenUpdateNowPostpone(int n) {
        this.dmcApplication.sendMessage(5053, n);
    }

    public void screenUpdateRecord(String string2, int n) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("msg", (Serializable)((Object)string2));
        bundle.putInt("nextState", n);
        this.dmcApplication.sendMessage(5025, (Object)bundle);
    }

    public void screenUpdateStatusUnknown(int n) {
        this.dmcApplication.sendMessage(5018, n);
    }

    public void screenUpdateTime(int n) {
        this.dmcApplication.sendMessage(5043, n);
    }

    public void screenUsimReadError(int n) {
        this.dmcApplication.sendMessage(5039, n);
    }

    public void screenVerifyingPackageProgress() {
        this.dmcApplication.sendMessage(5042, null);
    }

    public void screenWirelessDownloadInterruption(int n) {
        this.dmcApplication.sendMessage(5022, n);
    }

    public void screenWirelessNotConnected(int n) {
        this.dmcApplication.sendMessage(5020, n);
    }

    public void setAutoInstall(int n) {
        DeviceUtils.setAutoInstall(this.mContext, n);
    }

    public native void setClientService();

    public native boolean setDMServerAddress(String var1);

    public native boolean setProxyAddress(String var1);

    public void setRestrainAutoInstall(int n) {
        DeviceUtils.setRestrainAutoInstall(this.mContext, n);
    }

    public int startUpdate() {
        return this.mIpl.startUpdate();
    }

    /*
     * Enabled aggressive block sorting
     */
    public void updateNotificationIcon(int n, boolean bl) {
        DmcLog.v("ClientService", "updateNotificationIcon()" + n);
        String string2 = this.dmcService.getString(2131099700);
        String string3 = this.dmcService.getString(2131099701);
        boolean bl2 = true;
        boolean bl3 = false;
        switch (n) {
            default: {
                DmcLog.d("ClientService", "UN SUPPORTED NOTIFICATION FORMAT");
                break;
            }
            case 1: {
                string2 = this.dmcService.getString(2131099690);
                string3 = this.dmcService.getString(2131099691);
                bl2 = false;
                break;
            }
            case 2: {
                string2 = this.isWiFiOnly() ? this.dmcService.getString(2131099693) : this.dmcService.getString(2131099692);
                string3 = this.dmcService.getString(2131099694);
                break;
            }
            case 3: {
                string2 = this.dmcService.getString(2131099696);
                string3 = this.dmcService.getString(2131099697);
                break;
            }
            case 4: {
                string2 = this.dmcService.getString(2131099698);
                string3 = this.dmcService.getString(2131099699);
                bl3 = true;
                break;
            }
            case 5: 
            case 11: 
            case 12: {
                string2 = this.dmcService.getString(2131099700);
                string3 = this.dmcService.getString(2131099701);
                break;
            }
            case 6: 
            case 7: {
                string2 = this.dmcService.getString(2131099702);
                string3 = this.dmcService.getString(2131099703);
                break;
            }
            case 8: {
                string2 = this.dmcService.getString(2131099704);
                string3 = this.dmcService.getString(2131099705);
                break;
            }
            case 9: {
                string2 = this.dmcService.getString(2131099696);
                string3 = this.dmcService.getString(2131099697);
                break;
            }
            case 10: {
                string2 = this.dmcService.getString(2131099707);
                string3 = this.dmcService.getString(2131099708);
                bl2 = false;
                break;
            }
            case 13: {
                string2 = this.dmcService.getString(2131099709);
                bl2 = false;
                break;
            }
            case 14: {
                string2 = this.dmcService.getString(2131099710);
                bl2 = false;
                break;
            }
            case 15: {
                string2 = this.dmcService.getString(2131099711);
                string3 = this.dmcService.getString(2131099712);
                bl2 = false;
            }
        }
        DmcUtils dmcUtils = this.dmcService.u;
        bl = bl;
        dmcUtils.showNotification(n, 2130837504, bl, string3, this.dmcService.getString(2131099671), string2, false, bl2, bl3);
    }

    public void updateProgressBar(int n) {
        this.dmcApplication.sendMessage(5007, n);
    }
}

