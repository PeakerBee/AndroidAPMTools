package com.chehejia.iot.ping;

import android.util.Log;

public class Ping {

    private static String TAG = "Ping";
    // Used to load the 'ping' library on application startup.
    static {
        System.loadLibrary("ping");
    }

    private ActionListener actionListener;

    /**
     * start ping
     */
    public native void nativeStartPing(String[] params);


    public void nativeOnStart(String msg) {
        Log.i(TAG, "nativeOnStart: " + msg);
        if (null != actionListener) {
            actionListener.onStart();
        }
    }

    public void nativeOnMessage(String msg) {
        Log.i(TAG, "nativeOnMessage: " + msg);
        if (null != actionListener) {
            actionListener.onUpdate(msg);
        }
    }

    public void nativeOnError(String msg) {
        Log.i(TAG, "nativeOnError: " + msg);
    }

    public void nativeOnEnd(String msg) {
        Log.i(TAG, "nativeOnEnd: " + msg);
        if (null != actionListener) {
            actionListener.onEnd();
        }
    }

    public interface ActionListener {
        void onStart();
        void onUpdate(String update);
        void onEnd();
    }

}