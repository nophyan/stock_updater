/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.Application
 *  android.os.Message
 */
package com.redbend.dmcFramework;

import android.app.Application;
import android.os.Message;
import com.redbend.dmcFramework.DmcApplication;
import com.redbend.dmcFramework.DmcLog;
import com.redbend.dmcFramework.DmcService;

public abstract class DmClientBaseService {
    protected String LOG_TAG = "DmClientBaseService";
    protected DmcApplication dmcApplication = null;
    protected DmcService dmcService = null;

    public DmClientBaseService(DmcService dmcService) {
        DmcLog.v(this.LOG_TAG, "DmClientBaseService()");
        this.dmcService = dmcService;
        this.dmcApplication = (DmcApplication)this.dmcService.getApplication();
    }

    public abstract void dispatch(Message var1);
}

