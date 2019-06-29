/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.android;

public class RbException
extends Exception {
    private static final long serialVersionUID = 1;
    public VdmError vdmError;

    public RbException(int n) {
        this.vdmError = VdmError.fromInt(n);
    }

    public RbException(VdmError vdmError) {
        this.vdmError = vdmError;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public RbException(String string2) {
        int n;
        n = 16;
        super(string2);
        try {
            int n2;
            n = n2 = Integer.parseInt(string2);
        }
        catch (Exception var1_2) {}
        this.vdmError = VdmError.fromInt(n);
    }

    public VdmError getError() {
        return this.vdmError;
    }

    @Override
    public String getMessage() {
        String string2 = super.getMessage();
        if (string2 != null) {
            return string2;
        }
        return Integer.toString(this.vdmError.val);
    }

    public int intValue() {
        return this.vdmError.val;
    }

    public static enum VdmError {
        OK("VDM_ERR_OK", 0),
        INVALID_INPUT_PARAM("VDM_ERR_INVALID_INPUT_PARAM", 2),
        UNSPECIFIC("VDM_ERR_UNSPECIFIC", 16),
        MEMORY("VDM_ERR_MEMORY", 17),
        INVALID_CALL("VDM_ERR_INVALID_CALL", 18),
        IS_SUSPENDED("VDM_ERR_IS_SUSPENDED", 19),
        INVALID_PROTO_OR_VERSION("VDM_ERR_INVALID_PROTO_OR_VERSION", 32),
        RTK_BUFFER_OVERFLOW("VDM_ERR_RTK_BUFFER_OVERFLOW", 8194),
        DATABASE_CREATE_FAILED("VDM_ERR_DATABASE_CREATE_FAILED", 16896),
        DATABASE_OPEN_FAILED("VDM_ERR_DATABASE_OPEN_FAILED", 16897),
        DATABASE_INSERT_DATA_FAILED("VDM_ERR_DATABASE_INSERT_DATA_FAILED", 16898),
        DATABASE_UPDATE_DATA_FAILED("VDM_ERR_DATABASE_UPDATE_DATA_FAILED", 16899),
        DATABASE_SELECT_DATA_FAILED("VDM_ERR_DATABASE_SELECT_DATA_FAILED", 16900),
        DATABASE_DELETE_DATA_FAILED("VDM_ERR_DATABASE_DELETE_DATA_FAILED", 16901),
        DATABASE_PURGE_DATA_FAILED("VDM_ERR_DATABASE_PURGE_DATA_FAILED", 16902),
        DATABASE_UPGRADE_FAILED("VDM_ERR_DATABASE_UPGRADE_FAILED", 16903),
        BAD_MSG("VDM_ERR_BAD_MSG", 16944),
        BUFFER_OVERFLOW("VDM_ERR_BUFFER_OVERFLOW", 24576),
        BAD_INPUT("VDM_ERR_BAD_INPUT", 24577),
        ALREADY_EXISTS("VDM_ERR_ALREADY_EXISTS", 24578),
        NODE_MISSING("VDM_ERR_NODE_MISSING", 24579),
        PARENT_MISSING("VDM_ERR_PARENT_MISSING", 24580),
        LEAF_NODE("VDM_ERR_LEAF_NODE", 24581),
        NOT_LEAF_NODE("VDM_ERR_NOT_LEAF_NODE", 24582),
        UNKNOWN_PROPERTY("VDM_ERR_UNKNOWN_PROPERTY", 24583),
        PERMANENT_NODE("VDM_ERR_PERMANENT_NODE", 24584),
        NOT_ALLOWED("VDM_ERR_NOT_ALLOWED", 24585),
        ABORT("VDM_ERR_ABORT", 24586),
        TREE_ACCESS_DENIED("VDM_ERR_TREE_ACCESS_DENIED", 24587),
        TREE_EXT_NOT_PARTIAL("VDM_ERR_TREE_EXT_NOT_PARTIAL", 24588),
        TREE_EXT_NOT_ALLOWED("TREE_VDM_ERR_EXT_NOT_ALLOWED", 24589),
        MAY_TREE_NOT_REPLACE("VDM_TREE_ERR_MAY_NOT_REPLACE", 24590),
        STORAGE_READ("VDM_ERR_STORAGE_READ", 24591),
        STORAGE_WRITE("VDM_ERR_STORAGE_WRITE", 24592),
        AUTHENTICATION("VDM_ERR_AUTHENTICATION", 24593),
        NODE_ACCESS_DENIED("VDM_ERR_NODE_ACCESS_DENIED", 24594),
        NODE_VALUE_NOT_READABLE("VDM_ERR_NODE_VALUE_NOT_READABLE", 24595),
        NODE_VALUE_NODE_NOT_WRITEABLE("VDM_ERR_NODE_VALUE_NOT_WRITEABLE", 24596),
        NODE_NOT_EXECUTABLE("VDM_ERR_NODE_NOT_EXECUTABLE", 24597),
        STORAGE_OPEN("VDM_ERR_STORAGE_OPEN", 24598),
        STORAGE_COMMIT("VDM_ERR_STORAGE_COMMIT", 24599),
        NO_MORE_COMMANDS("VDM_ERR_NO_MORE_COMMANDS", 24832),
        MISSING_START_MESSAGE_CMD("VDM_ERR_MISSING_START_MESSAGE_CMD", 24833),
        MISSING_STATUS_CMD("VDM_ERR_MISSING_STATUS_CMD", 24834),
        NOT_IMPLEMENTED("VDM_ERR_NOT_IMPLEMENTED", 24835),
        ALERT_PARSING_ERROR("VDM_ERR_ALERT_PARSING_ERROR", 24837),
        ALERT_MISSING_ITEMS("VDM_ERR_ALERT_MISSING_ITEMS", 24838),
        ALERT_MISSING_DATA("VDM_ERR_ALERT_MISSING_DATA", 24839),
        NO_DATA("VDM_ERR_NO_DATA", 24840),
        ALERT_USER_ABORTED("VDM_ERR_ALERT_USER_ABORTED", 24841),
        ALERT_TOO_MANY_CHOICES("VDM_ERR_ALERT_TOO_MANY_CHOICES", 24842),
        ALERT_SESSION_ABORTED("VDM_ERR_ALERT_SESSION_ABORTED", 24843),
        LO_HANDLED("VDM_ERR_LO_HANDLED", 24844),
        TOO_BIG("VDM_ERR_TOO_BIG", 24845),
        COMMAND_FAILED("VDM_ERR_COMMAND_FAILED", 24846),
        NOTIF_BAD_LENGTH("VDM_ERR_NOTIF_BAD_LENGTH", 25088),
        NOTIF_BAD_DIGEST("VDM_ERR_NOTIF_BAD_DIGEST", 25089),
        BOOT_DIGEST("VDM_ERR_BOOT_DIGEST", 25090),
        BOOT_NSS("VDM_ERR_BOOT_NSS", 25091),
        BOOT_PIN("VDM_ERR_BOOT_PIN", 25092),
        BOOT_PINLENGTH("VDM_ERR_BOOT_PINLENGTH", 25093),
        BOOT_BAD_SEC("VDM_ERR_BOOT_BAD_SEC", 25094),
        BOOT_BAD_MAC("VDM_ERR_BOOT_BAD_MAC", 25095),
        BOOT_BAD_MESSAGE("VDM_ERR_BOOT_BAD_MESSAGE", 25096),
        BOOT_BAD_PROF("VDM_ERR_BOOT_BAD_PROF", 25097),
        TRG_BAD_REASON("VDM_ERR_TRG_BAD_REASON", 25104),
        NOTIF_UNSUPPORTED_VERSION("VDM_ERR_NOTIF_UNSUPPORTED_VERSION", 25105),
        BOOT_DISABLED("VDM_ERR_BOOT_DISABLED", 25106),
        DL_OBJ_TOO_LARGE("VDM_ERR_DL_OBJ_TOO_LARGE", 25108),
        COMMS_BAD_PROTOCOL("VDM_ERR_COMMS_BAD_PROTOCOL", 25344),
        COMMS_MIME_MISMATCH("VDM_ERR_COMMS_MIME_MISMATCH", 25345),
        COMMS_FATAL("VDM_ERR_COMMS_FATAL", 25346),
        COMMS_NON_FATAL("VDM_ERR_COMMS_NON_FATAL", 25347),
        COMMS_SOCKET_TIMEOUT("VDM_ERR_COMMS_SOCKET_TIMEOUT", 25348),
        COMMS_SOCKET_ERROR("VDM_ERR_COMMS_SOCKET_ERROR", 25349),
        COMMS_HTTP_ERROR("VDM_ERR_COMMS_HTTP_ERROR", 25408),
        INTERNAL("VDM_ERR_INTERNAL", 25601),
        MO_STORAGE("VDM_ERR_MO_STORAGE", 25602),
        CANCEL("VDM_ERR_CANCEL", 25604),
        UPDATE_INIT("VDM_ERR_UPDATE_INIT", 25605),
        BAD_URL("VDM_ERR_BAD_URL", 25606),
        BAD_DD("VDM_ERR_BAD_DD", 25607),
        COMMS_OBJECT_CHANGED("VDM_ERR_COMMS_OBJECT_CHANGED", 25608),
        OUT_OF_SYNC("VDM_ERR_OUT_OF_SYNC", 25856),
        REGISTRY("VDM_ERR_REGISTRY", 32769),
        SHUTTING_DOWN("VDM_ERR_SHUTTING_DOWN", 32770),
        OUT_OF_BOUNDS("VDM_ERR_OUT_OF_BOUNDS", 32771),
        STORAGE_REMOVE("VDM_ERR_STORAGE_REMOVE", 32772);
        
        public String string;
        public int val;

        private VdmError(String string3, int n2) {
            this.string = string3;
            this.val = n2;
        }

        public static VdmError fromInt(int n) {
            VdmError vdmError;
            VdmError[] arrvdmError = VdmError.values();
            int n2 = arrvdmError.length;
            int n3 = 0;
            do {
                if (n3 >= n2) {
                    return UNSPECIFIC;
                }
                vdmError = arrvdmError[n3];
                if (vdmError.val == n) break;
                ++n3;
            } while (true);
            return vdmError;
        }
    }

}

