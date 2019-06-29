/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import java.util.HashMap;

public class CommHmac {
    private String _algorithm;
    private String _mac;
    private String _username;

    CommHmac() {
        this(null, null, "MD5");
    }

    CommHmac(String string2, String string3) {
        this(string2, string3, "MD5");
    }

    CommHmac(String string2, String string3, String string4) {
        this._algorithm = string2;
        this._username = string3;
        this._mac = string4;
    }

    public void fromHttpHeaderField(String arrstring) {
        HashMap<String, String> hashMap = new HashMap<String, String>();
        arrstring = arrstring.split(",");
        int n = arrstring.length;
        int n2 = 0;
        do {
            if (n2 >= n) {
                this._algorithm = (String)hashMap.get("algorithm");
                this._username = (String)hashMap.get("username");
                this._mac = (String)hashMap.get("mac");
                return;
            }
            String[] arrstring2 = arrstring[n2].split("=", 2);
            hashMap.put(arrstring2[0].trim(), arrstring2[1].trim());
            ++n2;
        } while (true);
    }

    public String get_algorithm() {
        return this._algorithm;
    }

    public String get_mac() {
        return this._mac;
    }

    public String get_username() {
        return this._username;
    }

    public void set_algorithm(String string2) {
        this._algorithm = string2;
    }

    public void set_mac(String string2) {
        this._mac = string2;
    }

    public void set_username(String string2) {
        this._username = string2;
    }

    public String toHttpHeaderField() {
        return new String("algorithm=" + this._algorithm + "," + "username=\"" + this._username + "\"," + "mac=" + this._mac);
    }
}

