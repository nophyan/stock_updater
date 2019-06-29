/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.util.Log
 */
package com.redbend.android;

import android.util.Log;
import com.redbend.android.PLLogger;
import com.redbend.android.VdmLogLevel;

public class VdmAgnosticLog {
    private static boolean isRls = VdmAgnosticLog.isRelease();
    private static Logger logger = !isRls ? new DefaultLogger() : new EmptyLogger();

    private VdmAgnosticLog() {
    }

    public static void d(String string2, String string3) {
        logger.d(string2, string3);
    }

    public static void d(String string2, String string3, Throwable throwable) {
        logger.d(string2, string3, throwable);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void destroy() {
        synchronized (VdmAgnosticLog.class) {
            logger.destroy();
            if (!isRls) {
                logger = new DefaultLogger();
                do {
                    return;
                    break;
                } while (true);
            }
            logger = new EmptyLogger();
            return;
        }
    }

    public static void e(String string2, String string3) {
        logger.e(string2, string3);
    }

    public static void e(String string2, String string3, Throwable throwable) {
        logger.e(string2, string3, throwable);
    }

    public static boolean getIsRls() {
        return isRls;
    }

    public static void i(String string2, String string3) {
        logger.i(string2, string3);
    }

    public static void i(String string2, String string3, Throwable throwable) {
        logger.i(string2, string3, throwable);
    }

    private static native void initIDs();

    private static boolean isRelease() {
        try {
            boolean bl = VdmAgnosticLog.isRls();
            return bl;
        }
        catch (UnsatisfiedLinkError var1_1) {
            return false;
        }
    }

    private static native boolean isRls();

    protected static void logMsg(int n, String string2) {
        logger.logMsg(VdmLogLevel.values()[n - 1], string2);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public static void setLogger(PLLogger pLLogger) {
        synchronized (VdmAgnosticLog.class) {
            block6 : {
                boolean bl = isRls;
                if (!bl) break block6;
                do {
                    return;
                    break;
                } while (true);
            }
            logger = new PLLoggerImpl(pLLogger);
            return;
        }
    }

    protected static native void terminate();

    public static void v(String string2, String string3) {
        logger.v(string2, string3);
    }

    public static void v(String string2, String string3, Throwable throwable) {
        logger.v(string2, string3, throwable);
    }

    private static String vdmTag(String string2) {
        return new String(string2 + " (" + Thread.currentThread().getName() + ") ");
    }

    public static void w(String string2, String string3) {
        logger.w(string2, string3);
    }

    public static void w(String string2, String string3, Throwable throwable) {
        logger.w(string2, string3, throwable);
    }

    static class DefaultLogger
    extends Logger {
        DefaultLogger() {
            super();
        }

        @Override
        public void d(String string2, String string3) {
            Log.d((String)VdmAgnosticLog.vdmTag(string2), (String)string3);
        }

        @Override
        public void d(String string2, String string3, Throwable throwable) {
            Log.d((String)VdmAgnosticLog.vdmTag(string2), (String)string3, (Throwable)throwable);
        }

        @Override
        public void e(String string2, String string3) {
            Log.e((String)VdmAgnosticLog.vdmTag(string2), (String)string3);
        }

        @Override
        public void e(String string2, String string3, Throwable throwable) {
            Log.e((String)VdmAgnosticLog.vdmTag(string2), (String)string3, (Throwable)throwable);
        }

        @Override
        public void i(String string2, String string3) {
            Log.i((String)VdmAgnosticLog.vdmTag(string2), (String)string3);
        }

        @Override
        public void i(String string2, String string3, Throwable throwable) {
            Log.i((String)VdmAgnosticLog.vdmTag(string2), (String)string3, (Throwable)throwable);
        }

        @Override
        public void v(String string2, String string3) {
            Log.v((String)VdmAgnosticLog.vdmTag(string2), (String)string3);
        }

        @Override
        public void v(String string2, String string3, Throwable throwable) {
            Log.v((String)VdmAgnosticLog.vdmTag(string2), (String)string3, (Throwable)throwable);
        }

        @Override
        public void w(String string2, String string3) {
            Log.w((String)VdmAgnosticLog.vdmTag(string2), (String)string3);
        }

        @Override
        public void w(String string2, String string3, Throwable throwable) {
            Log.w((String)VdmAgnosticLog.vdmTag(string2), (String)string3, (Throwable)throwable);
        }
    }

    static class EmptyLogger
    extends Logger {
        EmptyLogger() {
            super();
        }
    }

    private static class Logger {
        private Logger() {
        }

        public void d(String string2, String string3) {
        }

        public void d(String string2, String string3, Throwable throwable) {
        }

        public void destroy() {
        }

        public void e(String string2, String string3) {
        }

        public void e(String string2, String string3, Throwable throwable) {
        }

        public void i(String string2, String string3) {
        }

        public void i(String string2, String string3, Throwable throwable) {
        }

        protected void logMsg(VdmLogLevel vdmLogLevel, String string2) {
        }

        public void v(String string2, String string3) {
        }

        public void v(String string2, String string3, Throwable throwable) {
        }

        public void w(String string2, String string3) {
        }

        public void w(String string2, String string3, Throwable throwable) {
        }
    }

    static class PLLoggerImpl
    extends Logger {
        static PLLogger l;

        PLLoggerImpl(PLLogger pLLogger) {
            l = pLLogger;
            VdmAgnosticLog.initIDs();
        }

        @Override
        public void d(String string2, String string3) {
            l.logMsg(VdmLogLevel.INFO, string3);
        }

        @Override
        public void d(String string2, String string3, Throwable throwable) {
            l.logMsg(VdmLogLevel.INFO, string3);
        }

        @Override
        public void destroy() {
            VdmAgnosticLog.terminate();
            l = null;
        }

        @Override
        public void e(String string2, String string3) {
            l.logMsg(VdmLogLevel.ERROR, string3);
        }

        @Override
        public void e(String string2, String string3, Throwable throwable) {
            l.logMsg(VdmLogLevel.ERROR, string3);
        }

        @Override
        public void i(String string2, String string3) {
            l.logMsg(VdmLogLevel.NOTICE, string3);
        }

        @Override
        public void i(String string2, String string3, Throwable throwable) {
            l.logMsg(VdmLogLevel.NOTICE, string3);
        }

        @Override
        protected void logMsg(VdmLogLevel vdmLogLevel, String string2) {
            l.logMsg(vdmLogLevel, string2);
        }

        @Override
        public void v(String string2, String string3) {
            l.logMsg(VdmLogLevel.DEBUG, string3);
        }

        @Override
        public void v(String string2, String string3, Throwable throwable) {
            l.logMsg(VdmLogLevel.DEBUG, string3);
        }

        @Override
        public void w(String string2, String string3) {
            l.logMsg(VdmLogLevel.WARNING, string3);
        }

        @Override
        public void w(String string2, String string3, Throwable throwable) {
            l.logMsg(VdmLogLevel.WARNING, string3);
        }
    }

}

