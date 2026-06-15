import java.io.*;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.List;

public class DnsClient {

    private final String dnsServer;
    private final int port;

    public DnsClient(String dnsServer, int port) {
        this.dnsServer = dnsServer;
        this.port = port;
    }

    public List<DnsAnswer> resolve(String domain) {
        final short queryId = 6868;

        try (Socket socket = new Socket(dnsServer, port);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            DnsMessageBuilder builder = new DnsMessageBuilder();
            byte[] message = builder.buildIpv4Query(domain, queryId);
            out.writeShort(message.length);
            out.write(message);


            short dataLen = in.readShort();
            byte[] data = in.readNBytes(dataLen);

            return DnsResponseParser.parse(data);
        } catch (IOException e) {
            System.err.println("Server connection error");
            throw new RuntimeException(e);
        }

    }

}
