package android.apm.tool.ping;

public class PingStatistics {

    private String hostname;
    private int transmitted;
    private int received;
    private String loss;
    private int time;
    private double rtt_min;
    private double rtt_avg;
    private double rtt_max;
    private double rtt_mdev;
    private int duplicates;
    private int checksum;
    private int errors;

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
    public String getHostname() {
        return hostname;
    }

    public void setTransmitted(int transmitted) {
        this.transmitted = transmitted;
    }
    public int getTransmitted() {
        return transmitted;
    }

    public void setReceived(int received) {
        this.received = received;
    }
    public int getReceived() {
        return received;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }
    public String getLoss() {
        return loss;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public int getTime() {
        return time;
    }

    public void setRtt_min(double rtt_min) {
        this.rtt_min = rtt_min;
    }
    public double getRtt_min() {
        return rtt_min;
    }

    public void setRtt_avg(double rtt_avg) {
        this.rtt_avg = rtt_avg;
    }
    public double getRtt_avg() {
        return rtt_avg;
    }

    public void setRtt_max(double rtt_max) {
        this.rtt_max = rtt_max;
    }
    public double getRtt_max() {
        return rtt_max;
    }

    public void setRtt_mdev(double rtt_mdev) {
        this.rtt_mdev = rtt_mdev;
    }
    public double getRtt_mdev() {
        return rtt_mdev;
    }

    public void setDuplicates(int duplicates) {
        this.duplicates = duplicates;
    }
    public int getDuplicates() {
        return duplicates;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }
    public int getChecksum() {
        return checksum;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }
    public int getErrors() {
        return errors;
    }

    @Override
    public String toString() {
        return "PingStatistics{" +
                "hostname='" + hostname + '\'' +
                ", transmitted=" + transmitted +
                ", received=" + received +
                ", loss='" + loss + '\'' +
                ", time=" + time +
                ", rtt_min=" + rtt_min +
                ", rtt_avg=" + rtt_avg +
                ", rtt_max=" + rtt_max +
                ", rtt_mdev=" + rtt_mdev +
                ", duplicates=" + duplicates +
                ", checksum=" + checksum +
                ", errors=" + errors +
                '}';
    }
}
