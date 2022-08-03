package android.apm.tool.ping;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class PingOptions {

    private String addressString = null;
    private int timeoutMillis;
    private int timeToLive;
    private int times;

    public void setAddressString(String addressString) {
        this.addressString = addressString;
    }

    public String getAddressString() {
        return addressString;
    }

    public void setTimeoutMillis(int timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
    }

    public int getTimeoutMillis() {
        return timeoutMillis;
    }

    public void setTimeToLive(int timeToLive) {
        this.timeToLive = timeToLive;
    }

    public int getTimeToLive() {
        return timeToLive;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public int getTimes() {
        return times;
    }

    @Override
    public String toString() {
        return "PingOptions{" +
                "addressString='" + addressString + '\'' +
                ", timeoutMillis=" + timeoutMillis +
                ", timeToLive=" + timeToLive +
                ", times=" + times +
                '}';
    }

    public String[] convertParam() {
        List<String> list = new ArrayList<>();
        if (TextUtils.isEmpty(addressString)) {
            return list.toArray(new String[]{});
        }

        list.add("ping");


        if (times > 0) {
            list.add("-c");
            list.add(String.valueOf(times));
        }

        if (timeoutMillis > 0) {
            list.add("-W");
            list.add(String.valueOf(timeoutMillis));
        }

        if (timeToLive > 0) {
            list.add("-t");
            list.add(String.valueOf(timeToLive));
        }



        list.add(addressString);

        return list.toArray(new String[]{});
    }
}
