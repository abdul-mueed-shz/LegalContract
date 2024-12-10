package com.abdul.legalcontract.config;

import com.abdul.legalcontract.domain.hyperledger.port.in.StartOffChainDataListenerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppStartUpConfig {
    private final StartOffChainDataListenerUseCase startOffChainDataListenerUseCase;

    @Bean
    public ApplicationRunner startupRunner() {
        return args -> startOffChainDataListenerUseCase.startListening();
    }
}
