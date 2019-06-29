/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import com.redbend.vdm.comm.CommHttpAuth;
import com.redbend.vdm.comm.VdmCommException;

public interface CommRawConnection {
    public void close();

    public void init(String var1, CommHttpAuth.Level var2, String var3, String var4, boolean var5) throws VdmCommException;

    public void open(String var1) throws VdmCommException;

    public int receive(byte[] var1) throws VdmCommException;

    public void send(byte[] var1) throws VdmCommException;

    public void setConnectionTimeout(int var1);
}

