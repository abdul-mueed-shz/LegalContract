package com.abdul.legalcontract.domain.hyperledger.usecase;

import com.abdul.legalcontract.domain.hyperledger.port.in.BlockParserUseCase;
import com.abdul.legalcontract.domain.hyperledger.port.in.OffChainDataListenerUseCase;
import com.abdul.legalcontract.domain.hyperledger.port.in.ProcessBlockUseCase;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.Checkpointer;
import org.hyperledger.fabric.client.Network;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class OffChainDataListenerUseCaseImpl implements OffChainDataListenerUseCase {
    private static final long START_BLOCK = 0L;

    private final Gson GSON;
    private final Network network;
    private final Checkpointer fileCheckpointer;
    private final BlockParserUseCase blockParserUseCase;
    private final ProcessBlockUseCase processBlockUseCase;

    @Override
    public void execute() throws IOException {
        log.info("Starting event listening from block {}",
                Long.toUnsignedString(fileCheckpointer.getBlockNumber().orElse(START_BLOCK)));

        log.info(fileCheckpointer.getTransactionId().map(transactionId -> "Last processed transaction ID within block: "
                + transactionId).orElse("No last processed transaction ID"));

        var blocks = network.newBlockEventsRequest().startBlock(START_BLOCK) // Used only if there is no checkpoint block number
                .checkpoint(fileCheckpointer).build().getEvents();

        blocks.forEachRemaining(blockProto -> {
            var block = blockParserUseCase.parseBlock(blockProto);
            processBlockUseCase.process(block);
        });
    }
}
