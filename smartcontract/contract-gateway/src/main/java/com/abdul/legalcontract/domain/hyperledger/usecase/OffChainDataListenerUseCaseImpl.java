package com.abdul.legalcontract.domain.hyperledger.usecase;

import com.abdul.legalcontract.config.Utils;
import com.abdul.legalcontract.domain.hyperledger.model.Write;
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
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OffChainDataListenerUseCaseImpl implements OffChainDataListenerUseCase {
    private static final Path STORE_FILE = Paths.get(Utils.getEnvOrDefault("STORE_FILE", "store.log"));
    private static final int SIMULATED_FAILURE_COUNT = Utils.getEnvOrDefault("SIMULATED_FAILURE_COUNT", Integer::parseUnsignedInt, 0);

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

    private void applyWritesToOffChainStore(
            final long blockNumber,
            final String transactionId,
            final List<Write> writes) throws IOException {

        try (var writer = new StringWriter()) {
            for (var write : writes) {
                GSON.toJson(write, writer);
                writer.append('\n');
            }

            Files.writeString(STORE_FILE, writer.toString(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
    }
}
