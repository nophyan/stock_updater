/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.dmClient.ipl;

public class AskFotaManager {
    public final int FOTA_JNI_E_F_S_EXIST = 1;
    public final int FOTA_JNI_E_F_S_NONE = 0;
    public final int FOTA_JNI_NG = -1;
    public final int FOTA_JNI_OK = 0;
    public final String FOTA_JNI_PASS_CODE = "f0o7t2a2";
    public final int FOTA_JNI_UPDATE_FOTA = 0;
    public final int FOTA_JNI_UPDATE_MJUPD = 1;
    public final int FOTA_JNI_UPDATE_RESULT_DLERROR = 402;
    public final int FOTA_JNI_UPDATE_RESULT_ERROR = 401;
    public final int FOTA_JNI_UPDATE_RESULT_OFF = -1;
    public final int FOTA_JNI_UPDATE_RESULT_SDREADERROR = 403;
    public final int FOTA_JNI_UPDATE_RESULT_SUCCESS = 200;
    public final int FOTA_JNI_USE_EXTERNAL_SD = 1;
    public final int FOTA_JNI_USE_INTERNAL_MEM = 0;
    public final int FOTA_JNI_USE_INTERNAL_SD = 2;

    public final synchronized native int jniFotaDeltaDelete();

    public final synchronized native int jniFotaMd5(byte[] var1, int[] var2, byte[] var3, int[] var4, byte[] var5, int var6, byte[] var7);

    public final synchronized native long jniFotaMgrCalcCrc(long var1, byte[] var3, int var4);

    public final synchronized native int jniFotaMgrClearCaches();

    public final synchronized native int jniFotaMgrClearUpdateResult_valuation();

    public final synchronized native int jniFotaMgrEndSoftwareUpdate();

    public final synchronized native int jniFotaMgrGetCurrentFwVStrings(byte[] var1, byte[] var2);

    public final synchronized native int jniFotaMgrGetDeltaCrc(byte[] var1, long[] var2);

    public final synchronized native int jniFotaMgrGetDummyFlag(String var1, int[] var2);

    public final synchronized native int jniFotaMgrGetImageDeltaCrc(byte[] var1, long[] var2);

    public final synchronized native int jniFotaMgrGetImageDeltaInfo(byte[] var1, int[] var2, int[] var3);

    public final synchronized native int jniFotaMgrGetParamaterInfo(byte[] var1);

    public final synchronized native int jniFotaMgrGetParamaterInfoEx(byte[] var1, int var2);

    public final synchronized native int jniFotaMgrGetRomSumInfo();

    public final synchronized native int jniFotaMgrGetSoftwareUpdateResult();

    public final synchronized native int jniFotaMgrGetUEFK(byte[] var1, byte[] var2);

    public final synchronized native int jniFotaMgrGetUpdateResult_valuation();

    public final synchronized native int jniFotaMgrSetDeltaName(byte[] var1, int var2, int var3, byte[] var4, int var5, int var6);

    public final synchronized native int jniFotaMgrSetDummyFlag(int var1);

    public final synchronized native int jniFotaMgrSetUEFK(byte[] var1, long var2, int var4);

    public final synchronized native int jniFotaMgrStartSoftwareUpdate();

    public final synchronized native int jniFotaMgrStartToCalcRomSum();

    public final synchronized native int jniFotaMgrVerifyRewriteData(byte[] var1);
}

