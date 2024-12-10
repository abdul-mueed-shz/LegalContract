package com.abdul.legalcontract;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class LegalContractApplication {

    public static void main(String[] args) {
        SpringApplication.run(LegalContractApplication.class, args);
    }

}
