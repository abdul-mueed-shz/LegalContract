package com.abdul.legalcontract.domain.legalcontract.port.in;

import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.SubmitException;

public interface DeleteLegalContractUseCase {

    void deleteLegalContract(String contractId)
            throws EndorseException, SubmitException, CommitStatusException, CommitException;
}
