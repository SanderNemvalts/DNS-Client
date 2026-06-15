import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        List<DnsAnswer> answers = new ArrayList<>();
        DnsClient client = new DnsClient("8.8.8.8", 53);
        for (String domain : args) {
            answers.addAll(client.resolve(domain));
        }

        for (DnsAnswer answer : answers) {
            System.out.println(answer.domain() + " | " + answer.ip());
        }


    }
}
