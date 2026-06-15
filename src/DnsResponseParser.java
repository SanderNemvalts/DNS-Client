import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class DnsResponseParser {

    public static List<DnsAnswer> parse(byte[] data) {

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int answerCount = parseHeader(buffer);

        // skip question part
        byte len;
        do {
            len = buffer.get();
            buffer.position(buffer.position() + len);
        } while (len > (byte) 0);

        // parse answer parts
        for (int i = 0; i < answerCount; ++i) {

        }


        return new ArrayList<>();
    }

    // returns answer count
    private static int parseHeader(ByteBuffer buffer) {
        short id = buffer.getShort();
        short flags = buffer.getShort();
        short QDCOUNT = buffer.getShort();
        short ANCOUNT = buffer.getShort();

        if (flags == (short) 0x8180) {
            return (int) ANCOUNT;
        } else return 0;
    }

}
