package com.abdul.legalcontract.adapter.in.hyperledger.listener;

import com.abdul.legalcontract.domain.hyperledger.port.in.OffChainDataListenerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OffChainDataListener {
    private final OffChainDataListenerUseCase offChainDataListenerUseCase;

    @Async
    public void startListening() throws IOException {
        offChainDataListenerUseCase.execute();
    }
}
