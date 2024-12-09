package com.abdul.legalcontract.domain.legalcontract.port.in;

import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.SubmitException;

public interface InitLegalContractLedgerUseCase {

    /**
     * This type of transaction would typically only be run once by an application
     * the first time it was started after its initial deployment. A new version of
     * the chaincode deployed later would likely not need to run an "init" function.
     */
    void initLedger() throws EndorseException, SubmitException, CommitStatusException, CommitException;
}
