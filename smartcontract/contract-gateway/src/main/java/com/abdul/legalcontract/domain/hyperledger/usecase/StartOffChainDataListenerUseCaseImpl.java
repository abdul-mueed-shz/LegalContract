package com.abdul.legalcontract.domain.hyperledger.usecase;

import com.abdul.legalcontract.adapter.in.hyperledger.listener.OffChainDataListener;
import com.abdul.legalcontract.domain.hyperledger.port.in.StartOffChainDataListenerUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class StartOffChainDataListenerUseCaseImpl implements StartOffChainDataListenerUseCase {
    private final OffChainDataListener offChainDataListener;

    @Async
    public void startListening() throws IOException {
        offChainDataListener.run();
    }
}
