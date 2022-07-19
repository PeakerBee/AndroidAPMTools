package com.chehejia.iot.ping;

public class Ping {

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
        if (null != actionListener) {
            actionListener.onStart();
        }
    }

    public void nativeOnMessage(String msg) {
        if (null != actionListener) {
            actionListener.onUpdate(msg);
        }
    }

    public void nativeOnError(String msg, int code) {

    }

    public void nativeOnEnd(String msg) {
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