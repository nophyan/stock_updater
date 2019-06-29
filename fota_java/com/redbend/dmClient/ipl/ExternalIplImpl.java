/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.app.KeyguardManager
 *  android.app.KeyguardManager$KeyguardLock
 *  android.content.BroadcastReceiver
 *  android.content.ComponentName
 *  android.content.Context
 *  android.content.Intent
 *  android.content.IntentFilter
 *  android.content.ServiceConnection
 *  android.content.SharedPreferences
 *  android.content.SharedPreferences$Editor
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Environment
 *  android.os.IBinder
 *  android.os.PowerManager
 *  android.os.PowerManager$WakeLock
 *  android.os.RemoteException
 *  android.os.StatFs
 *  android.os.storage.StorageManager
 *  com.android.internal.widget.LockPatternUtils
 */
package com.redbend.dmClient.ipl;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.StatFs;
import android.os.storage.StorageManager;
import com.android.internal.widget.LockPatternUtils;
import com.redbend.dmClient.DeviceUtils;
import com.redbend.dmClient.ipl.AskFotaManager;
import com.redbend.dmClient.ipl.DmcIplImpl;
import com.redbend.dmClient.ipl.Ipl;
import com.redbend.dmcFramework.DmcLog;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.Arrays;
import jp.co.sharp.android.fotarecovery.IFotaRecoveryService;

/*
 * Illegal identifiers - consider using --renameillegalidents true
 */
