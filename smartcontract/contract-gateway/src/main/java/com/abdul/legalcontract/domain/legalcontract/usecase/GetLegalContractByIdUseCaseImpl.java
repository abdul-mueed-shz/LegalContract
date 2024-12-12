package com.abdul.legalcontract.domain.legalcontract.usecase;

import static com.abdul.legalcontract.adapter.in.constants.FabricConstants.READ_LEGAL_CONTRACT;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.GetLegalContractByIdUseCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GetLegalContractByIdUseCaseImpl implements GetLegalContractByIdUseCase {

    private final Contract contract;
    private final ObjectMapper objectMapper;

    /**
     * Evaluate a transaction to query an entry by ID.
     */
    @Override
    public LegalContract readLegalContractById(String contractId) throws GatewayException, IOException {
        byte[] evaluateResult = contract.evaluateTransaction(READ_LEGAL_CONTRACT, contractId);
        return objectMapper.readValue(evaluateResult, new TypeReference<>() {
        });
    }
}
