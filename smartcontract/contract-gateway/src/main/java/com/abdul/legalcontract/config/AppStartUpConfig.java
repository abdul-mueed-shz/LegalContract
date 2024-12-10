package com.abdul.legalcontract.config;

import com.abdul.legalcontract.adapter.in.hyperledger.listener.OffChainDataListener;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppStartUpConfig {
    private final OffChainDataListener offChainDataListener;

    @Bean
    public ApplicationRunner startupRunner() {
        return args -> offChainDataListener.startListening();
    }
}
