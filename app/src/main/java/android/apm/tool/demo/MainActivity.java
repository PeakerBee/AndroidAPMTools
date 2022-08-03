package android.apm.tool.demo;

import android.app.Activity;
import android.apm.tool.ping.Ping;
import android.apm.tool.ping.PingResult;
import android.apm.tool.ping.PingStatistics;
import android.net.tool.demo.R;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends Activity {

    private String TAG = "MainActivity";

    private Ping ping = Ping.onAddress("baidu.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ping.setTimes(5);
        new Thread(() -> ping.startPing(new Ping.Callback() {
            @Override
            public void onEnter(String msg) {
                Log.i(TAG, "onEnter: " + msg);
            }

            @Override
            public void onStart(String msg) {
                Log.i(TAG, "onStart: " + msg);
            }

            @Override
            public void onResult(PingResult res) {
                Log.i(TAG, "onResult: " + res);
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
        })).start();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ping.stopPing();
//            }
//        }, 10000);
    }
}