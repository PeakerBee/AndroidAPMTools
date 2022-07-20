package android.net.tool.demo;

import android.app.Activity;
import android.net.tool.ping.IcmpRes;
import android.net.tool.ping.Ping;
import android.net.tool.ping.PingStatistics;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


public class MainActivity extends Activity {

    private String TAG = "MainActivity";

    private Ping ping = new Ping(new Ping.Callback() {
        @Override
        public void onEnter(String msg) {
            Log.i(TAG, "onEnter: " + msg);
        }

        @Override
        public void onStart(String msg) {
            Log.i(TAG, "onStart: " + msg);
        }

        @Override
        public void onMessage(IcmpRes res) {
            Log.i(TAG, "onMessage: " + res);
        }

        @Override
        public void onError(String msg) {
            Log.i(TAG, "onError: " + msg);
        }

        @Override
        public void onStatistics(PingStatistics statistics) {
            Log.i(TAG, "onStatistics: " + statistics);
        }

        @Override
        public void onEnd(String msg) {
            Log.i(TAG, "onEnd: " + msg);
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(() -> ping.startPing("baidu.com")).start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ping.stopPing();
            }
        }, 10000);
    }
}