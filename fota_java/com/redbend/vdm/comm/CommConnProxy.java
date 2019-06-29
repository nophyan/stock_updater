/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import com.redbend.vdm.comm.CommFactory;
import com.redbend.vdm.comm.CommHttpAuth;
import com.redbend.vdm.comm.CommRawConnection;
import com.redbend.vdm.comm.VdmCommException;
import java.util.ArrayList;

class CommConnProxy {
    private static int _connId;
    private ArrayList<CommRawConnection> _connections;
    private CommFactory _factory;
    private int _timeout = 30;

    static {
        CommConnProxy.initIDs();
        _connId = 0;
    }

    CommConnProxy(CommFactory commFactory) {
        this.initInstance();
        this._factory = commFactory;
        this._connections = new ArrayList();
    }

    private static native void initIDs();

    private native void initInstance();

    void close(CommRawConnection commRawConnection) {
        commRawConnection.close();
    }

    protected native void destroyInstance();

    CommRawConnection init(String string2, int n, String string3, String string4, boolean bl) throws VdmCommException {
        CommRawConnection commRawConnection = this._factory.createRawConnection();
        commRawConnection.init(string2, CommHttpAuth.Level.values()[n], string3, string4, bl);
        commRawConnection.setConnectionTimeout(this._timeout);
        this._connections.add(commRawConnection);
        return commRawConnection;
    }

    int open(String string2, CommRawConnection commRawConnection) throws VdmCommException {
        int n;
        commRawConnection.open(string2);
        _connId = n = _connId + 1;
        return n;
    }

    int receive(byte[] arrby, CommRawConnection commRawConnection) throws VdmCommException {
        return commRawConnection.receive(arrby);
    }

    void send(byte[] arrby, CommRawConnection commRawConnection) throws VdmCommException {
        commRawConnection.send(arrby);
    }

    public void setConnectionTimeout(int n) {
        this._timeout = n;
        int n2 = 0;
        while (n2 < this._connections.size()) {
            this._connections.get(n2).setConnectionTimeout(n);
            ++n2;
        }
        return;
    }

    void term(CommRawConnection commRawConnection) {
        this._connections.remove(commRawConnection);
    }
}

