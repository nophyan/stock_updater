/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.vdm.comm;

public class VdmCommException
extends Exception {
    private static final long serialVersionUID = 1;
    public VdmCommError vdmCommError;

    public VdmCommException(int n) {
        this.vdmCommError = VdmCommError.fromInt(n);
    }

    @Override
    public String getMessage() {
        String string2 = super.getMessage();
        if (string2 != null) {
            return string2;
        }
        return Integer.toString(this.vdmCommError.val);
    }

    public static enum VdmCommError {
        INVALID_INPUT_PARAM("VDM_ERR_INVALID_INPUT_PARAM", 2),
        UNSPECIFIC("VDM_ERR_UNSPECIFIC", 16),
        COMMS_BAD_PROTOCOL("VDM_ERR_COMMS_BAD_PROTOCOL", 25344),
        COMMS_MIME_MISMATCH("VDM_ERR_COMMS_MIME_MISMATCH", 25345),
        COMMS_FATAL("VDM_ERR_COMMS_FATAL", 25346),
        COMMS_NON_FATAL("VDM_ERR_COMMS_NON_FATAL", 25347),
        COMMS_SOCKET_TIMEOUT("VDM_ERR_COMMS_SOCKET_TIMEOUT", 25348),
        COMMS_SOCKET_ERROR("VDM_ERR_COMMS_SOCKET_ERROR", 25349),
        COMMS_HTTP_ERROR("VDM_ERR_COMMS_HTTP_ERROR", 25408),
        BAD_URL("VDM_ERR_BAD_URL", 25606);
        
        public String string;
        public int val;

        private VdmCommError(String string3, int n2) {
            this.string = string3;
            this.val = n2;
        }

        public static VdmCommError fromInt(int n) {
            VdmCommError vdmCommError;
            VdmCommError[] arrvdmCommError = VdmCommError.values();
            int n2 = arrvdmCommError.length;
            int n3 = 0;
            do {
                if (n3 >= n2) {
                    return UNSPECIFIC;
                }
                vdmCommError = arrvdmCommError[n3];
                if (vdmCommError.val == n) break;
                ++n3;
            } while (true);
            return vdmCommError;
        }
    }

}

