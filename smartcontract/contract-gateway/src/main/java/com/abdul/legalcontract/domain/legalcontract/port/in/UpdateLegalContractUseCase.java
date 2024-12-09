package com.abdul.legalcontract.domain.legalcontract.port.in;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.CommitStatusException;
import org.hyperledger.fabric.client.EndorseException;
import org.hyperledger.fabric.client.SubmitException;

public interface UpdateLegalContractUseCase {

    void updateLegalContract(LegalContract legalContract)
            throws EndorseException, CommitException, SubmitException, CommitStatusException, JsonProcessingException;
}
