package com.bank.apigateway;

import com.bank.apigateway.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class BankApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankApiGatewayApplication.class, args);
    }

}
