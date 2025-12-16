package pl.piomin.payment;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.piomin.payment.domain.Customer;
import pl.piomin.payment.repository.CustomerRepository;
@SpringBootApplication
public class PaymentApp  {


    public static void main(String[] args) {
        SpringApplication.run(PaymentApp.class, args);
    }

}
