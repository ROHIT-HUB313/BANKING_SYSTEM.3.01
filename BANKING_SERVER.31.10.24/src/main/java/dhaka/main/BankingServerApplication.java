package dhaka.main;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "dhaka.entity") 
@EnableJpaRepositories(basePackages = "dhaka.repository")
@ComponentScan(basePackages = {"dhaka.service", "dhaka.repository", "dhaka.main"})
public class BankingServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingServerApplication.class, args);
	}
}
