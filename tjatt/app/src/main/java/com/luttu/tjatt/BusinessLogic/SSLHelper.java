package com.luttu.tjatt.BusinessLogic;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

public class SSLHelper {

    private Socket mSocket;
    private SSLContext mSSLContext;
    private HostnameVerifier mHostnameVerifier;
    private final String DEBUG_TAG = "SSLHelper";
    private String mServerIP = "130.229.162.86";

    public Socket getSocket() {
        return mSocket;
    }

    public void setSocket(Socket mSocket) {
        this.mSocket = mSocket;
    }

    public SSLContext getSSLContext() {
        return mSSLContext;
    }

    public void setSSLContext(SSLContext mSSLContext) {
        this.mSSLContext = mSSLContext;
    }

    public HostnameVerifier getHostnameVerifier() {
        return mHostnameVerifier;
    }

    public void setHostnameVerifier(HostnameVerifier mHostnameVerifier) {
        this.mHostnameVerifier = mHostnameVerifier;
    }

    public String getServerIP() {
        return mServerIP;
    }

    public void setServerIP(String mServerIP) {
        this.mServerIP = mServerIP;
    }

    public SSLHelper() {

        {
            try {
                IO.Options opts = new IO.Options();
                mSSLContext = SSLContext.getInstance("TLS");
                mSSLContext.init(null, null, null);
                mHostnameVerifier = new HostnameVerifier() {
                    @Override
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                };
                opts.sslContext = mSSLContext;
                opts.hostnameVerifier = mHostnameVerifier;
                // 10.0.2.2 för AVD
                mSocket = IO.socket("https://" + mServerIP + ":443", opts);
            } catch (URISyntaxException e) {
                Log.e(DEBUG_TAG, e.toString());
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (NoSuchAlgorithmException e) {
                Log.e(DEBUG_TAG, e.toString());
                e.printStackTrace();
            } catch (KeyManagementException e) {
                Log.e(DEBUG_TAG, e.toString());
                e.printStackTrace();
            } catch (Exception e) {
                Log.e(DEBUG_TAG, e.toString());
            }
        }
    }
}
