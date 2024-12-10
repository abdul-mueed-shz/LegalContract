package com.abdul.legalcontract.domain.hyperledger.usecase;

import com.abdul.legalcontract.domain.hyperledger.parser.Transaction;
import com.abdul.legalcontract.domain.hyperledger.port.in.ProcessTransactionUseCase;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Service
public class ProcessTransactionUseCaseImpl implements ProcessTransactionUseCase {
    // Typically we should ignore read/write sets that apply to system chaincode namespaces.
    private static final Set<String> SYSTEM_CHAINCODE_NAMES = Set.of(
            "_lifecycle",
            "cscc",
            "escc",
            "lscc",
            "qscc",
            "vscc"
    );

    private static boolean isSystemChaincode(final String chaincodeName) {
        return SYSTEM_CHAINCODE_NAMES.contains(chaincodeName);
    }

    public void process(long blockNumber, Transaction transaction) throws IOException {
        var channelName = transaction.getChannelHeader().getChannelId();
        for (var readWriteSet : transaction.getNamespaceReadWriteSets()) {
            var namespace = readWriteSet.getNamespace();
            if (isSystemChaincode(namespace)) {
                continue;
            }
            System.out.println("Process channelName " + channelName);
            System.out.println("Process namespace " + namespace);
        }
    }
}
