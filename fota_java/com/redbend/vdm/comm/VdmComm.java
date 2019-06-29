/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import com.redbend.vdm.comm.CommConnProxy;
import com.redbend.vdm.comm.CommFactory;
import com.redbend.vdm.comm.VdmCommException;
import com.redbend.vdm.comm.VdmRawConnection;

public class VdmComm {
    private CommConnProxy _connProxy;

    /*
     * Enabled aggressive block sorting
     */
    public VdmComm(CommFactory commFactory) throws VdmCommException {
        if (commFactory != null) {
            this._connProxy = new CommConnProxy(commFactory);
        }
        if (this._connProxy != null) {
            return;
        }
        throw new VdmCommException(VdmCommException.VdmCommError.INVALID_INPUT_PARAM.val);
    }

    public static void setCertificatePath(String string2) {
        VdmRawConnection.setCertificatePath(string2);
    }

    public void destroy() {
        if (this._connProxy == null) {
            return;
        }
        this._connProxy.destroyInstance();
    }

    public void setConnectionTimeout(int n) {
        this._connProxy.setConnectionTimeout(n);
    }
}

