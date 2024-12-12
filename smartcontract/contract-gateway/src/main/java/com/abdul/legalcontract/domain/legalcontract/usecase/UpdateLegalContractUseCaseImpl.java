package com.abdul.legalcontract.domain.legalcontract.usecase;

import static com.abdul.legalcontract.adapter.in.constants.FabricConstants.UPDATE_LEGAL_CONTRACT;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.UpdateLegalContractUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UpdateLegalContractUseCaseImpl implements UpdateLegalContractUseCase {

    private final Contract contract;
    private final ObjectMapper objectMapper;

    /**
     * Submit a transaction synchronously to update an entry.
     */
    @Override
    public void updateLegalContract(LegalContract legalContract)
            throws EndorseException, CommitException, SubmitException, CommitStatusException, JsonProcessingException {
        String participantsJson = objectMapper.writeValueAsString(legalContract.getParticipants());
        contract.submitTransaction(
                UPDATE_LEGAL_CONTRACT,
                legalContract.getId(),
                legalContract.getTitle(),
                legalContract.getDescription(),
                participantsJson);
    }

}
