package android.net.tool.ping;

public class IcmpRes {

    private int bytes;

    private String ip;

    private int icmp_seq;

    private int ttl;

    private int truncated;

    private double time;

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public int getIcmp_seq() {
        return icmp_seq;
    }

    public void setIcmp_seq(int icmp_seq) {
        this.icmp_seq = icmp_seq;
    }

    public int getTruncated() {
        return truncated;
    }

    public void setTruncated(int truncated) {
        this.truncated = truncated;
    }

    public int getTtl() {
        return ttl;
    }

    public void setTtl(int ttl) {
        this.ttl = ttl;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public String toString() {
        return "IcmpRes{" +
                "bytes=" + bytes +
                ", ip='" + ip + '\'' +
                ", icmp_seq=" + icmp_seq +
                ", ttl=" + ttl +
                ", truncated=" + truncated +
                ", time=" + time +
                '}';
    }
}

