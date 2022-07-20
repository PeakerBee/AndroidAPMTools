package android.net.tool.ping;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Optional;

public class Ping {

    private static String TAG = "Ping";
    // Used to load the 'ping' library on application startup.
    static {
        System.loadLibrary("ping");
    }

    private Callback callback;


    public Ping(Callback callback) {
        this.callback = callback;
    }

    public void startPing(String param) {
        if (TextUtils.isEmpty(param)) {
            if (callback != null) {
                callback.onError("params is empty");
            }
            return;
        }

        String[] params = param.trim().split("\\s+");
        if (params.length <=0) {
            if (callback != null) {
                callback.onError("params error");
            }
            return;
        }
        Arrays.asList(params).forEach(String::trim);
        nativeStartPing(params);

    }

    public void stopPing() {
        nativeStopPing();
    }

    /**
     * native start ping
     */
    private native void nativeStartPing(String[] params);

    private native void nativeStopPing();

    private void nativeOnEnter(String msg) {
        Optional.of(callback).ifPresent(callback -> callback.onEnter(msg));
    }

    private void nativeOnStart(String msg) {
        Optional.of(callback).ifPresent(callback -> callback.onStart(msg));
    }

    private void nativeOnMessage(String msg) {
        Optional.of(callback).ifPresent(callback -> {
            try {
                IcmpRes res = convertToIcmpRes(msg);
                callback.onMessage(res);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onError(e.getMessage());
            }
        });
    }

    private void nativeOnError(String msg) {
        Optional.of(callback).ifPresent(callback -> callback.onError(msg));
    }

    private void nativeOnStatistics(String msg) {
        Optional.of(callback).ifPresent(callback -> {
            try {
                PingStatistics statistics = convertToPingStatistics(msg);
                callback.onStatistics(statistics);
            } catch (JSONException e) {
                e.printStackTrace();
                callback.onError(e.getMessage());
            }
        });
    }

    private void nativeOnEnd(String msg) {
        Optional.of(callback).ifPresent(callback -> callback.onEnd(msg));
    }

    private IcmpRes convertToIcmpRes(String msg) throws JSONException {
        JSONObject jsonObject = new JSONObject(msg);
        IcmpRes res = new IcmpRes();
        res.setBytes(jsonObject.getInt("bytes"));
        res.setIp(jsonObject.getString("ip"));
        res.setIcmp_seq(jsonObject.getInt("icmp_seq"));
        res.setTtl(jsonObject.getInt("ttl"));
        res.setTruncated(jsonObject.getInt("truncated"));
        res.setTime(jsonObject.getDouble("time"));
        return res;
    }

    private PingStatistics convertToPingStatistics(String msg) throws JSONException {
        JSONObject jsonObject = new JSONObject(msg);
        PingStatistics statistics = new PingStatistics();
        statistics.setHostname(jsonObject.getString("hostname"));
        statistics.setTransmitted(jsonObject.getInt("transmitted"));
        statistics.setReceived(jsonObject.getInt("received"));
        statistics.setLoss(jsonObject.getString("loss"));
        statistics.setTime(jsonObject.getInt("time"));
        statistics.setRtt_min(jsonObject.getDouble("rtt_min"));
        statistics.setRtt_avg(jsonObject.getDouble("rtt_avg"));
        statistics.setRtt_max(jsonObject.getDouble("rtt_max"));
        statistics.setRtt_mdev(jsonObject.getDouble("rtt_mdev"));
        statistics.setDuplicates(jsonObject.getInt("duplicates"));
        statistics.setChecksum(jsonObject.getInt("checksum"));
        statistics.setErrors(jsonObject.getInt("errors"));
        return statistics;
    }

    public interface Callback {
        void onEnter(String msg);
        void onStart(String msg);
        void onMessage(IcmpRes res);
        void onError(String msg);
        void onStatistics(PingStatistics statistics);
        void onEnd(String msg);
    }

}