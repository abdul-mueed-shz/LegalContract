package com.abdul.legalcontract.config;

import com.abdul.legalcontract.OffChainDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppStartUpConfig {
    private final OffChainDataService offChainDataStartupService;

    @Bean
    public ApplicationRunner startupRunner() {
        return args -> offChainDataStartupService.startListening();
    }
}
