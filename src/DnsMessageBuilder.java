import java.nio.ByteBuffer;
import java.util.Arrays;

public class DnsMessageBuilder {

    public byte[] buildIpv4Query(String domain, short id) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        buffer.put(buildHeader(
                id,
                (short) 256, // standard query
                (short) 1,
                (short) 0,
                (short) 0,
                (short) 0
        ));

        buffer.put(buildDomain(domain));

        buffer.putShort((short) 0x001); // QTYPE A
        buffer.putShort((short) 0x001); // QCLASS IN

        return Arrays.copyOf(buffer.array(), buffer.position());
    }

    private byte[] buildHeader(short id,
                               short flags,
                               short QDCOUNT,
                               short ANCOUNT,
                               short NSCOUNT,
                               short ARCOUNT)
    {
        ByteBuffer buffer = ByteBuffer.allocate(12);

        buffer.putShort(id);
        buffer.putShort(flags);
        buffer.putShort(QDCOUNT);
        buffer.putShort(ANCOUNT);
        buffer.putShort(NSCOUNT);
        buffer.putShort(ARCOUNT);

        buffer.flip();
        return buffer.array();
    }

    private byte[] buildDomain(String domain) {
        String[] parts = domain.split("\\.");
        ByteBuffer buffer = ByteBuffer.allocate(domain.getBytes().length + 2);

        for (String part : parts) {
            byte[] bytes = part.getBytes();
            buffer.put((byte) bytes.length).put(bytes);
        }

        buffer.put((byte) 0);

        buffer.flip();
        return buffer.array();
    }

}
