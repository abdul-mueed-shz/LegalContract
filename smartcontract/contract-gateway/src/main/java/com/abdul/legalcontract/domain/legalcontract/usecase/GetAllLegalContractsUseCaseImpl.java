package com.abdul.legalcontract.domain.legalcontract.usecase;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.GetAllLegalContractsUseCase;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.Contract;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllLegalContractsUseCaseImpl implements GetAllLegalContractsUseCase {

    private final Contract contract;
    private final ObjectMapper objectMapper;

    /**
     * Evaluate a transaction to query ledger state.
     */
    @Override
    public List<LegalContract> getAllLegalContracts() throws GatewayException, IOException {
        byte[] result = contract.evaluateTransaction("GetAllAssets");

        if (result.length == 0) {
            return new ArrayList<>();
        }

        return objectMapper.readValue(result, new TypeReference<>() {
        });
    }
}
