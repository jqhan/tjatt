package com.luttu.tjatt;

import android.app.Application;

import com.github.nkzawa.socketio.client.Socket;
import com.luttu.tjatt.BusinessLogic.SSLHelper;
import com.luttu.tjatt.BusinessLogic.TjattModel;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

public class TjattApp extends Application {

    private Socket mSocket;
    private TjattModel mModel = new TjattModel();
    private static TjattApp mInstance;
    private SSLContext mSSLContext;
    private HostnameVerifier mHostnameVerifier;
    private static final String DEBUG_TAG = "TjattApp";
    private SSLHelper mSSLHelper = new SSLHelper();

    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mModel.setSSLHelper(mSSLHelper);
        mModel.update();
        mModel.setContext(mInstance);
    }

    public TjattModel getModel() {
        return mModel;
    }

    public void setModel(TjattModel model) {
        mModel = model;
    }

    public Socket getSocket() {
        return mSocket;
    }
}
