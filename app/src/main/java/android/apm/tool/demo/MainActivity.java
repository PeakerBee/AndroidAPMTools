package android.apm.tool.demo;

import android.apm.tool.fdtrack.FdTrack;
import android.apm.tool.ping.Ping;
import android.apm.tool.ping.PingResult;
import android.apm.tool.ping.PingStatistics;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.chehejia.iot.connector.NodeConnector;
import com.chehejia.iot.stream.pull.mesh.MeshNode;


public class MainActivity extends Activity {

    private String TAG = "MainActivity";

    private Ping ping = Ping.onAddress("baidu.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testFdTrack();
    }

    private void testFdTrack() {
        new Thread(() ->  {
            MeshNode node = new MeshNode("one-map-app");
            NodeConnector.connectVehProxy(node, "iot-cloud-proxy-service-ccell0.chehejia.com", 11025);
            new FdTrack().spawnFdLeakCheckThread();
        }).start();
    }

    private void testPing() {
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
    }
}