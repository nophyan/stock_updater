/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  android.util.Log
 *  org.apache.http.conn.ssl.StrictHostnameVerifier
 */
package com.redbend.vdm.comm;

import android.util.Log;
import com.redbend.android.VdmAgnosticLog;
import com.redbend.vdm.comm.CommHttpAuth;
import com.redbend.vdm.comm.CommRawConnection;
import com.redbend.vdm.comm.VdmCommException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.net.ssl.HandshakeCompletedEvent;
import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;

public class VdmRawConnection
implements CommRawConnection {
    private static final int DEAFULT_HTTPS_PORT = 443;
    private static final int DEAFULT_HTTP_PORT = 80;
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$");
    private static final Pattern IPV6_HEX_COMPRESSED_PATTERN;
    private static final Pattern IPV6_STD_PATTERN;
    private static final int PREFER_ANY = 0;
    private static final int PREFER_IPV4 = 1;
    private static final int PREFER_IPV6 = 2;
    protected static final int SOCKET_TIMEOUT = 30;
    private static String _certPath;
    protected Socket _conn = null;
    private DataInputStream _in = null;
    protected boolean _isSslMandatory = false;
    private DataOutputStream _out = null;
    private String _proxy = null;
    private CommHttpAuth.Level _proxyAuthLevel = CommHttpAuth.Level.NONE;
    private String _proxyUsernamePassword = null;
    protected int _timeout;
    private String _userAgent = null;

    static {
        IPV6_STD_PATTERN = Pattern.compile("^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$");
        IPV6_HEX_COMPRESSED_PATTERN = Pattern.compile("^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$");
        _certPath = null;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static void connectSocket(Socket socket, String object, int n, int n2, int[] arrn) throws IOException {
        InetAddress[] arrinetAddress;
        int n3;
        block7 : {
            VdmAgnosticLog.d("vDM", "+VdmRawConnection#connectSocket");
            if (arrn == null) {
                n3 = 0;
            } else {
                n3 = arrn[0];
                arrn[0] = 0;
            }
            if ((arrinetAddress = InetAddress.getAllByName((String)object)) == null) {
                throw new IOException("Did not find any ip address for host: " + (String)object);
            }
            Log.i((String)"VdmRawConnection", (String)("inetAddressArray.length=" + arrinetAddress.length));
            int n4 = 0;
            do {
                if (n4 >= arrinetAddress.length) {
                    object = null;
                    break block7;
                }
                object = arrinetAddress[n4].getHostAddress();
                Log.i((String)"VdmRawConnection", (String)("hostAddress=" + (String)object));
                if (VdmRawConnection.isIPv6StdAddress((String)object) && n3 == 2 || VdmRawConnection.isIPv4Address((String)object) && n3 == 1) break;
                ++n4;
            } while (true);
            object = arrinetAddress[n4];
            Log.i((String)"VdmRawConnection", (String)("found address=" + object.getHostAddress()));
        }
        if (object == null) {
            object = arrinetAddress[0];
        }
        if (arrn != null) {
            n3 = !VdmRawConnection.isIPv6StdAddress(object.getHostAddress()) ? 1 : 2;
            arrn[0] = n3;
        }
        Log.i((String)"VdmRawConnection", (String)("connecting to address=" + object.getHostAddress() + " with timeout: " + n2));
        socket.connect(new InetSocketAddress((InetAddress)object, n), n2);
    }

    public static boolean isIPv4Address(String string2) {
        return IPV4_PATTERN.matcher(string2).matches();
    }

    /*
     * Enabled aggressive block sorting
     */
    public static boolean isIPv6Address(String string2) {
        if (VdmRawConnection.isIPv6StdAddress(string2) || VdmRawConnection.isIPv6HexCompressedAddress(string2)) {
            return true;
        }
        return false;
    }

    public static boolean isIPv6HexCompressedAddress(String string2) {
        return IPV6_HEX_COMPRESSED_PATTERN.matcher(string2).matches();
    }

    public static boolean isIPv6StdAddress(String string2) {
        return IPV6_STD_PATTERN.matcher(string2).matches();
    }

    /*
     * Enabled aggressive block sorting
     */
    private void openHttp(URL object, URL uRL, int n, int[] arrn) throws VdmCommException, IOException {
        VdmAgnosticLog.d("vDM", "+VdmRawConnection#openHttp");
        new String("");
        if (this._proxy != null) {
            object = uRL.getHost();
            n = uRL.getPort();
        } else {
            if (n == -1) {
                n = 80;
            }
            object = object.getHost();
        }
        if (!this._isSslMandatory) {
            this._conn = new Socket();
            VdmRawConnection.connectSocket(this._conn, (String)object, n, this._timeout, arrn);
            return;
        }
        VdmAgnosticLog.e("vDM", "VdmRawConnection#open: Error: configuration requires that HTTPS will be mandatory");
        throw new VdmCommException(VdmCommException.VdmCommError.COMMS_BAD_PROTOCOL.val);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void openHttps(URL var1_1, URL var2_6, int var3_7, String var4_8, int[] var5_9) throws VdmCommException, IOException {
        var6_10 = null;
        VdmAgnosticLog.d("vDM", "+VdmRawConnection#openHttps");
        if (var3_7 == -1) {
            var3_7 = 443;
        }
        if (VdmRawConnection._certPath != null) {
            var7_11 = KeyStore.getInstance("BKS");
            var7_11.load(new FileInputStream(VdmRawConnection._certPath), null);
            var6_10 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var6_10.init((KeyStore)var7_11);
            var7_11 = SSLContext.getInstance("TLS");
            var7_11.init(null, var6_10.getTrustManagers(), null);
            var6_10 = var7_11.getSocketFactory();
            VdmAgnosticLog.d("vDM", "VdmRawConnection#openHttps::after getSocketFactory");
        }
        if (this._proxy == null) ** GOTO lbl31
        VdmAgnosticLog.e("vDM", "VdmRawConnection#openHttps::proxy = " + var2_6.getHost() + ":" + (String)var4_8);
        this._conn = this.createTunnelSocket((SSLSocketFactory)var6_10, var1_1.getHost(), var3_7, var2_6.getHost(), Integer.parseInt((String)var4_8), var5_9);
        ** GOTO lbl37
        catch (KeyStoreException var1_2) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: KeyStoreException: " + var1_2.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_FATAL.val);
        }
        catch (NoSuchAlgorithmException var1_3) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: NoSuchAlgorithmException: " + var1_3.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_FATAL.val);
        }
        catch (KeyManagementException var1_4) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: KeyManagementException: " + var1_4.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_FATAL.val);
        }
        catch (CertificateException var1_5) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: CertificateException: " + var1_5.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.INVALID_INPUT_PARAM.val);
        }
