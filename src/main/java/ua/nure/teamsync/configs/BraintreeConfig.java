package ua.nure.teamsync.configs;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BraintreeConfig {
    @Value("${teamsync.braintree.merchantId}")
    private String merchantId;

    @Value("${teamsync.braintree.publicKey}")
    private String publicKey;

    @Value("${teamsync.braintree.privateKey}")
    private String privateKey;

    @Value("${teamsync.braintree.environment}")
    private String environment;

    @Bean
    public BraintreeGateway getBraintreeGateway() {
        BraintreeGateway gateway = new BraintreeGateway(
                Environment.parseEnvironment(environment),
                merchantId,
                publicKey,
                privateKey
        );

        return gateway;
    }
}
