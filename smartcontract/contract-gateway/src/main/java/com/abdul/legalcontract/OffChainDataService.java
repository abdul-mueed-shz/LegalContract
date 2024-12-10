package com.abdul.legalcontract;

import com.abdul.legalcontract.adapter.in.hyperledger.listener.OffChainDataListener;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class OffChainDataService {
    private final OffChainDataListener offChainDataListener;

    @Async
    public void startListening() throws IOException {
        offChainDataListener.run();
    }
}
