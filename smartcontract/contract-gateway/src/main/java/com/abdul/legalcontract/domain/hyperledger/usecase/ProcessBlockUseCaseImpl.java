package com.abdul.legalcontract.domain.hyperledger.usecase;
/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

import com.abdul.legalcontract.domain.hyperledger.parser.Block;
import com.abdul.legalcontract.domain.hyperledger.parser.Transaction;
import com.abdul.legalcontract.domain.hyperledger.port.in.ProcessBlockUseCase;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.Checkpointer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public final class ProcessBlockUseCaseImpl implements ProcessBlockUseCase {
    private final Checkpointer checkpointer;

    public void process(Block block) {
        long blockNumber = block.getNumber();
        System.out.println("\nReceived block " + Long.toUnsignedString(blockNumber));

        try {
            List<Transaction> validTransactions = getNewTransactions(block, checkpointer).stream()
                    .filter(Transaction::isValid)
                    .toList();

            for (Transaction transaction : validTransactions) {
                // new TransactionProcessor(transaction, blockNumber, store).process();
                String transactionId = transaction.getChannelHeader().getTxId();
                System.out.println(transactionId);
                checkpointer.checkpointTransaction(blockNumber, transactionId);
            }
            checkpointer.checkpointBlock(blockNumber);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private List<Transaction> getNewTransactions(Block block, Checkpointer checkpointer)
            throws InvalidProtocolBufferException {
        var transactions = block.getTransactions();

        var lastTransactionId = checkpointer.getTransactionId();
        if (lastTransactionId.isEmpty()) {
            // No previously processed transactions within this block so all are new
            return transactions;
        }
        var transactionIds = new ArrayList<>();
        for (var transaction : transactions) {
            transactionIds.add(transaction.getChannelHeader().getTxId());
        }

        // Ignore transactions up to the last processed transaction ID
        var lastProcessedIndex = transactionIds.indexOf(lastTransactionId.get());
        if (lastProcessedIndex < 0) {
            throw new IllegalArgumentException("Checkpoint transaction ID " + lastTransactionId + " not found in block "
                    + Long.toUnsignedString(block.getNumber()) + " containing transactions: " + transactionIds);
        }

        return transactions.subList(lastProcessedIndex + 1, transactions.size());
    }
}