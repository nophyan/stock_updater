/*
 * Decompiled with CFR 0_115.
 */
package com.redbend.android;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RbFileUtils {
    /*
     * Unable to fully structure code
     * Enabled aggressive exception aggregation
     */
    public static boolean copyFile(File var0, File var1_5) {
        block22 : {
            var2_9 = null;
            var3_12 = null;
            var0 = new BufferedInputStream(new FileInputStream((File)var0));
            var1_5 = new BufferedOutputStream(new FileOutputStream((File)var1_5, false));
            RbFileUtils.copyFile((InputStream)var0, (OutputStream)var1_5);
            if (var0 != null) break block22;
            while (var1_5 == null) lbl-1000: // 2 sources:
            {
                do {
                    return true;
                    break;
                } while (true);
            }
            ** GOTO lbl18
        }
        var0.close();
lbl18: // 1 sources:
        var1_5.close();
        ** continue;
        catch (IOException var0_1) {
            var0 = null;
            var1_5 = var3_12;
lbl23: // 3 sources:
            do {
                if (var1_5 != null) ** GOTO lbl29
                while (var0 == null) {
                    return false;
                }
                ** GOTO lbl31
lbl29: // 2 sources:
                var1_5.close();
lbl31: // 1 sources:
                var0.close();
                return false;
                break;
            } while (true);
        }
        catch (Throwable var1_6) {
            var0 = null;
lbl35: // 3 sources:
            do {
                if (var0 != null) ** GOTO lbl42
lbl37: // 2 sources:
                while (var2_9 == null) lbl-1000: // 2 sources:
                {
                    do {
                        throw var1_5;
                        break;
                    } while (true);
                }
                ** GOTO lbl44
lbl42: // 2 sources:
                var0.close();
                ** GOTO lbl37
lbl44: // 1 sources:
                var2_9.close();
                ** continue;
                break;
            } while (true);
        }
        catch (Throwable var1_7) {
            ** GOTO lbl35
        }
        catch (Throwable var3_13) {
            var2_9 = var1_5;
            var1_5 = var3_13;
            ** continue;
        }
        catch (IOException var1_8) {
            var2_9 = null;
            var1_5 = var0;
            var0 = var2_9;
            ** GOTO lbl23
        }
        catch (IOException var2_10) {
            var2_11 = var0;
            var0 = var1_5;
            var1_5 = var2_11;
            ** continue;
        }
        catch (IOException var0_2) {
            return false;
        }
        {
            catch (IOException var0_3) {
                return false;
            }
        }
        {
            catch (IOException var0_4) {
                return false;
            }
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream == null) {
            return false;
        }
        byte[] arrby = new byte[1024];
        inputStream.read(arrby);
        do {
            outputStream.write(arrby);
        } while (inputStream.read(arrby) != -1);
        return true;
    }
}

