package com.mkr.ecom.ecomeureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EcomeurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcomeurekaApplication.class, args);
    }

}
