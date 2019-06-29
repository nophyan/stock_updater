/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.dmClient;

import com.redbend.dmClient.ClientService;
import com.redbend.dmcFramework.DmcApplication;

public class DmClientApplication
extends DmcApplication {
    @Override
    public String getClientServiceClassName() {
        return ClientService.class.getName();
    }
}