lbl31: // 1 sources:
        VdmAgnosticLog.d("vDM", "VdmRawConnection#openHttps::_proxy == null");
        var2_6 = (HttpsURLConnection)var1_1.openConnection();
        if (var6_10 == null) {
            var6_10 = var2_6.getSSLSocketFactory();
        }
        this._conn = var6_10.createSocket();
        VdmRawConnection.connectSocket(this._conn, var1_1.getHost(), var3_7, this._timeout, var5_9);
lbl37: // 2 sources:
        var2_6 = new StrictHostnameVerifier();
        var4_8 = ((SSLSocket)this._conn).getSession();
        if (var2_6.verify(var1_1.getHost(), (SSLSession)var4_8)) {
            VdmAgnosticLog.d("vDM", "-VdmRawConnection#openHttps");
            return;
        }
        this._conn.close();
        throw new SSLHandshakeException("Hostname mismatch: " + var4_8.getPeerPrincipal());
    }

    public static void setCertificatePath(String string2) {
        _certPath = string2;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    private void tunnelHandshake(Socket var1_1, String var2_3, int var3_5) throws IOException {
        var7_6 = var1_1.getOutputStream();
        var2_3 = "CONNECT " + (String)var2_3 + ":" + var3_5 + " HTTP/1.1\r\n" + "User-Agent: " + this._userAgent + "\r\n" + "Host: " + (String)var2_3 + ":" + var3_5 + "\r\n";
        if (this._proxyAuthLevel == CommHttpAuth.Level.BASIC) {
            var2_3 = (String)var2_3 + "Proxy-Authorization: Basic " + this._proxyUsernamePassword + "\r\n";
        }
        var8_7 = (String)var2_3 + "\r\n";
        try {
            var2_3 = var8_7.getBytes("ASCII7");
        }
        catch (UnsupportedEncodingException var2_4) {
            var2_3 = var8_7.getBytes();
        }
        var7_6.write(var2_3);
        var7_6.flush();
        var2_3 = new byte[200];
        var1_1 = var1_1.getInputStream();
        var5_8 = false;
        var3_5 = 0;
        var4_9 = 0;
        do {
            if (var3_5 >= 2) {
                var1_1 = new String(var2_3, 0, var4_9, "ASCII7");
            }
            var6_10 = var1_1.read();
            if (var6_10 < 0) throw new IOException("proxy Unexpected EOF");
            if (var6_10 == 10) ** GOTO lbl29
            if (var6_10 == 13) continue;
            if (!var5_8) ** GOTO lbl32
            ** GOTO lbl37
lbl29: // 1 sources:
            var5_8 = true;
            ++var3_5;
            continue;
lbl32: // 1 sources:
            if (var4_9 < var2_3.length) {
                var2_3[var4_9] = (byte)var6_10;
                ++var4_9;
                var3_5 = 0;
                continue;
            }
lbl37: // 3 sources:
            var3_5 = 0;
        } while (true);
        catch (UnsupportedEncodingException var1_2) {
            var1_1 = new String(var2_3, 0, var4_9);
        }
        if (var1_1.toLowerCase().indexOf("200 ") == -1) throw new IOException("Unable to tunnel through the defined proxy. Proxy returns \"" + (String)var1_1 + "\"");
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void close() {
        block7 : {
            VdmAgnosticLog.w("vDM", "+VdmRawConnection#close");
            try {
                if (this._in != null) {
                    this._in.close();
                }
                if (this._out != null) {
                    this._out.close();
                }
                if ((var1_1 = this._conn) == null) {
                }
                break block7;
            }
            catch (IOException var1_2) {
                VdmAgnosticLog.w("vDM", "RawConnection#close: Caught IOException: " + var1_2.getMessage());
                var1_2.printStackTrace();
            }
            ** GOTO lbl16
        }
        this._conn.close();
lbl16: // 3 sources:
        VdmAgnosticLog.w("vDM", "-VdmRawConnection#close");
    }

    public SSLSocket createTunnelSocket(SSLSocketFactory object, String string2, int n, String string3, int n2, int[] arrn) throws IOException, UnknownHostException {
        VdmAgnosticLog.d("vDM", "+VdmRawConnection#createTunnelSocket: hostAdd:" + string2 + " hostPort:" + n + " proxyAdd:" + string3 + " proxyPort:" + n2);
        Socket socket = new Socket();
        VdmRawConnection.connectSocket(socket, string3, n2, this._timeout, arrn);
        socket.setSoLinger(false, 0);
        socket.setSoTimeout(this._timeout);
        this.tunnelHandshake(socket, string2, n);
        if (object == null) {
            object = (SSLSocketFactory)SSLSocketFactory.getDefault();
        }
        object = (SSLSocket)object.createSocket(socket, string2, n, true);
        VdmAgnosticLog.d("vDM", "VdmRawConnection#createTunnelSocket socket created");
        object.addHandshakeCompletedListener(new HandshakeCompletedListener(){

            @Override
            public void handshakeCompleted(HandshakeCompletedEvent handshakeCompletedEvent) {
                VdmAgnosticLog.v("vDM", "Handshake finished with PeerHost " + handshakeCompletedEvent.getSession().getPeerHost());
            }
        });
        return object;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void init(String string2, CommHttpAuth.Level level, String string3, String string4, boolean bl) throws VdmCommException {
        VdmAgnosticLog.d("vDM", "+VdmRawConnection#init: proxyUsernamePassword " + string3 + "; userAgent " + string4);
        if (string2 != null) {
            this._proxy = new String(string2);
        }
        this._userAgent = string4 == null ? new String("") : new String(string4);
        if (level != null) {
            this._proxyAuthLevel = level;
        }
        this._proxyUsernamePassword = string3 == null ? new String("") : new String(string3);
        this._isSslMandatory = bl;
        this.setConnectionTimeout(30);
        VdmAgnosticLog.d("vDM", "-VdmRawConnection#init");
    }

    /*
     * Loose catch block
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    @Override
    public void open(String object) throws VdmCommException {
        block17 : {
            String string2 = null;
            VdmAgnosticLog.d("vDM", "+VdmRawConnection#open Url " + (String)object);
            String string3 = new String("");
            int[] arrn = new int[]{0};
            if (!object.contains("://")) {
                this.openProprietaryConnection((String)object);
            } else {
                URL uRL = new URL((String)object);
                int n = uRL.getPort();
                if (this._proxy == null) {
                    object = string2;
                } else {
                    object = new URL(this._proxy);
                    int n2 = object.getPort();
                    if (n2 == -1) throw new VdmCommException(VdmCommException.VdmCommError.BAD_URL.val);
                    string3 = string3 + n2;
                }
                if (!(string2 = uRL.getProtocol()).equalsIgnoreCase("http")) {
                    if (string2.equalsIgnoreCase("https")) {
                        this.openHttps(uRL, (URL)object, n, string3, arrn);
                    }
                    break block17;
                }
                this.openHttp(uRL, (URL)object, n, arrn);
            }
        }
        try {
            this._conn.setSoLinger(false, 0);
            this._conn.setSoTimeout(this._timeout);
            VdmAgnosticLog.d("vDM", "VdmRawConnection#open set timeout " + this._timeout);
            this._out = new DataOutputStream(this._conn.getOutputStream());
            this._in = new DataInputStream(this._conn.getInputStream());
        }
        catch (SocketException var1_4) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: SocketException: " + var1_4.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_ERROR.val);
        }
        catch (SocketTimeoutException var1_5) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: SocketTimeoutException: " + var1_5.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_TIMEOUT.val);
        }
        catch (IllegalArgumentException var1_6) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: IllegalArgumentException: " + var1_6.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.BAD_URL.val);
        }
        catch (UnknownHostException var1_7) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: UnknownHostException: " + var1_7.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.BAD_URL.val);
        }
        catch (IOException var1_8) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open2: IOException: " + var1_8.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_ERROR.val);
        }
        VdmAgnosticLog.d("vDM", "-VdmRawConnection#open");
        return;
        catch (MalformedURLException malformedURLException) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open: MalformedURLException: " + malformedURLException.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.BAD_URL.val);
        }
        catch (IOException iOException) {
            VdmAgnosticLog.e("vDM", "VdmRawConnection#open1: IOException: " + iOException.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_ERROR.val);
        }
    }

    protected void openProprietaryConnection(String string2) throws VdmCommException {
        throw new VdmCommException(VdmCommException.VdmCommError.COMMS_BAD_PROTOCOL.val);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public int receive(byte[] arrby) throws VdmCommException {
        int n;
        block8 : {
            int n2;
            int n3 = 0;
            n = arrby.length;
            VdmAgnosticLog.w("vDM", "+VdmRawConnection#receive msg.length: " + n);
            do {
                if ((n = arrby.length) - n3 <= 0) {
                    n = n3;
                    break block8;
                }
                n2 = this._in.read(arrby, n3, arrby.length - n3);
                if (n2 <= 0) break;
                n3 += n2;
            } while (true);
            n = n3;
            if (n2 != -1) break block8;
            n = n3;
            if (n3 != 0) break block8;
            n = -1;
        }
        if (n != -1) {
            VdmAgnosticLog.d("vDM", "-VdmRawConnection#receive, dataLen = " + n);
            return n;
        }
        try {
            VdmAgnosticLog.w("vDM", "VdmRawConnection#receive: nothing received");
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_ERROR.val);
        }
        catch (SocketTimeoutException var1_2) {
            VdmAgnosticLog.w("vDM", "-VdmRawConnection#receive: SocketTimeoutException: " + var1_2.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_TIMEOUT.val);
        }
        catch (IOException var1_3) {
            VdmAgnosticLog.w("vDM", "-VdmRawConnection#receive: IOException: " + var1_3.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_ERROR.val);
        }
    }

    @Override
    public void send(byte[] arrby) throws VdmCommException {
        VdmAgnosticLog.d("vDM", "+VdmRawConnection#send");
        try {
            this._out.write(arrby);
        }
        catch (SocketTimeoutException var1_2) {
            VdmAgnosticLog.w("vDM", "VdmRawConnection#send: SocketTimeoutException: " + var1_2.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_TIMEOUT.val);
        }
        catch (IOException var1_3) {
            VdmAgnosticLog.w("vDM", "VdmRawConnection#send: IOException: " + var1_3.getMessage());
            throw new VdmCommException(VdmCommException.VdmCommError.COMMS_SOCKET_ERROR.val);
        }
        VdmAgnosticLog.d("vDM", "-VdmRawConnection#send");
        return;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void setConnectionTimeout(int n) {
        if (n > 0) {
            this._timeout = n * 1000;
        }
        VdmAgnosticLog.w("vDM", "-VdmRawConnection#setConnectionTimeout timeout " + this._timeout);
    }

}

