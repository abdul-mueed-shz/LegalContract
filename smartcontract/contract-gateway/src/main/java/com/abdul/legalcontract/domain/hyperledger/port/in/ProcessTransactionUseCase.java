package com.abdul.legalcontract.domain.hyperledger.port.in;

import com.abdul.legalcontract.domain.hyperledger.model.TransactionInfo;
import com.abdul.legalcontract.domain.hyperledger.parser.Transaction;

import java.io.IOException;
import java.util.List;

public interface ProcessTransactionUseCase {

    List<TransactionInfo> process(long blockNumber, Transaction transaction) throws IOException;
}
