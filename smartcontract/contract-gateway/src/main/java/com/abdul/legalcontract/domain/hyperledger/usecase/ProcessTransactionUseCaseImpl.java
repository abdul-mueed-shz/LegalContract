package com.abdul.legalcontract.domain.hyperledger.usecase;

import com.abdul.legalcontract.domain.hyperledger.model.TransactionInfo;
import com.abdul.legalcontract.domain.hyperledger.model.Write;
import com.abdul.legalcontract.domain.hyperledger.parser.NamespaceReadWriteSet;
import com.abdul.legalcontract.domain.hyperledger.parser.Transaction;
import com.abdul.legalcontract.domain.hyperledger.port.in.ProcessTransactionUseCase;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

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

    @Override
    public List<TransactionInfo> process(long blockNumber, Transaction transaction) throws IOException {
        String transactionId = transaction.getChannelHeader().getTxId();
        String channelName = transaction.getChannelHeader().getChannelId();

        AtomicReference<ArrayList<Write>> writes = new AtomicReference<>(new ArrayList<Write>());
        for (NamespaceReadWriteSet readWriteSet : transaction.getNamespaceReadWriteSets()) {
            String contractName = readWriteSet.getNamespace();
            if (isSystemChaincode(contractName)) {
                continue;
            }
            readWriteSet.getReadWriteSet().getWritesList().stream()
                    .map(write -> new Write(channelName, contractName, write))
                    .forEach(writes.get()::add);
        }
        System.out.println("TransactionId -> " + transactionId);
        System.out.println("ChannelName -> " + channelName);
        writes.get().forEach(write -> {
            System.out.println("NameSpace -> " + write.getNamespace());
            System.out.println("isDelete -> " + write.isDelete());
            System.out.println("Key -> " + write.getKey());
            System.out.println("Content -> " + write.getValue());
        });
        return new ArrayList<>();
    }
}
