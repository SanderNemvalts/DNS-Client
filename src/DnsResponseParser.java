import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DnsResponseParser {

    public static List<DnsAnswer> parse(byte[] data) throws IOException{
        ByteBuffer buffer = ByteBuffer.wrap(data);

        // parse header
        short id = buffer.getShort();
        short flags = buffer.getShort();
        short QDCOUNT = buffer.getShort();
        short ANCOUNT = buffer.getShort();
        short NSCOUNT = buffer.getShort();
        short ARCOUNT = buffer.getShort();
        if (flags != (short) -32384) { // Server: success
            throw new IOException("Unexpected server response: flags");
        }

        // skip question part
        byte len;
        do {
            len = buffer.get();
            buffer.position(buffer.position() + (int) len);
        } while (len > (byte) 0);
        buffer.position(buffer.position() + 4);

        // parse answer parts
        List<DnsAnswer> answers = new ArrayList<>();
        for (int i = 0; i < ANCOUNT; ++i) {

            // domain name
            byte namePointerCheck = buffer.get();
            String domain;
            if ((namePointerCheck & 0xFF) == 192) { // name is of pointer type
                int offset = (int) buffer.get();
                domain = parseDomain(data, offset);
            } else {
                domain = parseDomain(data, buffer.position());
            }

            short ansType = buffer.getShort();
            short ansClass = buffer.getShort();
            if (ansType != (short) 1 || ansClass != (short) 1) { // skip this answer
                int ttl = buffer.getInt();
                short dataLen = buffer.getShort();
                buffer.position(buffer.position() + (int) dataLen);
                continue;
            }
            int ttl = buffer.getInt();

            short dataLen = buffer.getShort();
            byte[] ipBytes = new byte[dataLen];
            buffer.get(ipBytes);
            String ip = ipToString(ipBytes);

            answers.add(new DnsAnswer(ip, domain, ttl));
        }
        return answers;
    }

    // returns answer count
    private static String ipToString(byte[] ipBytes) {
        StringBuilder builder = new StringBuilder();
        for (byte ipNr : ipBytes) {
            builder.append(ipNr & 0xFF).append('.');
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

    private static String parseDomain(byte[] data, int offset) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, data.length - offset);
        StringBuilder builder = new StringBuilder();
        byte len = buffer.get();

        while (len > (byte) 0) {
            byte[] partBytes = new byte[len];
            buffer.get(partBytes);
            builder.append(new String(partBytes));
            builder.append('.');
            len = buffer.get();
        }

        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }

}
