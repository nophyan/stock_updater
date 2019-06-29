/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.dmClient.ipl;

public interface Ipl {
    public String getDLProxyAddress();

    public String getDMProxyAddress();

    public String getFirmwareVersion();

    public long getPackageStorageSize(String var1);

    public String getUpdatePackagePath();

    public String getUpdatePackagePathPreDownload(long var1);

    public int getUpdateResult();

    public String getUserAgentName();

    public boolean isBatterySufficient();

    public boolean isExternalStorageUsed();

    public boolean isPostDownloadRequired(String var1);

    public boolean isProxyAddressNeeded();

    public int onPostDownload();

    public int postUpdate();

    public int startUpdate();
}

