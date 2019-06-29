/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

import com.redbend.vdm.comm.VdmCommException;

public class CommHttpAuth {
    private Level _level = Level.NONE;
    private String _usernamePwd = null;

    /*
     * Enabled aggressive block sorting
     */
    public CommHttpAuth(Level level, String string2) throws VdmCommException {
        if (level == Level.NONE && string2 != null) {
            throw new VdmCommException(VdmCommException.VdmCommError.INVALID_INPUT_PARAM.val);
        }
        if (level != Level.NONE && string2 == null) {
            throw new VdmCommException(VdmCommException.VdmCommError.INVALID_INPUT_PARAM.val);
        }
        this._level = level;
        if (string2 == null) {
            return;
        }
        this._usernamePwd = new String(string2);
    }

    public CommHttpAuth(CommHttpAuth commHttpAuth) {
        this._level = commHttpAuth._level;
        if (commHttpAuth._usernamePwd == null) {
            return;
        }
        this._usernamePwd = new String(commHttpAuth._usernamePwd);
    }

    public Level getLevel() {
        return this._level;
    }

    public String toHttpHeaderField() {
        if (this._level == Level.NONE) {
            return null;
        }
        String string2 = new String(this._level.toString());
        return string2 + " " + this._usernamePwd;
    }

    public static enum Level {
        NONE,
        BASIC,
        DIGEST;
        

        private Level() {
        }

        static Level fromInt(int n) {
            Level level;
            Level[] arrlevel = Level.values();
            int n2 = arrlevel.length;
            int n3 = 0;
            do {
                if (n3 >= n2) {
                    return NONE;
                }
                level = arrlevel[n3];
                if (level.ordinal() == n) break;
                ++n3;
            } while (true);
            return level;
        }
    }

}

