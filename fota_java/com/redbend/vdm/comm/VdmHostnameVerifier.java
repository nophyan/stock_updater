/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

class VdmHostnameVerifier
implements HostnameVerifier {
    VdmHostnameVerifier() {
    }

    @Override
    public boolean verify(String string2, SSLSession sSLSession) {
        return true;
    }
}

