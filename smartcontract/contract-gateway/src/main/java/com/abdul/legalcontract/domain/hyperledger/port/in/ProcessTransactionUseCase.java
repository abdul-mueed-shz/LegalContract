package com.abdul.legalcontract.domain.hyperledger.port.in;

import com.abdul.legalcontract.domain.hyperledger.parser.Transaction;

import java.io.IOException;

public interface ProcessTransactionUseCase {
    
    void process(long blockNumber, Transaction transaction) throws IOException;
}