public class ExternalIplImpl
extends DmcIplImpl
implements Ipl {
    public static final int BATTERY_LIMIT = 40;
    public static final int BATTERY_LIMIT_MASK = 255;
    public static final int BATTERY_LIMIT_MAX = 100;
    public static final int DELTA_COPY_INVALID = 99;
    public static final int DELTA_COPY_NECESSARY = 1;
    public static final int DELTA_COPY_UNNECESSARY = 0;
    public static final long DELTA_HEADER_SIZE = 2232;
    public static final String DELTA_STORAGE_INTERNAL_MEMORY_PATH = "/fota";
    public static final int DELTA_STORAGE_POSITION_EXTERNAL_SD = 1;
    public static final int DELTA_STORAGE_POSITION_INTERNAL_MEMORY = 0;
    public static final int DELTA_STORAGE_POSITION_INTERNAL_SD = 2;
    public static final int DELTA_STORAGE_POSITION_INVALID = 99;
    public static final int DELTA_STORAGE_POSITION_SHARED_MEMORY = 3;
    public static final String DELTA_STORAGE_SHARED_MEMORY_PATH = "/data/fota_sharp";
    public static final String FOTA_REBOOT_STR = "";
    public static final int FREESIZE_ADJUSTED_VALUE = 104857600;
    public static final int GAP_SIZE_FIX_VALUE = 104857600;
    public static final int HS_SAVE_DELTA_LIMIT_SIZE = 104857600;
    public static final int IPL_UPDATE_RESULT_DLERROR = 402;
    public static final int IPL_UPDATE_RESULT_ERROR = 401;
    public static final int IPL_UPDATE_RESULT_OFF = -1;
    public static final int IPL_UPDATE_RESULT_SDREADERROR = 403;
    public static final int IPL_UPDATE_RESULT_SUCCESS = 0;
    private static final int JAVA_LONG_BYTE = 8;
    private static final String LOG_TAG = "ExternalIplImpl";
    public static final int MLTPL_DELTA_ONE_LIMIT_SIZE = 83886080;
    private static final int ON_POST_DOWNLOAD_FAILED = 0;
    private static final int ON_POST_DOWNLOAD_SUCCESS = 1;
    public static final int SPLIT_FILE_READ_SIZE = 524288;
    public static final int STORAGEINFO_INSUFFICIENT = 2;
    public static final int STORAGEINFO_NOT_MOUNTED = 4;
    public static final int STORAGEINFO_NOT_USE = 5;
    public static final int STORAGEINFO_NOT_WRITE = 6;
    public static final int STORAGEINFO_OK = 0;
    public static final int STORAGEINFO_PARAM_ERROR = 1;
    public static final int STORAGEINFO_PREPARING = 3;
    public static final int UPDATE_DISK_INSUFFICIENT = 3;
    public static final int UPDATE_FAILED_GENERAL = 1;
    public static final int UPDATE_INVALID_PACKAGE = 2;
    public static final int UPDATE_SUCCEESS = 0;
    public static AskFotaManager m_ask_fota_manager;
    byte[] deltaReadBuff = null;
    private IFotaRecoveryService mService;
    private ServiceConnection mServiceConnection;

    static {
        System.loadLibrary("AskFotaManager");
        m_ask_fota_manager = new AskFotaManager();
    }

    public ExternalIplImpl(Context context) {
        super(context);
        this.mServiceConnection = new ServiceConnection(){

            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                DmcLog.v("ExternalIplImpl", "onServiceConnected");
                ExternalIplImpl.this.mService = IFotaRecoveryService.Stub.asInterface(iBinder);
                ExternalIplImpl.this.InstallPackage();
            }

            public void onServiceDisconnected(ComponentName componentName) {
                DmcLog.v("ExternalIplImpl", "onServiceDisconnected");
                ExternalIplImpl.this.mService = null;
            }
        };
        DmcLog.v("ExternalIplImpl", "ExternalIplImpl(Context) started");
        if (m_ask_fota_manager == null) {
            m_ask_fota_manager = new AskFotaManager();
        }
        DmcLog.v("ExternalIplImpl", "ExternalIplImpl(Context) return");
    }

    public static boolean FOTA_Filedelete(String string2) {
        DmcLog.v("ExternalIplImpl", "FOTA_Filedelete()started: delete file path:" + string2);
        if (new File(string2).delete()) {
            DmcLog.d("ExternalIplImpl", "FOTA_Filedelete(): END(delete success)");
            DmcLog.v("ExternalIplImpl", "FOTA_Filedelete() returning:true");
            return true;
        }
        DmcLog.e("ExternalIplImpl", "FOTA_Filedelete(): END(delete fail)");
        DmcLog.v("ExternalIplImpl", "FOTA_Filedelete() returning:false");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean checkAllDeltaCrc() {
        int n;
        long l;
        DmcLog.v("ExternalIplImpl", "checkAllDeltaCrc() started");
        boolean bl = true;
        long l2 = 0;
        long[] arrl = new long[1];
        byte[] arrby = this.getDeltaFileName();
        long l3 = new File((String)arrby).length();
        DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : fileSize=" + l3);
        DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : splitFileReadSize=" + 524288);
        this.deltaReadBuff = null;
        if (l3 <= 524288) {
            this.deltaReadBuff = new byte[(int)(l3 - 8)];
            byte[] arrby2 = this.readDeltaFile((String)arrby, 8, (int)(l3 - 8));
            l = m_ask_fota_manager.jniFotaMgrCalcCrc(0, arrby2, arrby2.length);
        } else {
            long l4;
            byte[] arrby3;
            this.deltaReadBuff = new byte[524288];
            l = l3 - 524288;
            DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : sizechk=" + l);
            for (l4 = 8; l4 < l; l4 += 524288) {
                DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : idx=" + l4);
                arrby3 = this.readDeltaFile((String)arrby, l4, 524288);
                l2 = m_ask_fota_manager.jniFotaMgrCalcCrc(l2, arrby3, arrby3.length);
            }
            l = l2;
            if (true) {
                n = (int)(l3 - l4);
                DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : remainSize=" + n);
                l = l2;
                if (n > 0) {
                    DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : Remain CRC calculate");
                    this.deltaReadBuff = new byte[n];
                    arrby3 = this.readDeltaFile((String)arrby, l4, n);
                    l = m_ask_fota_manager.jniFotaMgrCalcCrc(l2, arrby3, arrby3.length);
                }
            }
        }
        arrby = this.readFileHeader((String)arrby);
        n = m_ask_fota_manager.jniFotaMgrGetDeltaCrc(arrby, arrl);
        m_ask_fota_manager.getClass();
        if (n != 0) {
            bl = false;
        } else {
            DmcLog.d("ExternalIplImpl", "checkAllDeltaCrc() : calculateCrc=0x" + Long.toHexString(l) + "/allDeltaCrc=0x" + Long.toHexString(arrl[0]));
            if (l != arrl[0]) {
                bl = false;
            }
        }
        DmcLog.v("ExternalIplImpl", "checkAllDeltaCrc() returning: result=" + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean checkImageDeltaCrc(int n, int n2) {
        long l;
        DmcLog.v("ExternalIplImpl", "checkImageDeltaCrc() started");
        boolean bl = true;
        long l2 = 0;
        long[] arrl = new long[1];
        byte[] arrby = this.getDeltaFileName();
        byte[] arrby2 = this.getImageDeltaStoragePath();
        n2 = (int)new File((String)arrby2).length();
        DmcLog.d("ExternalIplImpl", "checkImageDeltaCrc() : splitFileReadSize=" + 524288);
        this.deltaReadBuff = null;
        if (n2 <= 524288) {
            this.deltaReadBuff = new byte[n2 - 16];
            arrby2 = this.readDeltaFile((String)arrby2, 16, n2 - 16);
            l = m_ask_fota_manager.jniFotaMgrCalcCrc(0, arrby2, arrby2.length);
        } else {
            this.deltaReadBuff = new byte[524288];
            for (n = 16; n < n2 - 524288; n += 524288) {
                byte[] arrby3 = this.readDeltaFile((String)arrby2, n, 524288);
                l2 = m_ask_fota_manager.jniFotaMgrCalcCrc(l2, arrby3, arrby3.length);
            }
            DmcLog.d("ExternalIplImpl", "checkImageDeltaCrc() : remainSize=" + (n2 -= n));
            l = l2;
            if (n2 > 0) {
                this.deltaReadBuff = new byte[n2];
                arrby2 = this.readDeltaFile((String)arrby2, n, n2);
                l = m_ask_fota_manager.jniFotaMgrCalcCrc(l2, arrby2, arrby2.length);
            }
        }
        arrby = this.readFileHeader((String)arrby);
        n = m_ask_fota_manager.jniFotaMgrGetImageDeltaCrc(arrby, arrl);
        m_ask_fota_manager.getClass();
        if (n != 0) {
            bl = false;
        } else {
            DmcLog.d("ExternalIplImpl", "checkImageDeltaCrc() : calculateCrc=0x" + Long.toHexString(l) + "/imageDeltaCrc=0x" + Long.toHexString(arrl[0]));
            if (l != arrl[0]) {
                bl = false;
            }
        }
        DmcLog.v("ExternalIplImpl", "checkImageDeltaCrc() returning: result=" + bl);
        return bl;
    }

    private long convertToLongByLittleEndian(byte[] arrby) throws Exception {
        DmcLog.v("ExternalIplImpl", "convertToLongByLittleEndian() started");
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        int n = byteBuffer.capacity();
        for (int i = 0; i < n; ++i) {
            byteBuffer.put(i, 0);
        }
        byteBuffer.put(arrby, 0, arrby.length);
        byteBuffer.rewind();
        byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
        long l = byteBuffer.getLong();
        DmcLog.v("ExternalIplImpl", "convertToLongByLittleEndian() returning:" + l);
        return l;
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private boolean copyImageDelta() {
        block12 : {
            DmcLog.v("ExternalIplImpl", "copyImageDelta() started");
            var3_1 = true;
            var6_2 = new int[1];
            var7_3 = new int[1];
            var5_4 = this.getDeltaFileName();
            var8_8 = this.readFileHeader((String)var5_4);
            var1_9 = ExternalIplImpl.m_ask_fota_manager.jniFotaMgrGetImageDeltaInfo((byte[])var8_8, var6_2, var7_3);
            ExternalIplImpl.m_ask_fota_manager.getClass();
            if (var1_9 == 0) ** GOTO lbl13
            DmcLog.d("ExternalIplImpl", "copyImageDelta() : GetImageDeltaInfo Error");
            var4_10 = false;
            ** GOTO lbl41
lbl13: // 1 sources:
            DmcLog.d("ExternalIplImpl", "copyImageDelta() : ImageDelta range=" + var6_2[0] + "-" + var7_3[0]);
            var8_8 = this.getImageDeltaStoragePath();
            if (var8_8 != "") ** GOTO lbl18
            var3_1 = false;
            ** GOTO lbl44
lbl18: // 1 sources:
            if (var7_3[0] >= 1 && var7_3[0] <= 104857600) ** GOTO lbl22
            DmcLog.d("ExternalIplImpl", "copyImageDelta() : Image delta size NG");
            var3_1 = false;
            ** GOTO lbl44
lbl22: // 1 sources:
            DmcLog.d("ExternalIplImpl", "copyImageDelta() : Image delta size OK");
            ExternalIplImpl.deletionFiles(this.getInternalMemoryPath());
            DmcLog.d("ExternalIplImpl", "copyImageDelta() : splitFileReadSize=" + 524288);
            var8_8 = new RandomAccessFile((String)var8_8, "rw");
            {
                catch (Exception var5_7) {
                    ** GOTO lbl-1000
                }
            }
            this.deltaReadBuff = null;
            if (var7_3[0] > 524288) ** GOTO lbl39
            try {
                this.deltaReadBuff = new byte[var7_3[0]];
                var5_4 = this.readDeltaFile((String)var5_4, var6_2[0], var7_3[0]);
                DmcLog.d("ExternalIplImpl", "copyImageDelta() : ImageDelta Write");
                var8_8.write(var5_4);
                ** GOTO lbl55
lbl39: // 2 sources:
                this.deltaReadBuff = new byte[524288];
                break block12;
lbl41: // 4 sources:
                do {
                    DmcLog.v("ExternalIplImpl", "copyImageDelta() returning: result=" + var4_10);
                    return var4_10;
                    break;
                } while (true);
lbl44: // 4 sources:
                do {
                    var4_10 = var3_1;
                    if (!var3_1) ** GOTO lbl41
                    var1_9 = ExternalIplImpl.m_ask_fota_manager.jniFotaMgrClearCaches();
                    ExternalIplImpl.m_ask_fota_manager.getClass();
                    if (var1_9 == 0) ** GOTO lbl53
                    DmcLog.d("ExternalIplImpl", "copyImageDelta() : jniFotaMgrClearCaches NG");
                    var4_10 = false;
                    ** GOTO lbl41
lbl53: // 1 sources:
                    var4_10 = this.checkImageDeltaCrc(var6_2[0], var7_3[0]);
                    ** continue;
                    break;
                } while (true);
lbl55: // 3 sources:
                do {
                    var8_8.getFD().sync();
                    var8_8.close();
                    break;
                } while (true);
            }
            catch (Exception var5_5) lbl-1000: // 2 sources:
            {
                DmcLog.d("ExternalIplImpl", "copyImageDelta() : File write NG " + var5_6);
                var3_1 = false;
            }
            ** while (true)
        }
        for (var1_9 = var6_2[0]; var1_9 < var7_3[0] - 524288; var1_9 += 524288) {
            if (var1_9 <= 0) ** GOTO lbl67
            {
                var8_8.seek(var1_9);
lbl67: // 2 sources:
                DmcLog.d("ExternalIplImpl", "copyImageDelta() : File position=" + var8_8.getFilePointer());
                var9_11 = this.readDeltaFile((String)var5_4, var1_9, 524288);
                DmcLog.d("ExternalIplImpl", "copyImageDelta() : ImageDelta Write");
                var8_8.write(var9_11);
            }
        }
        var2_12 = var7_3[0] - var1_9;
        {
            DmcLog.d("ExternalIplImpl", "copyImageDelta() : remainSize=" + var2_12);
            if (var2_12 <= 0) ** GOTO lbl55
            this.deltaReadBuff = new byte[var2_12];
            var5_4 = this.readDeltaFile((String)var5_4, var1_9, var2_12);
            var8_8.seek(var1_9);
            var8_8.write(var5_4);
            ** continue;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static void deletionFiles(String string2) {
        synchronized (ExternalIplImpl.class) {
            DmcLog.d("ExternalIplImpl", "deletionFiles() started: path=" + string2);
            String[] arrstring = new File(string2).list();
            int n = 0;
            int n2 = arrstring.length;
            do {
                if (n >= n2) {
                    DmcLog.d("ExternalIplImpl", "deletionFiles() : End");
                    DmcLog.v("ExternalIplImpl", "deletionFiles() return");
                    return;
                }
                String string3 = arrstring[n];
                string3 = string2 + "/" + string3;
                File file = new File(string3);
                if (file.isDirectory()) {
                    ExternalIplImpl.deletionFiles(string3);
                }
                file.delete();
                DmcLog.d("ExternalIplImpl", "deletionFiles() : delete=" + string3);
                ++n;
            } while (true);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean encryptionStorage() {
        int n;
        boolean bl;
        long l;
        DmcLog.v("ExternalIplImpl", "encryptionStorage() started");
        boolean bl2 = true;
        m_ask_fota_manager.getClass();
        byte[] arrby = new byte[64];
        long l2 = 0;
        m_ask_fota_manager.getClass();
        int n2 = 0;
        Arrays.fill(arrby, 0);
        DmcLog.d("ExternalIplImpl", "encryptionStorage(): isDeviceEncryptionEnabled()");
        if (new LockPatternUtils(this.mContext) == null) {
            DmcLog.d("ExternalIplImpl", "encryptionStorage(): LockPatternUtils Get NG");
            bl = false;
            l = l2;
            n = n2;
        } else {
            bl = bl2;
            n = n2;
            l = l2;
            if (LockPatternUtils.isDeviceEncryptionEnabled()) {
                DmcLog.d("ExternalIplImpl", "encryptionStorage(): EncryptionStatus = ENCRYPTION_STATUS_ACTIVE");
                byte[] arrby2 = new byte[32];
                byte[] arrby3 = new byte[4];
                Arrays.fill(arrby2, 0);
                Arrays.fill(arrby3, 0);
                n = m_ask_fota_manager.jniFotaMgrGetUEFK(arrby2, arrby3);
                m_ask_fota_manager.getClass();
                if (n != 0) {
                    DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrGetUEFK NG");
                    bl = false;
                    n = n2;
                    l = l2;
                } else {
                    DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrGetUEFK getKey =" + new String(arrby2));
                    DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrGetUEFK getSize[0]" + arrby3[0] + " [1]" + arrby3[1] + " [2]" + arrby3[2] + " [3]" + arrby3[3]);
                    l = l2;
                    try {
                        l = l2 = this.convertToLongByLittleEndian(arrby3);
                        System.arraycopy((byte[])arrby2, (int)0, (byte[])arrby, (int)0, (int)arrby2.length);
                        l = l2;
                        m_ask_fota_manager.getClass();
                        n = 1;
                        bl = bl2;
                        l = l2;
                    }
                    catch (Exception var10_9) {
                        DmcLog.e("ExternalIplImpl", "encryptionStorage(): byte array to long(LE) change Exception=" + var10_9);
                        bl = false;
                        n = n2;
                    }
                }
            }
        }
        bl2 = bl;
        if (bl) {
            DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrSetUEFK setKey=" + new String(arrby));
            DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrSetUEFK size  =" + l);
            DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrSetUEFK state =" + n);
            n = m_ask_fota_manager.jniFotaMgrSetUEFK(arrby, l, n);
            m_ask_fota_manager.getClass();
            bl2 = bl;
            if (n != 0) {
                DmcLog.d("ExternalIplImpl", "encryptionStorage(): jniFotaMgrSetUEFK NG");
                bl2 = false;
            }
        }
        DmcLog.v("ExternalIplImpl", "encryptionStorage() returning: result =" + bl2);
        return bl2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private String getSetDeltaFileName(String var1_1, String var2_2) {
        DmcLog.v("ExternalIplImpl", "getSetDeltaFileName() started");
        DmcLog.d("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName sdPath = " + var1_1);
        var4_3 = var2_2;
        DmcLog.d("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName fileName = " + var2_2);
        if (var2_2.startsWith(var1_1)) {
            DmcLog.d("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName path modify");
            var2_2 = var2_2.substring(var1_1.length());
            DmcLog.d("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName modify fileName=" + var2_2);
        } else {
            DmcLog.e("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName unexpected file path");
            var2_2 = var4_3;
        }
        var5_4 = new File(var1_1).list();
        var3_5 = 0;
        do {
            var4_3 = var2_2;
            if (var3_5 >= var5_4.length) ** GOTO lbl21
            if (var5_4[var3_5].equalsIgnoreCase("PRIVATE")) {
                DmcLog.d("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName find private [ " + var5_4[var3_5] + " ]");
                if (new File(var1_1 + "/" + var5_4[var3_5]).isDirectory()) {
                    var4_3 = var2_2.replaceAll("PRIVATE", var5_4[var3_5]);
lbl21: // 2 sources:
                    DmcLog.v("ExternalIplImpl", "getSetDeltaFileName()returning: wkFileName=" + var4_3);
                    return var4_3;
                }
                DmcLog.d("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName " + var5_4[var3_5] + " is not Directy");
            }
            if (var3_5 == var5_4.length - 1) {
                DmcLog.e("ExternalIplImpl", "getSetDeltaFileName(): getSetDeltaFileName private not find");
            }
            ++var3_5;
        } while (true);
    }

    /*
     * Enabled aggressive block sorting
     */
    private boolean setDeltaName(String arrby) {
        DmcLog.v("ExternalIplImpl", "setDeltaName() started");
        boolean bl = true;
        byte[] arrby2 = null;
        int n = 0;
        Object var9_5 = null;
        int n2 = 0;
        int n3 = 0;
        int n4 = this.getDownloadDeltaStoragePosition();
        switch (n4) {
            default: {
                DmcLog.d("ExternalIplImpl", "setDeltaName(): setDeltaName invalid position");
                bl = false;
                arrby = var9_5;
                break;
            }
            case 0: {
                arrby2 = this.getSetDeltaFileName(this.getInternalMemoryPath(), (String)arrby).getBytes();
                n = arrby2.length;
                m_ask_fota_manager.getClass();
                arrby = arrby2;
                n2 = n;
                n3 = 0;
                break;
            }
            case 1: 
            case 2: 
            case 3: {
                arrby2 = this.getImageDeltaStoragePath();
                arrby2 = this.getSetDeltaFileName(this.getInternalMemoryPath(), (String)arrby2).getBytes();
                n = arrby2.length;
                m_ask_fota_manager.getClass();
                if (n4 == 3) {
                    arrby = this.getSetDeltaFileName(this.getSharedMemoryPath(), (String)arrby).getBytes();
                    m_ask_fota_manager.getClass();
                    n3 = 2;
                } else if (n4 == 1) {
                    arrby = this.getSetDeltaFileName(this.getExternalSdPath(), (String)arrby).getBytes();
                    m_ask_fota_manager.getClass();
                    n3 = 1;
                } else {
                    arrby = this.getSetDeltaFileName(this.getInternalSdPath(), (String)arrby).getBytes();
                    m_ask_fota_manager.getClass();
                    n3 = 2;
                }
                n2 = arrby.length;
            }
        }
        boolean bl2 = bl;
        if (bl) {
            DmcLog.d("ExternalIplImpl", "setDeltaName(): jniFotaMgrSetDeltaName [ImageDelta]FileName=" + new String(arrby2) + ",FileNameLength=" + n + ",FilePosition=" + 0);
            DmcLog.d("ExternalIplImpl", "setDeltaName(): jniFotaMgrSetDeltaName [FsDelta]FileName=" + new String(arrby) + ",FileNameLength=" + n2 + ",FilePosition=" + n3);
            n3 = m_ask_fota_manager.jniFotaMgrSetDeltaName(arrby2, n, 0, arrby, n2, n3);
            m_ask_fota_manager.getClass();
            bl2 = bl;
            if (n3 != 0) {
                bl2 = false;
            }
        }
        DmcLog.v("ExternalIplImpl", "setDeltaName() returning: result=" + bl2);
        return bl2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void CombinedDeltaFileCopy() {
        DmcLog.v("ExternalIplImpl", "CombinedDeltaFileCopy() started");
        Object object = this.getDeltaFileName();
        DmcLog.v("ExternalIplImpl", "CombinedDeltaFileCopy():Delta file :src_path = " + (String)object);
        Object object2 = new File((String)object);
        String string2 = "/fota/" + object2.getName();
        DmcLog.v("ExternalIplImpl", "CombinedDeltaFileCopy():DstDelta file :dst_path = " + string2);
        if (!object2.exists()) {
            DmcLog.e("ExternalIplImpl", "CombinedDeltaFileCopy():Delta file not exist!");
        } else {
            try {
                DmcLog.v("ExternalIplImpl", "CombinedDeltaFileCopy() copying..");
                object = new FileInputStream((String)object).getChannel();
                object2 = new FileOutputStream(string2).getChannel();
                object2.transferFrom((ReadableByteChannel)object, 0, object.size());
                object.close();
                object2.close();
            }
            catch (IOException var2_2) {
                var2_2.printStackTrace();
            }
            try {
                DmcLog.v("ExternalIplImpl", "CombinedDeltaFileCopy() Modify Preference File.");
                object = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
                object.putInt("key_DownloadDeltaStoragePosition", 0);
                object.putInt("key_DownloadDeltaPosition", 0);
                object.putString("key_DownloadDeltaStoragePath", "/fota");
                object.putString("key_DownloadDeltaStoragePathFull", string2);
                object.commit();
            }
            catch (Exception var1_5) {
                DmcLog.e("ExternalIplImpl", "CombinedDeltaFileCopy(): Exception " + var1_5);
                var1_5.printStackTrace();
            }
        }
        DmcLog.v("ExternalIplImpl", "CombinedDeltaFileCopy() end");
    }

    public void InstallPackage() {
        if (this.mService != null) {
            try {
                DmcLog.v("ExternalIplImpl", "rebootFotaInstall");
                this.mService.rebootFotaInstall(this.getDeltaFileName());
                return;
            }
            catch (RemoteException var1_1) {
                DmcLog.v("ExternalIplImpl", "Error=" + var1_1.getMessage());
                var1_1.printStackTrace();
                return;
            }
        }
        DmcLog.i("ExternalIplImpl", "mService is null. failed to call rebootFotaInstall. ");
    }

    /*
     * Enabled aggressive block sorting
     */
    public boolean checkDeltaCrc() {
        boolean bl;
        DmcLog.v("ExternalIplImpl", "checkDeltaCrc() started");
        int n = m_ask_fota_manager.jniFotaMgrClearCaches();
        m_ask_fota_manager.getClass();
        if (n != 0) {
            DmcLog.d("ExternalIplImpl", "checkDeltaCrc() : jniFotaMgrClearCaches NG");
            bl = false;
        } else {
            boolean bl2;
            bl = bl2 = this.checkAllDeltaCrc();
            if (bl2) {
                bl = bl2;
                if (this.getDownloadDeltaStoragePosition() != 0) {
                    bl = this.copyImageDelta();
                }
            }
        }
        DmcLog.v("ExternalIplImpl", "checkDeltaCrc() returning: result=" + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    public int delete_delta_files() {
        DmcLog.v("ExternalIplImpl", "delete_delta_files() started");
        int n = 0;
        File[] arrfile = new File("/fota").listFiles();
        for (int i = 0; i < arrfile.length; ++i) {
            File file = arrfile[i];
            file.getName();
            int n2 = n;
            if (!ExternalIplImpl.FOTA_Filedelete(file.getAbsolutePath())) {
                n2 = n + 1;
            }
            n = n2;
        }
        if (n == 0) {
            DmcLog.i("ExternalIplImpl", "delete_delta_files(): All files deleted!");
        } else {
            DmcLog.e("ExternalIplImpl", "delete_delta_files(): Some files were not deleted.. Undeletable_file = [" + n + "]");
        }
        DmcLog.v("ExternalIplImpl", "delete_delta_files() returning:" + n);
        return n;
    }

    public String getBuildVersion() {
        DmcLog.v("ExternalIplImpl", "getBuildVersion() started");
        String[] arrstring = Build.DISPLAY;
        DmcLog.d("ExternalIplImpl", "getBuildVersion() : Build.DISPLAY [ " + (String)arrstring + " ]");
        arrstring = arrstring.split(" ");
        DmcLog.v("ExternalIplImpl", "getBuildVersion() returning: verStr [ " + arrstring[0] + " ]");
        return arrstring[0];
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public long getDDinfo_DeltaSize() {
        DmcLog.v("ExternalIplImpl", "getDDinfo_DeltaSize() started");
        long l = 0;
        try {
            long l2;
            l = l2 = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).getLong("key_DownloadDeltaFileSize", 0);
        }
        catch (Exception var5_3) {
            DmcLog.e("ExternalIplImpl", "getDDinfo_DeltaSize() : Exception " + var5_3);
            var5_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "getDDinfo_DeltaSize() returning: size=" + l);
        return l;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String getDLProxyAddress() {
        String string2;
        DmcLog.v("ExternalIplImpl", "getDLProxyAddress() started");
        DmcLog.i("ExternalIplImpl", "JAVA IPL getDLProxyAddress");
        if (new File("/data/local/tmp/redbend/dlproxy").exists()) {
            DmcLog.d("ExternalIplImpl", "read DL Proxy address from simulation file");
            string2 = DeviceUtils.getFileText("/data/local/tmp/redbend/dlproxy");
        } else {
            string2 = "";
        }
        DmcLog.v("ExternalIplImpl", "getDLProxyAddress() returning:" + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public String getDMProxyAddress() {
        String string2;
        DmcLog.v("ExternalIplImpl", "getDMProxyAddress() started");
        DmcLog.i("ExternalIplImpl", "JAVA IPL getDMProxyAddress");
        if (new File("/data/local/tmp/redbend/dmproxy").exists()) {
            DmcLog.d("ExternalIplImpl", "read DM Proxy address from simulation file");
            string2 = DeviceUtils.getFileText("/data/local/tmp/redbend/dmproxy");
            DmcLog.d("ExternalIplImpl", " DM Proxy address is: " + string2);
        } else {
            string2 = "";
        }
        DmcLog.v("ExternalIplImpl", "getDMProxyAddress() returning:" + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public String getDeltaFileName() {
        DmcLog.v("ExternalIplImpl", "getDeltaFileName() started");
        String string2 = "";
        try {
            String string3;
            string2 = string3 = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).getString("key_DownloadDeltaStoragePathFull", "");
        }
        catch (Exception var2_3) {
            DmcLog.e("ExternalIplImpl", "getDeltaFileName(): Exception " + var2_3);
            var2_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "getDeltaFileName()returning: FileName=" + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String getDownloadDeltaStoragePath() {
        DmcLog.v("ExternalIplImpl", "getDownloadDeltaStoragePath() started");
        String string2 = "";
        switch (this.getDownloadDeltaStoragePosition()) {
            default: {
                DmcLog.d("ExternalIplImpl", "getDownloadDeltaStoragePath(): invalid position");
                break;
            }
            case 0: {
                string2 = this.getInternalMemoryPath();
                break;
            }
            case 1: {
                string2 = this.getExternalSdPath();
                break;
            }
            case 2: {
                string2 = this.getInternalSdPath();
                break;
            }
            case 3: {
                string2 = this.getSharedMemoryPath();
            }
        }
        DmcLog.v("ExternalIplImpl", "getDownloadDeltaStoragePath() returning: path=" + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getDownloadDeltaStoragePosition() {
        DmcLog.v("ExternalIplImpl", "getDownloadDeltaStoragePosition() started");
        int n = 99;
        try {
            int n2;
            n = n2 = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).getInt("key_DownloadDeltaPosition", 99);
        }
        catch (Exception var3_3) {
            DmcLog.e("ExternalIplImpl", "getDownloadDeltaStoragePosition() : Exception " + var3_3);
            var3_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "getDownloadDeltaStoragePosition() returning: position=" + n);
        return n;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String getExternalSdPath() {
        Object object;
        DmcLog.v("ExternalIplImpl", "getExternalSdPath() started");
        Object object2 = Environment.getExternalStorageDirectory().getAbsolutePath();
        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): ===========================================================================");
        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): path_primary = Environment.getExternalStorageDirectory().getAbsolutePath();");
        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): path_primary = " + (String)object2);
        if (Environment.isExternalStorageRemovable()) {
            DmcLog.d("ExternalIplImpl", "Environment.isExternalStorageRemovable() is true");
            object = object2;
            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = path_primary;");
            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = " + (String)object2);
        } else {
            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): Environment.isExternalStorageRemovable() is false");
            object = new File((String)object2);
            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): File mnt_sdcard = new File(path_primary);");
            if (object != null && object.exists() && object.isDirectory()) {
                DmcLog.d("ExternalIplImpl", "mnt_sdcard != null && mnt_sdcard.exists() && mnt_sdcard.isDirectory() is true");
                object = object2 = null;
                if (Build.VERSION.SDK_INT >= 17) {
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 is true");
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): This model is Jelly Bean 4.2 or later");
                    object = System.getenv("SECONDARY_STORAGE");
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): envValue = System.getenv(\"SECONDARY_STORAGE\");");
                    if (object == null || object.isEmpty()) {
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): envValue == null || envValue.isEmpty() is true");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): no external memory supported.");
                        object = object2;
                    } else {
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): envValue == null || envValue.isEmpty() is false");
                        object = object.split(":");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): String[] externalPaths = envValue.split(\":\");");
                        if (object.length <= 0 || object[0] == null || object[0].isEmpty()) {
                            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalPaths.length > 0 && externalPaths[0] != null && !externalPaths[0].isEmpty() is false");
                            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): no external memory supported.");
                            object = object2;
                        } else {
                            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalPaths.length > 0 && externalPaths[0] != null && !externalPaths[0].isEmpty() is true");
                            object = object[0];
                            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): external memory supported.");
                            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): path_secondary = externalPaths[0];");
                            DmcLog.d("ExternalIplImpl", "getExternalSdPath(): path_secondary = " + (String)object);
                        }
                    }
                }
                DmcLog.d("ExternalIplImpl", "getExternalSdPath(): At this moment, path_secondary is valid.");
                if (object != null) {
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): path_secondary is not null.");
                    object2 = new File((String)object);
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): mnt_sdcard_external_sd = new File(path_secondary);");
                    if (object2 != null) {
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): mnt_sdcard_external_sd is not null.");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): model type: both internal storage and microSD");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = path_secondary;");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = " + (String)object);
                    } else {
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): mnt_sdcard_external_sd is null.");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): model type: internal storage only ");
                        object = null;
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = null;");
                        DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = " + null);
                    }
                } else {
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): path_secondary is null.");
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): model type: internal storage only ");
                    object = null;
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = null;");
                    DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = " + null);
                }
            } else {
                DmcLog.d("ExternalIplImpl", "getExternalSdPath(): mnt_sdcard != null && mnt_sdcard.exists() && mnt_sdcard.isDirectory() is false");
                DmcLog.d("ExternalIplImpl", "getExternalSdPath(): \"/storage/sdcard0\" not exist.");
                DmcLog.d("ExternalIplImpl", "getExternalSdPath(): model type: inconsistent with Google definitions.");
                object = null;
                DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = null;");
                DmcLog.d("ExternalIplImpl", "getExternalSdPath(): externalSdPath = " + null);
            }
        }
        DmcLog.v("ExternalIplImpl", "getExternalSdPath()returning: externalSdPath=" + (String)object);
        return object;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String getExternalSdState(String string2) {
        DmcLog.v("ExternalIplImpl", "getExternalSdState() started: externalSdPath=" + string2);
        StorageManager storageManager = (StorageManager)this.mContext.getSystemService("storage");
        string2 = string2 != null ? storageManager.getVolumeState(string2) : "removed";
        DmcLog.v("ExternalIplImpl", "getExternalSdState() returning: externalSdState=" + string2);
        return string2;
    }

    @Override
    public String getFirmwareVersion() {
        DmcLog.v("ExternalIplImpl", "getFirmwareVersion() started");
        String string2 = this.getBuildVersion();
        DmcLog.v("ExternalIplImpl", "getFirmwareVersion() returning: fwv = [" + string2 + "]");
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public String getImageDeltaStoragePath() {
        DmcLog.v("ExternalIplImpl", "getImageDeltaStoragePath() started");
        String string2 = "";
        String string3 = this.getDeltaFileName();
        int n = string3.lastIndexOf("/");
        if (n < 0) {
            DmcLog.d("ExternalIplImpl", "getImageDeltaStoragePath(): Search fails");
        } else {
            string2 = string3.substring(n, string3.length());
            string2 = this.getInternalMemoryPath() + string2;
        }
        DmcLog.v("ExternalIplImpl", "getImageDeltaStoragePath() returning: path=" + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public long getInternalMemoryFreeSize() {
        DmcLog.v("ExternalIplImpl", "getInternalMemoryFreeSize() started");
        long l = 0;
        File file = Environment.getDataDirectory();
        if (file != null) {
            file = new StatFs(file.getPath());
            l = (long)file.getBlockSize() * (long)file.getAvailableBlocks();
            DmcLog.d("ExternalIplImpl", "getInternalMemoryFreeSize(): " + String.format("free  = %d bytes", l) + String.format(" ( %d MB )", l / 1024 / 1024));
        } else {
            DmcLog.e("ExternalIplImpl", "getInternalMemoryFreeSize(): dataDirectoryPath is null.");
        }
        DmcLog.d("ExternalIplImpl", "getInternalMemoryFreeSize() returning : freeSize = [" + l + "]");
        return l;
    }

    public String getInternalMemoryPath() {
        DmcLog.v("ExternalIplImpl", "getInternalMemoryPath() started");
        DmcLog.v("ExternalIplImpl", "getInternalMemoryPath()returning: path=" + "/fota");
        return "/fota";
    }

    /*
     * Enabled aggressive block sorting
     */
    public String getInternalSdPath() {
        DmcLog.v("ExternalIplImpl", "getInternalSdPath() started");
        Object object = ((StorageManager)this.mContext.getSystemService("storage")).getVolumePaths();
        if (object.length > 1) {
            DmcLog.d("ExternalIplImpl", "getInternalSdPath(): Internal storage path");
            object = object[0];
        } else {
            DmcLog.d("ExternalIplImpl", "getInternalSdPath(): Internal storage Not Use");
            object = null;
        }
        DmcLog.v("ExternalIplImpl", "getInternalSdPath() returning: path=" + (String)object);
        return object;
    }

    /*
     * Enabled aggressive block sorting
     */
    public long getLimitedFreeSpaceSize() {
        DmcLog.v("ExternalIplImpl", "getLimitedFreeSpaceSize() started");
        long l = 0;
        String string2 = Environment.getDataDirectory().getPath();
        if (string2 == null) {
            DmcLog.d("ExternalIplImpl", "getLimitedFreeSpaceSize() : getDataDirectory() NG");
        } else {
            DmcLog.d("ExternalIplImpl", "getLimitedFreeSpaceSize() : getDataDirectory() get=" + string2);
            string2 = new StatFs(string2);
            long l2 = string2.getBlockSize();
            long l3 = string2.getBlockCount();
            long l4 = string2.getAvailableBlocks();
            long l5 = l2 * l4;
            DmcLog.d("ExternalIplImpl", "getLimitedFreeSpaceSize() : blockSize       =" + l2);
            DmcLog.d("ExternalIplImpl", "getLimitedFreeSpaceSize() : blockCount     =" + l3);
            DmcLog.d("ExternalIplImpl", "getLimitedFreeSpaceSize() : availableBlocks =" + l4);
            DmcLog.d("ExternalIplImpl", "getLimitedFreeSpaceSize() : freeSize1       =" + l5);
            if (l5 > 104857600) {
                l = l5 - 104857600;
            }
        }
        DmcLog.v("ExternalIplImpl", "getLimitedFreeSpaceSize() returning: limitedSpaceSize=" + l + "(" + l / 1024 / 1024 + " MB)");
        return l;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public long getPackageStorageSize(String var1_1) {
        block10 : {
            DmcLog.v("ExternalIplImpl", "getPackageStorageSize() started. path=[" + var1_1 + "]");
            if (new File("/data/local/tmp/redbend/nostorage").exists()) {
                // empty if block
            }
            var2_4 = var6_3 = 0;
            if (this.getStorageState() == 0) {
                var4_5 = var6_3;
                if (Environment.isExternalStorageEmulated()) {
                    var4_5 = var6_3;
                    if (this.getDownloadDeltaStoragePosition() == 3) {
                        var4_5 = var6_3;
                        var2_4 = this.getLimitedFreeSpaceSize();
                        break block10;
                    }
                }
                var4_5 = var6_3;
                var1_1 = new StatFs(var1_1);
                var4_5 = var6_3;
                var2_4 = var1_1.getBlockSize();
                var4_5 = var6_3;
                var8_6 = var1_1.getBlockCount();
                var4_5 = var6_3;
                var6_3 = var1_1.getAvailableBlocks();
                var8_6 = var2_4 * var8_6;
                var4_5 = var6_3 = var2_4 * var6_3;
                try {
                    DmcLog.d("ExternalIplImpl", "getPackageStorageSize() : " + String.format("Storage total = %d bytes", new Object[]{var8_6}) + String.format(" ( %d MB )", new Object[]{var8_6 / 1024 / 1024}));
                    var4_5 = var6_3;
                    DmcLog.d("ExternalIplImpl", "getPackageStorageSize() : " + String.format("Storage free  = %d bytes", new Object[]{var6_3}) + String.format(" ( %d MB )", new Object[]{var6_3 / 1024 / 1024}));
                    var2_4 = var6_3;
                    var4_5 = var6_3;
                    ** if (this.getDownloadDeltaStoragePosition() != 3) goto lbl-1000
                }
                catch (Exception var1_2) {
                    DmcLog.e("ExternalIplImpl", "getPackageStorageSize() : Exception=" + var1_2);
                    var2_4 = var4_5;
                }
lbl-1000: // 1 sources:
                {
                    var4_5 = var2_4 = var6_3 - 104857600;
                    DmcLog.d("ExternalIplImpl", "getPackageStorageSize() : " + String.format("Adjusted !! Storage free  = %d bytes", new Object[]{var2_4}) + String.format(" ( %d MB )", new Object[]{var2_4 / 1024 / 1024}));
                }
lbl-1000: // 2 sources:
                {
                }
            }
        }
        DmcLog.i("ExternalIplImpl", "JAVA IPL getPackageStorageSize: size is " + var2_4);
        DmcLog.v("ExternalIplImpl", "getPackageStorageSize() returning:" + var2_4);
        return var2_4;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int getReqBatteryLevel() {
        DmcLog.v("ExternalIplImpl", "getReqBatteryLevel() started");
        int n = -1;
        try {
            int n2;
            n = n2 = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).getInt("key_ReqBatteryLevel", -1);
        }
        catch (Exception var3_3) {
            DmcLog.e("ExternalIplImpl", "getReqBatteryLevel() : Exception " + var3_3);
            var3_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "getReqBatteryLevel() returning: required battery level = [" + n + "]");
        return n;
    }

    public String getSharedMemoryPath() {
        DmcLog.v("ExternalIplImpl", "getSharedMemoryPath() started");
        DmcLog.v("ExternalIplImpl", "getSharedMemoryPath() returning: path=" + "/data/fota_sharp");
        return "/data/fota_sharp";
    }

    /*
     * Enabled aggressive block sorting
     */
    public int getStorageState() {
        String string2;
        DmcLog.v("ExternalIplImpl", "getStorageState() started");
        int n = 5;
        switch (this.getDownloadDeltaStoragePosition()) {
            default: {
                DmcLog.d("ExternalIplImpl", "getStorageState(): invalid position");
                string2 = "removed";
                break;
            }
            case 0: {
                string2 = "mounted";
                break;
            }
            case 1: {
                string2 = this.getExternalSdState(this.getExternalSdPath());
                break;
            }
            case 2: {
                string2 = Environment.getExternalStorageState();
                break;
            }
            case 3: {
                string2 = "mounted";
            }
        }
        DmcLog.d("ExternalIplImpl", "getStorageState(): StorageState = " + string2);
        if (string2.equals("bad_removal")) {
            n = 5;
        } else if (string2.equals("checking")) {
            n = 3;
        } else if (string2.equals("mounted")) {
            n = 0;
        } else if (string2.equals("mounted_ro")) {
            n = 6;
        } else if (string2.equals("nofs")) {
            n = 6;
        } else if (string2.equals("removed")) {
            n = 5;
        } else if (string2.equals("shared")) {
            n = 4;
        } else if (string2.equals("unmountable")) {
            n = 4;
        } else if (string2.equals("unmounted")) {
            n = 4;
        } else {
            DmcLog.d("ExternalIplImpl", "getStorageState(): others");
        }
        DmcLog.v("ExternalIplImpl", "getStorageState() returning: ret=" + n);
        return n;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public String getUpdatePackagePathPreDownload(long l) {
        DmcLog.v("ExternalIplImpl", "getUpdatePackagePathPreDownload() started. size = [" + l + "]");
        DmcLog.i("ExternalIplImpl", "JAVA IPL getUpdatePackagePathPreDownload");
        String string2 = "";
        if (new File("/data/local/tmp/redbend/setdppath").exists()) {
            DmcLog.d("ExternalIplImpl", "read DP path from simulation file");
            string2 = DeviceUtils.getFileText("/data/local/tmp/redbend/setdppath");
        } else {
            this.setDDinfo_DeltaSize(l);
            int n = this.setDownloadDeltaStoragePosition();
            switch (n) {
                default: {
                    DmcLog.d("ExternalIplImpl", "getUpdatePackagePathPreDownload(): invalid position");
                    break;
                }
                case 0: {
                    DmcLog.v("ExternalIplImpl", "getUpdatePackagePathPreDownload(): posision=INTERNAL_MEMORY[" + n + "]");
                    string2 = this.getInternalMemoryPath() + "/";
                    break;
                }
                case 1: {
                    DmcLog.v("ExternalIplImpl", "getUpdatePackagePathPreDownload(): posision=EXTERNAL_SD[" + n + "]");
                    string2 = this.getExternalSdPath() + "/";
                    break;
                }
                case 2: {
                    DmcLog.v("ExternalIplImpl", "getUpdatePackagePathPreDownload(): posision=INTERNAL_SD[" + n + "]");
                    string2 = this.getInternalSdPath() + "/";
                    break;
                }
                case 3: {
                    DmcLog.v("ExternalIplImpl", "getUpdatePackagePathPreDownload(): posision=SHARED_MEMORY[" + n + "]");
                    string2 = this.getSharedMemoryPath() + "/";
                }
            }
            try {
                SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
                editor.putLong("key_DownloadDeltaFileSize", l);
                editor.putInt("key_DownloadDeltaStoragePosition", n);
                editor.putString("key_DownloadDeltaStoragePath", string2);
                editor.commit();
            }
            catch (Exception var5_5) {
                DmcLog.e("ExternalIplImpl", "getUpdatePackagePathPreDownload(): Exception " + var5_5);
                var5_5.printStackTrace();
            }
        }
        DmcLog.v("ExternalIplImpl", "getUpdatePackagePathPreDownload() returning:" + string2);
        return string2;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int getUpdateResult() {
        DmcLog.v("ExternalIplImpl", "getUpdateResult() started");
        try {
            DeviceUtils.getIntFromFile(this.mContext.getFilesDir().toString() + "/update");
            this.mContext.deleteFile("update");
        }
        catch (Exception var2_1) {
            DmcLog.e("ExternalIplImpl", "GetUpdateResult: " + var2_1.toString());
        }
        DmcLog.d("ExternalIplImpl", "getUpdateResult(): onReceive");
        int n = m_ask_fota_manager.jniFotaMgrGetSoftwareUpdateResult();
        m_ask_fota_manager.getClass();
        if (n == -1) {
            DmcLog.d("ExternalIplImpl", "getUpdateResult(): FOTA_JNI_UPDATE_RESULT_OFF");
            n = -1;
        } else {
            m_ask_fota_manager.getClass();
            if (n == 200) {
                DmcLog.d("ExternalIplImpl", "getUpdateResult(): FOTA_JNI_UPDATE_RESULT_SUCCESS");
                n = 0;
            } else {
                m_ask_fota_manager.getClass();
                if (n == 402) {
                    DmcLog.d("ExternalIplImpl", "getUpdateResult(): FOTA_JNI_UPDATE_RESULT_DLERROR");
                    n = 402;
                } else {
                    m_ask_fota_manager.getClass();
                    if (n == 403) {
                        DmcLog.d("ExternalIplImpl", "getUpdateResult(): FOTA_JNI_UPDATE_RESULT_SDREADERROR");
                        n = 403;
                    } else {
                        DmcLog.d("ExternalIplImpl", "getUpdateResult(): FOTA_JNI_UPDATE_RESULT_ERROR");
                        n = 401;
                    }
                }
            }
        }
        try {
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
            editor.putInt("key_softwareupdate_result", n);
            editor.commit();
        }
        catch (Exception var2_3) {
            DmcLog.e("ExternalIplImpl", "getUpdateResult(): Exception " + var2_3);
            var2_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "getUpdateResult() returning:" + n);
        return n;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public boolean initDeltaInfos() {
        DmcLog.v("ExternalIplImpl", "initDeltaInfos() started");
        boolean bl = false;
        try {
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
            editor.putLong("key_DownloadDeltaFileSize", 0);
            editor.putInt("key_DownloadDeltaStoragePosition", 0);
            editor.putString("key_DownloadDeltaStoragePath", "");
            editor.putString("key_DownloadDeltaStoragePathFull", "");
            editor.putString("key_softwareupdate_DeltaFileName", "");
            editor.putInt("key_DownloadDeltaPosition", 99);
            editor.putInt("key_NeedCopyDeltaFile", 99);
            editor.putInt("key_ReqBatteryLevel", -1);
            editor.commit();
            bl = true;
        }
        catch (Exception var2_3) {
            DmcLog.e("ExternalIplImpl", "initDeltaInfos(): Exception " + var2_3);
            var2_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "initDeltaInfos(): End ret=" + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean isBatterySufficient() {
        DmcLog.v("ExternalIplImpl", "isBatterySufficient() started");
        if (new File("/data/local/tmp/redbend/nobattery").exists()) {
            DmcLog.v("ExternalIplImpl", "isBatterySufficient returning:false");
            return false;
        }
        boolean bl = false;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        intentFilter = this.mContext.registerReceiver(null, intentFilter);
        DmcLog.d("ExternalIplImpl", "isBatterySufficient(): intent = [" + (Object)intentFilter + "]");
        int n = intentFilter.getIntExtra("level", 0);
        DmcLog.d("ExternalIplImpl", "isBatterySufficient(): level = [" + n + "]");
        int n2 = this.getReqBatteryLevel();
        if (n2 <= 40 || n2 > 100) {
            DmcLog.v("ExternalIplImpl", "isBatterySufficient():Compared with [BATTERY_LIMIT].(req_battery_level:" + n2 + ")");
            if (n > 40) {
                bl = true;
            }
        } else {
            DmcLog.v("ExternalIplImpl", "isBatterySufficient():Compared with [req_battery_level(" + n2 + ")].");
            if (n >= n2) {
                bl = true;
            }
        }
        DmcLog.v("ExternalIplImpl", "isBatterySufficient() returning:" + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int isCopyNeeded() {
        DmcLog.v("ExternalIplImpl", "isCopyNeeded() started");
        int n = 99;
        try {
            int n2;
            n = n2 = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).getInt("key_NeedCopyDeltaFile", 99);
        }
        catch (Exception var3_3) {
            DmcLog.e("ExternalIplImpl", "isCopyNeeded() : Exception " + var3_3);
            var3_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "isCopyNeeded() returning: position=" + n);
        return n;
    }

    @Override
    public boolean isExternalStorageUsed() {
        DmcLog.i("ExternalIplImpl", "JAVA IPL isExternalStorageUsed");
        return false;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public boolean isPostDownloadRequired(String string2) {
        DmcLog.i("ExternalIplImpl", "Package path is " + string2);
        DmcLog.v("ExternalIplImpl", "isPostDownloadRequired() started. Package path is [" + string2 + "]");
        if (new File("/data/local/tmp/redbend/noauthrequired").exists()) {
            DmcLog.i("ExternalIplImpl", "QA Simulation - noauthrequired file exists");
        }
        String string3 = new File(string2).getName();
        DmcLog.i("ExternalIplImpl", "Package filename is " + string3);
        try {
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
            editor.putString("key_DownloadDeltaStoragePathFull", string2);
            editor.putString("key_softwareupdate_DeltaFileName", string3);
            editor.commit();
        }
        catch (Exception var1_2) {
            DmcLog.e("ExternalIplImpl", "isPostDownloadRequired(): Exception " + var1_2);
            var1_2.printStackTrace();
        }
        DmcLog.i("ExternalIplImpl", "JAVA IPL isPostDownloadRequired result is " + true);
        DmcLog.v("ExternalIplImpl", "isPostDownloadRequired() returning:" + true);
        return true;
    }

    @Override
    public boolean isProxyAddressNeeded() {
        DmcLog.v("ExternalIplImpl", "isProxyAddressNeeded() started");
        boolean bl = false;
        if (new File("/data/local/tmp/redbend/isproxyneeded").exists()) {
            bl = true;
        }
        DmcLog.i("ExternalIplImpl", "JAVA IPL  isProxyAddressNeeded : " + bl);
        DmcLog.v("ExternalIplImpl", "isProxyAddressNeeded() returning:" + bl);
        return bl;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int onPostDownload() {
        DmcLog.v("ExternalIplImpl", "onPostDownload() started");
        int n = 1;
        if (new File("/data/local/tmp/redbend/novalidpkg").exists()) {
            DmcLog.i("ExternalIplImpl", "QA Simulation - novalidpkg file exists");
            n = 0;
        }
        if (this.isCopyNeeded() == 1) {
            DmcLog.v("ExternalIplImpl", "onPostDownload() : It is necessary to copy the DeltFile.");
            this.CombinedDeltaFileCopy();
        } else if (this.isCopyNeeded() == 99) {
            DmcLog.v("ExternalIplImpl", "onPostDownload() returning:DELTA_COPY_INVALID");
            return 0;
        }
        byte[] arrby = this.readFileHeader(this.getDeltaFileName());
        int n2 = m_ask_fota_manager.jniFotaMgrVerifyRewriteData(arrby);
        m_ask_fota_manager.getClass();
        if (n2 != 0) {
            DmcLog.e("ExternalIplImpl", "onPostDownload() : jniFotaMgrVerifyRewriteData ret Error");
            n = 0;
        } else {
            n2 = m_ask_fota_manager.jniFotaMgrGetParamaterInfoEx(arrby, 4);
            DmcLog.v("ExternalIplImpl", "onPostDownload():GET req_battery_level = [" + n2 + "](before &MASK)");
            m_ask_fota_manager.getClass();
            if (n2 == -1) {
                DmcLog.e("ExternalIplImpl", "onPostDownload() : jniFotaMgrGetParamaterInfoEx ret Error");
                n = 0;
            } else {
                DmcLog.v("ExternalIplImpl", "onPostDownload():GET req_battery_level = [" + (n2 &= 255) + "]");
                this.setReqBatteryLevel(n2);
            }
        }
        if (new File("/data/local/tmp/redbend/takelongtimetocopypkg").exists()) {
            try {
                Thread.sleep(5000);
            }
            catch (InterruptedException var3_3) {
                var3_3.printStackTrace();
            }
        }
        DmcLog.i("ExternalIplImpl", "JAVA IPL onPostDownload result is " + n);
        DmcLog.v("ExternalIplImpl", "onPostDownload() returning:" + n);
        return n;
    }

    @Override
    public int postUpdate() {
        DmcLog.i("ExternalIplImpl", "postUpdate called");
        DmcLog.d("ExternalIplImpl", "postUpdate() : Calling jniFotaMgrUpdateEnd");
        int n = m_ask_fota_manager.jniFotaMgrEndSoftwareUpdate();
        DmcLog.d("ExternalIplImpl", "postUpdate() : Return jniFotaMgrUpdateEnd=" + n);
        m_ask_fota_manager.getClass();
        if (n == 0) {
            this.delete_delta_files();
            this.initDeltaInfos();
        }
        DmcLog.v("ExternalIplImpl", "postUpdate() returning:0");
        return 0;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    public byte[] readDeltaFile(String var1_1, long var2_5, int var4_6) {
        block6 : {
            // MONITORENTER : this
            DmcLog.v("ExternalIplImpl", "readDeltaFile() started: fileName=" + (String)var1_1 + ", offset=" + (long)var2_4 + ", size=" + var4_5);
            try {
                var1_1 = new RandomAccessFile((String)var1_1, "r");
                if (var2_4 <= 0) ** GOTO lbl9
            }
            catch (Exception var1_2) {}
            try {
                var1_1.seek((long)var2_4);
lbl9: // 2 sources:
                DmcLog.d("ExternalIplImpl", "readDeltaFile() : File position=" + var1_1.getFilePointer());
                var4_5 = var1_1.read(this.deltaReadBuff, 0, var4_5);
                DmcLog.d("ExternalIplImpl", "readDeltaFile() : Read Size=" + var4_5);
                var1_1.close();
                break block6;
            }
            catch (Exception var1_3) {}
            {
                var1_1.printStackTrace();
                DmcLog.e("ExternalIplImpl", "readDeltaFile() : Exception=" + var1_1);
                this.deltaReadBuff = null;
            }
        }
        DmcLog.v("ExternalIplImpl", "readDeltaFile() returning: deltaReadBuff=" + this.deltaReadBuff);
        var1_1 = this.deltaReadBuff;
        // MONITOREXIT : this
        return var1_1;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public byte[] readFileHeader(String var1_1) {
        DmcLog.v("ExternalIplImpl", "readFileHeader()started: fileName=" + (String)var1_1);
        var5_5 = new byte[2232];
        var3_6 = null;
        var4_11 = null;
        var2_12 = var3_6;
        var6_14 = new FileInputStream((String)var1_1);
        var2_12 = var3_6;
        var1_1 = new BufferedInputStream(var6_14);
        var1_1.read(var5_5, 0, var5_5.length);
        var1_1.close();
        var6_14.close();
        ** if (var1_1 == null) goto lbl21
lbl-1000: // 1 sources:
        {
            try {
                var1_1.close();
            }
            catch (Exception var1_2) {
                var1_2.printStackTrace();
                DmcLog.e("ExternalIplImpl", "readFileHeader(): readFileHeader Exception=" + var1_2);
            }
        }
lbl21: // 2 sources:
        ** GOTO lbl51
        catch (Exception var3_7) {
            block14 : {
                var1_1 = var4_11;
                ** GOTO lbl30
                catch (Throwable var3_9) {
                    var2_12 = var1_1;
                    var1_1 = var3_9;
                    ** GOTO lbl-1000
                }
                catch (Exception var3_10) {}
lbl30: // 2 sources:
                var2_12 = var1_1;
                try {
                    var3_8.printStackTrace();
                    var2_12 = var1_1;
                    DmcLog.e("ExternalIplImpl", "readFileHeader(): readFileHeader Exception=" + var3_8);
                    if (var1_1 == null) break block14;
                }
                catch (Throwable var1_4) lbl-1000: // 2 sources:
                {
                    if (var2_12 == null) throw var1_1;
                    try {
                        var2_12.close();
                    }
                    catch (Exception var2_13) {
                        var2_13.printStackTrace();
                        DmcLog.e("ExternalIplImpl", "readFileHeader(): readFileHeader Exception=" + var2_13);
                        throw var1_1;
                    }
                    throw var1_1;
                }
                try {
                    var1_1.close();
                }
                catch (Exception var1_3) {
                    var1_3.printStackTrace();
                    DmcLog.e("ExternalIplImpl", "readFileHeader(): readFileHeader Exception=" + var1_3);
                }
            }
            DmcLog.v("ExternalIplImpl", "readFileHeader() returning: buff.length=" + var5_5.length);
            return var5_5;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void setDDinfo_DeltaSize(long l) {
        DmcLog.v("ExternalIplImpl", "setDDinfo_DeltaSize() started: size=" + l);
        try {
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
            editor.putLong("key_DownloadDeltaFileSize", l);
            editor.commit();
        }
        catch (Exception var3_3) {
            DmcLog.e("ExternalIplImpl", "setDDinfo_DeltaSize() : Exception " + var3_3);
            var3_3.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "setDDinfo_DeltaSize() return");
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public int setDownloadDeltaStoragePosition() {
        int n;
        int n2;
        DmcLog.v("ExternalIplImpl", "setDownloadDeltaStoragePosition() started");
        long l = this.getDDinfo_DeltaSize();
        if (l <= 0x5000000) {
            n2 = 0;
            n = 0;
        } else {
            n = new File("/data/fota_sharp").exists() ? 3 : (Environment.isExternalStorageRemovable() ? 1 : 2);
            n2 = l > 0x5000000 && l <= 104857600 ? 1 : 0;
        }
        try {
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
            editor.putInt("key_DownloadDeltaPosition", n);
            editor.putInt("key_NeedCopyDeltaFile", n2);
            editor.commit();
        }
        catch (Exception var5_5) {
            DmcLog.e("ExternalIplImpl", "setDownloadDeltaStoragePosition() : Exception " + var5_5);
            var5_5.printStackTrace();
        }
        DmcLog.v("ExternalIplImpl", "setDownloadDeltaStoragePosition() [NeedCopy] = " + n2);
        DmcLog.v("ExternalIplImpl", "setDownloadDeltaStoragePosition() returning: position=" + n);
        return n;
    }

    public void setReqBatteryLevel(int n) {
        DmcLog.v("ExternalIplImpl", "setReqBatteryLevel() started. req_battery_level = [" + n + "]");
        try {
            SharedPreferences.Editor editor = this.mContext.getSharedPreferences("FOTA_ipl_pref", 0).edit();
            editor.putInt("key_ReqBatteryLevel", n);
            editor.commit();
            return;
        }
        catch (Exception var2_3) {
            DmcLog.e("ExternalIplImpl", "setReqBatteryLevel() : Exception " + var2_3);
            var2_3.printStackTrace();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int startUpdate() {
        Object object;
        DmcLog.v("ExternalIplImpl", "startUpdate() started");
        if (new File("/data/local/tmp/redbend/fail_start_update").exists()) {
            DmcLog.e("ExternalIplImpl", "/data/local/tmp/redbend/fail_start_update file found, simulating update failure");
            DmcLog.v("ExternalIplImpl", "startUpdate() simulating returning:UPDATE_FAILED_GENERAL");
            return 1;
        }
        if (new File("/data/local/tmp/redbend/invalid_package").exists()) {
            DmcLog.e("ExternalIplImpl", "Update failed due to invalid package");
            DmcLog.v("ExternalIplImpl", "startUpdate() returning:UPDATE_INVALID_PACKAGE");
            return 2;
        }
        if (new File("/data/local/tmp/redbend/disk_insufficient").exists()) {
            DmcLog.e("ExternalIplImpl", "/data/local/tmp/redbend/disk_insufficient file found, simulating disk insufficient");
            DmcLog.v("ExternalIplImpl", "startUpdate() returning:UPDATE_DISK_INSUFFICIENT");
            return 3;
        }
        int n = 0;
        try {
            int n2;
            n = n2 = DeviceUtils.getIntFromFile("/data/local/tmp/redbend/updateResult");
        }
        catch (Exception var8_3) {
            DmcLog.d("ExternalIplImpl", "exception " + var8_3.toString());
        }
        DmcLog.i("ExternalIplImpl", "Writing " + n + " as update result");
        try {
            object = this.mContext.openFileOutput("update", 0);
            object.write(String.valueOf(n).getBytes());
            object.close();
        }
        catch (Exception var8_5) {
            DmcLog.e("ExternalIplImpl", "Could not write update result to file" + var8_5.toString());
        }
        object = this.getDeltaFileName();
        DmcLog.v("ExternalIplImpl", "startUpdate():Delta file :fileName = " + (String)object);
        if (!new File((String)object).exists()) {
            DmcLog.e("ExternalIplImpl", "startUpdate():Delta file not exist!");
            return 2;
        }
        DmcLog.d("ExternalIplImpl", "startUpdate():Call checkDeltaCrc call.");
        boolean bl = this.checkDeltaCrc();
        DmcLog.d("ExternalIplImpl", "startUpdate(): checkDeltaCrc call end. chkret = [" + bl + "]");
        if (!bl) {
            DmcLog.e("ExternalIplImpl", "startUpdate(): checkDeltaCrc returning:UPDATE_INVALID_PACKAGE");
            return 2;
        }
        KeyguardManager.KeyguardLock keyguardLock = this.readFileHeader((String)object);
        DmcLog.d("ExternalIplImpl", "startUpdate(): deltaInfoData[] = [" + (Object)keyguardLock + "]");
        n = m_ask_fota_manager.jniFotaMgrGetParamaterInfo((byte[])keyguardLock);
        DmcLog.d("ExternalIplImpl", "startUpdate(): jniFotaMgrGetParamaterInfo - retKind = [" + n + "]");
        m_ask_fota_manager.getClass();
        if (n != 0) {
            m_ask_fota_manager.getClass();
            if (n <= 0) {
                DmcLog.e("ExternalIplImpl", "startUpdate(): check Major update, jniFotaMgrGetUpdateKind ret Delta Error.");
                DmcLog.e("ExternalIplImpl", "startUpdate() returning:UPDATE_INVALID_PACKAGE");
                return 2;
            }
            DmcLog.d("ExternalIplImpl", "startUpdate(): GetUpdateKind is UPDATE_MJUPD. Check internal memory free size.");
            long l = this.getInternalMemoryFreeSize();
            DmcLog.d("ExternalIplImpl", "startUpdate(): internal memory free size = [" + l + "]");
            long l2 = (long)n * 1024 * 1024;
            DmcLog.d("ExternalIplImpl", "startUpdate(): retKind size = [" + l2 + "]");
            if (l < l2) {
                DmcLog.e("ExternalIplImpl", "startUpdate(): check Major update, internal memory free size error.");
                DmcLog.e("ExternalIplImpl", "startUpdate(): need free size=" + n * 1024 * 1024);
                DmcLog.e("ExternalIplImpl", "startUpdate() returning:UPDATE_DISK_INSUFFICIENT");
                return 3;
            }
            DmcLog.d("ExternalIplImpl", "startUpdate(): check Major update, internal memory free size OK.");
        }
        if (!this.setDeltaName((String)object)) {
            DmcLog.d("ExternalIplImpl", "startUpdate(): checkUpdateStart End setDeltaName NG");
            DmcLog.e("ExternalIplImpl", "startUpdate() returning:UPDATE_INVALID_PACKAGE");
            return 2;
        }
        DmcLog.d("ExternalIplImpl", "startUpdate(): jniFotaMgrUpdateStart Call");
        n = m_ask_fota_manager.jniFotaMgrStartSoftwareUpdate();
        m_ask_fota_manager.getClass();
        if (n != 0) {
            DmcLog.d("ExternalIplImpl", "startUpdate(): jniFotaMgrUpdateStart NG! ");
            DmcLog.e("ExternalIplImpl", "startUpdate() returning:UPDATE_FAILED_GENERAL");
            return 1;
        }
        DmcLog.d("ExternalIplImpl", "startUpdate(): jniFotaMgrUpdateStart OK. Update START!");
        object = ((PowerManager)this.mContext.getSystemService("power")).newWakeLock(805306394, "unlock screen");
        DmcLog.d("ExternalIplImpl", "startUpdate(): wakeLock.acquire() call");
        object.acquire();
        object = (KeyguardManager)this.mContext.getSystemService("keyguard");
        DmcLog.d("ExternalIplImpl", "startUpdate(): newKeyguardLock");
        keyguardLock = object.newKeyguardLock("FOTA DEBUG unlock screen");
        bl = object.inKeyguardRestrictedInputMode();
        DmcLog.d("ExternalIplImpl", "startUpdate(): isKeyguard=" + bl);
        if (bl) {
            DmcLog.d("ExternalIplImpl", "startUpdate(): keyguardLock.disableKeyguard() call");
            keyguardLock.disableKeyguard();
        }
        DmcLog.v("ExternalIplImpl", "bindService");
        object = new Intent(IFotaRecoveryService.class.getName());
        object.setPackage("jp.co.sharp.android.fotarecovery");
        this.mContext.bindService((Intent)object, this.mServiceConnection, 1);
        try {
            Thread.sleep(600000);
        }
        catch (Exception var8_6) {
            DmcLog.v("ExternalIplImpl", "sleep exception" + var8_6.toString());
        }
        DmcLog.d("ExternalIplImpl", "startUpdate(): End(Check OK!)");
        DmcLog.i("ExternalIplImpl", "<<<<<<<<< UPDATE HANDOFF COMPLETE. PLEASE REBOOT NOW >>>>>>>>>>>");
        DmcLog.v("ExternalIplImpl", "startUpdate() returning:UPDATE_SUCCEESS");
        return 3;
    }

}

