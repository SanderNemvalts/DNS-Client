import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

        try (Socket socket = new Socket(dnsServer, port);
            OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {

            DnsMessageBuilder builder = new DnsMessageBuilder();
            out.write(builder.buildMessage());

            ByteBuffer lenBuf = ByteBuffer.wrap(in.readNBytes(2));
            short dataLen = lenBuf.getShort();
            byte[] data = in.readNBytes(dataLen);

            return DnsResponseParser.parse(data);
        } catch (IOException e) {
            System.err.println("Server connection error");
            throw new RuntimeException(e);
        }

    }

}
