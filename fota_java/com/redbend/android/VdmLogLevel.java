/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.android;

public enum VdmLogLevel {
    ERROR(1),
    WARNING(3),
    NOTICE(4),
    INFO(5),
    DEBUG(6),
    VERBOSE(7);
    
    private int _val;

    private VdmLogLevel(int n2) {
        this._val = n2;
    }

    public int val() {
        return this._val;
    }
}

