package com.abdul.legalcontract.domain.legalcontract.usecase;

import static com.abdul.legalcontract.adapter.in.constants.FabricConstants.DELETE_LEGAL_CONTRACTS;

import com.abdul.legalcontract.domain.legalcontract.port.in.DeleteLegalContractUseCase;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteLegalContractUseCaseImpl implements DeleteLegalContractUseCase {

    private final Contract contract;


    /**
     * Submit a transaction to delete an entry from the ledger.
     */
    @Override
    public void deleteLegalContract(String contractId) throws EndorseException, SubmitException, CommitStatusException, CommitException {

        contract.submitTransaction(DELETE_LEGAL_CONTRACTS, contractId);
    }
}
