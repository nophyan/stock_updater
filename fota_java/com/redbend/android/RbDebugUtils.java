/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.android;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RbDebugUtils {
    private static int readFile(String object, char[] arrc) throws IOException {
        object = new InputStreamReader(new FileInputStream((String)object));
        int n = object.read(arrc);
        object.close();
        return n;
    }

    public static int readIntValue(String string2) throws IOException {
        char[] arrc = new char[32];
        return Integer.parseInt(String.valueOf(arrc, 0, RbDebugUtils.readFile(string2, arrc)).trim());
    }

    public static long readLongValue(String string2) throws IOException {
        char[] arrc = new char[32];
        return Long.parseLong(String.valueOf(arrc, 0, RbDebugUtils.readFile(string2, arrc)).trim());
    }

    public static String readStringValue(String string2) {
        try {
            char[] arrc = new char[1024];
            string2 = String.valueOf(arrc, 0, RbDebugUtils.readFile(string2, arrc)).trim();
            return string2;
        }
        catch (IOException var0_1) {
            return null;
        }
    }
}

