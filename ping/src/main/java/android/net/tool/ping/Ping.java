package android.net.tool.ping;

import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Optional;

public class Ping {

    private static final String TAG = "Ping";

    private boolean printMsg = false;

    private Callback callback;

    private final PingOptions pingOptions;


    // Used to load the 'ping' library on application startup.
    static {
        System.loadLibrary("ping");
    }

    /**
     * Set the address to ping
     *
     * Note that a lookup is not performed here so that we do not accidentally perform a network
     * request on the UI thread.
     *
     * @param address - Address to be pinged
     * @return this object to allow chaining
     */
    public static Ping onAddress(String address) {
        PingOptions options = new PingOptions();
        options.setAddressString(address);
        Ping ping = new Ping(options);
        return ping;
    }


    /**
     * Set the PingOptions to ping
     *
     * @param options - ping options to be pinged
     * @return this object to allow chaining
     */
    public static Ping onOptions(PingOptions options) {
        Ping ping = new Ping(options);
        return ping;
    }

    // This class is not to be instantiated
    private Ping(PingOptions options) {
        this.pingOptions = options;
    }

    /**
     * set number of times to ping the address
     *
     * @param onTimes - numbers of times, 0 = continuous
     * @return this object to allow chaining
     */
    public Ping setTimes(int onTimes) {
        this.pingOptions.setTimes(onTimes);
        return this;
    }

    /**
     * Set the timeout
     *
     * @param timeOutMillis - the timeout for each ping in milliseconds
     * @return this object to allow chaining
     */
    public Ping setTimeOutMillis(int timeOutMillis) {
        if (timeOutMillis < 0) throw new IllegalArgumentException("Times cannot be less than 0");
        pingOptions.setTimeoutMillis(timeOutMillis);
        return this;
    }


    /**
     * Set the time to live
     *
     * @param timeToLive - the TTL for each ping
     * @return this object to allow chaining
     */
    public Ping setTimeToLive(int timeToLive) {
        this.pingOptions.setTimeToLive(timeToLive);
        return this;
    }


    public Ping enableLog(boolean enable) {
        printMsg = enable;
        return this;
    }


    public void startPing(Callback callback) {

        if (isMainThread()) {
            throw new NetworkOnMainThreadException();
        }

        this.callback = callback;
        String[] params = this.pingOptions.convertParam();
        if (params.length <=0) {
            if (callback != null) {
                callback.onError("PingOptions error, " + pingOptions);
            }
            return;
        }
        nativeStartPing(params);

    }

    private boolean isMainThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
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

    private void printMsg(String msg) {
        if (printMsg) {
            Log.i(TAG, msg);
        }
    }

    private void nativeOnMessage(String msg) {
        Optional.of(callback).ifPresent(callback -> {
            try {
                PingResult res = convertToIcmpRes(msg);
                callback.onResult(res);
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

    private PingResult convertToIcmpRes(String msg) throws JSONException {
        JSONObject jsonObject = new JSONObject(msg);
        PingResult res = new PingResult();
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
        void onResult(PingResult res);
        void onError(String msg);
        void onStatistics(PingStatistics statistics);
        void onEnd(String msg);
    }

}