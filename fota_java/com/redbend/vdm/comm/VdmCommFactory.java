/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import com.redbend.vdm.comm.CommFactory;
import com.redbend.vdm.comm.CommRawConnection;
import com.redbend.vdm.comm.VdmRawConnection;

public class VdmCommFactory
implements CommFactory {
    @Override
    public CommRawConnection createRawConnection() {
        return new VdmRawConnection();
    }
}

