package com.abdul.legalcontract.domain.legalcontract.usecase;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.CreateLegalContractUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateLegalContractUseCaseImpl implements CreateLegalContractUseCase {

    private final Contract contract;
    private final ObjectMapper objectMapper;

    /**
     * Submit a transaction synchronously, blocking until it has been committed to
     * the ledger.
     */
    @Override
    public void createLegalContract(LegalContract legalContract)
            throws EndorseException, SubmitException, CommitStatusException, CommitException, JsonProcessingException {
        String participantsJson = objectMapper.writeValueAsString(legalContract.getParticipants());
        contract.submitTransaction(
                "CreateLegalContract",
                legalContract.getId(),
                legalContract.getTitle(),
                legalContract.getDescription(),
                participantsJson
        );
        System.out.println("*** Transaction committed successfully");
    }

}
