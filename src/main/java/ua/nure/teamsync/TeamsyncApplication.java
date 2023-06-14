package ua.nure.teamsync;

import com.braintreegateway.BraintreeGateway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TeamsyncApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeamsyncApplication.class, args);
	}

//	@Bean
//	public BraintreeGateway braintreeGateway() {
//		return new BraintreeGateway("teamsync-client", "secret");
//	}

}
