/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.telephony.TelephonyManager
 *  android.webkit.WebSettings
 */
package com.redbend.dmClient.ipl;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.webkit.WebSettings;
import com.redbend.dmClient.DeviceUtils;
import com.redbend.dmClient.ipl.DevUtils;
import com.redbend.dmClient.ipl.Ipl;
import com.redbend.dmcFramework.DmcLog;
import java.io.File;

public abstract class DmcIplImpl
implements Ipl,
DevUtils {
    private static final String LOG_TAG = "DmcIplImpl";
    protected Context mContext;
    protected TelephonyManager mTelephonyMgr;

    public DmcIplImpl(Context context) {
        this.mContext = context;
        this.mTelephonyMgr = (TelephonyManager)context.getSystemService("phone");
    }

    @Override
    public String getDeviceId() {
        if (new File("/data/local/tmp/redbend/deviceid").exists()) {
            // empty if block
        }
        return this.mTelephonyMgr.getDeviceId();
    }

    @Override
    public String getFirmwareVersion() {
        DmcLog.d("DmcIplImpl", "+getFirmwareVersion");
        if (new File("/data/local/tmp/redbend/fwver").exists()) {
            // empty if block
        }
        String string2 = Build.DISPLAY;
        DmcLog.d("DmcIplImpl", "+getFirmwareVersion returns " + string2);
        return string2;
    }

    @Override
    public String getHardwareVersion() {
        return Build.HARDWARE;
    }

    @Override
    public String getIMSI() {
        DmcLog.d("DmcIplImpl", "+getIMSI");
        if (new File("/data/local/tmp/redbend/imsi").exists()) {
            // empty if block
        }
        String string2 = this.mTelephonyMgr.getSubscriberId();
        DmcLog.d("DmcIplImpl", "+getIMSI returns " + string2);
        return string2;
    }

    @Override
    public String getMSISDN() {
        DmcLog.d("DmcIplImpl", "+getMSISDN");
        if (new File("/data/local/tmp/redbend/msisdn").exists()) {
            // empty if block
        }
        String string2 = this.mTelephonyMgr.getLine1Number();
        DmcLog.d("DmcIplImpl", "+getMSISDN returns " + string2);
        return string2;
    }

    @Override
    public String getManName() {
        if (new File("/data/local/tmp/redbend/manname").exists()) {
            // empty if block
        }
        return Build.MANUFACTURER;
    }

    @Override
    public String getModelName() {
        if (new File("/data/local/tmp/redbend/modelname").exists()) {
            // empty if block
        }
        return Build.MODEL;
    }

    @Override
    public String getSoftwareVersion() {
        return this.getFirmwareVersion();
    }

    @Override
    public String getUpdatePackagePath() {
        String string2 = DeviceUtils.getFileText(new StringBuilder().append(this.mContext.getFilesDir().toString()).append("/").append("dp_path").toString()) + "/";
        DmcLog.i("DmcIplImpl", "JAVA IPL getUpdatePackagePath: path is " + string2);
        return string2;
    }

    @Override
    public String getUserAgentName() {
        String string2;
        String string3 = string2 = WebSettings.getDefaultUserAgent((Context)this.mContext);
        if (string2 == null) {
            string3 = "Mozilla/5.0 (Linux; U; Android " + Build.VERSION.RELEASE + "; " + "en; " + Build.MODEL + "; " + "Build/" + Build.DISPLAY + ") " + "AppleWebKit/534.30 (KHTML, like Gecko) Version/4.1 Mobile Safari/534.30";
        }
        return string3;
    }
}

