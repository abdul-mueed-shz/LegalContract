package com.abdul.legalcontract.domain.legalcontract.usecase;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.CreateLegalContractUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateLegalContractUseCaseImpl implements CreateLegalContractUseCase {

    private final Contract contract;
    private final ObjectMapper objectMapper;

    /**
     * Submit a transaction synchronously, blocking until it has been committed to
     * the ledger.
     */
    @Override
    public String createLegalContract(LegalContract legalContract)
            throws EndorseException, SubmitException, CommitStatusException, CommitException, JsonProcessingException {
        String participantsJson = objectMapper.writeValueAsString(legalContract.getParticipants());
        String uuId = UUID.randomUUID().toString();
        contract.submitTransaction(
                "CreateLegalContract",
                uuId,
                legalContract.getTitle(),
                legalContract.getDescription(),
                participantsJson
        );
        log.info("Transaction committed successfully with uuid: {}", uuId);
        return uuId;
    }

}
