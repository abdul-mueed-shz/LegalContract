package com.abdul.legalcontract.adapter.in.hyperledger.listener;

import com.abdul.legalcontract.config.BlockProcessor;
import com.abdul.legalcontract.config.Utils;
import com.abdul.legalcontract.config.parser.BlockParser;
import com.abdul.legalcontract.domain.hyperledger.model.Write;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.FileCheckpointer;
import org.hyperledger.fabric.client.Network;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OffChainDataListener {


    private static final Path STORE_FILE =
            Paths.get(Utils.getEnvOrDefault("STORE_FILE", "store.log"));
    private static final int SIMULATED_FAILURE_COUNT =
            Utils.getEnvOrDefault("SIMULATED_FAILURE_COUNT", Integer::parseUnsignedInt, 0);

    private static final long START_BLOCK = 0L;

    private final Gson GSON;
    private final Network network;
    private final FileCheckpointer fileCheckpointer;

    public void run() throws IOException {
        log.info("Starting event listening from block {}",
                Long.toUnsignedString(fileCheckpointer.getBlockNumber().orElse(START_BLOCK)));

        log.info(fileCheckpointer.getTransactionId()
                .map(transactionId -> "Last processed transaction ID within block: " + transactionId)
                .orElse("No last processed transaction ID"));

        var blocks = network.newBlockEventsRequest()
                .startBlock(START_BLOCK) // Used only if there is no checkpoint block number
                .checkpoint(fileCheckpointer)
                .build()
                .getEvents();

        blocks.forEachRemaining(blockProto -> {
            var block = BlockParser.parseBlock(blockProto);
            var processor = new BlockProcessor(block, fileCheckpointer);
            processor.process();
        });
    }

    private void applyWritesToOffChainStore(final long blockNumber, final String transactionId,
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
