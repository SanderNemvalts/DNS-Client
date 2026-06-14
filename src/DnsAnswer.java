public class DnsAnswer {
    private final String domain;
    private final String ip;
    private final int ttl;

    public DnsAnswer(String ip, String domain, int ttl) {
        this.ip = ip;
        this.domain = domain;
        this.ttl = ttl;
    }

    public String getDomain() {return domain;}
    public String getIp() {return ip;}
    public int getTtl() {return ttl;}
}
