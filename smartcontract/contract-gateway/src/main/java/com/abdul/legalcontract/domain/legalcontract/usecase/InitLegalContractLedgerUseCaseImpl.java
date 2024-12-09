package com.abdul.legalcontract.domain.legalcontract.usecase;

import com.abdul.legalcontract.domain.legalcontract.port.in.InitLegalContractLedgerUseCase;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitLegalContractLedgerUseCaseImpl implements InitLegalContractLedgerUseCase {

    private final Contract contract;

    public void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException {
        System.out.println("\n--> Submit Transaction: InitLedger, function creates the initial set of entries on the ledger");
        contract.submitTransaction("InitLedger");
        System.out.println("*** Transaction committed successfully");
    }
}
