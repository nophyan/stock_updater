/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.content.Context
 */
package com.redbend.dmClient.ipl;

import android.content.Context;
import com.redbend.dmClient.ipl.DmcIplImpl;
import com.redbend.dmClient.ipl.ExternalIplImpl;

public class IplFactory {
    public static DmcIplImpl create(Context context) {
        return IplFactory.create(context, "default");
    }

    public static DmcIplImpl create(Context context, String string2) {
        return new ExternalIplImpl(context);
    }
}

