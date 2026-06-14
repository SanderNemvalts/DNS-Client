import java.util.List;

public class Main {

    public static void main(String[] args) {

        String domain = args[0];
        DnsClient client = new DnsClient("8.8.8.8", 53);

        List<DnsAnswer> answers = client.resolve(domain);
        for (DnsAnswer answer : answers) {
            System.out.println(answer.getIp());
        }


    }
}
