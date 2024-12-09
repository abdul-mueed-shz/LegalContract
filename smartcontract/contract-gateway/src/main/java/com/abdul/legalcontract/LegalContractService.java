package com.abdul.legalcontract;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class LegalContractService {
    private final GetLegalContractByIdUseCase getLegalContractByIdUseCase;
    private final GetAllLegalContractsUseCase getAllLegalContractsUseCase;
    private final CreateLegalContractUseCase createLegalContractUseCase;
    private final UpdateLegalContractUseCase updateLegalContractUseCase;
    private final DeleteLegalContractUseCase deleteLegalContractUseCase;
    private final InitLegalContractLedgerUseCase initLegalContractLedgerUseCase;

    @PostConstruct
    public void exec() throws GatewayException, IOException, CommitException {
        LegalContract legalContract = new LegalContract();
        legalContract.setId("RandomID");
        legalContract.setTitle("Project Alpha");
        legalContract.setDescription("Initial phase of the project");
        legalContract.setParticipants(Arrays.asList("Alice", "Bob"));

        createLegalContractUseCase.createLegalContract(legalContract);
        System.out.println(getAllLegalContractsUseCase.getAllLegalContracts());

        System.out.println(getLegalContractByIdUseCase.readLegalContractById("RandomID"));


        LegalContract legalContract2 = new LegalContract();
        legalContract2.setId("RandomID");
        legalContract2.setTitle("Project Alpha");
        legalContract2.setDescription("Secondd phase of the project");
        legalContract2.setParticipants(Arrays.asList("Moeed", "Bob2"));
        updateLegalContractUseCase.updateLegalContract(legalContract2);
        System.out.println(getAllLegalContractsUseCase.getAllLegalContracts());

        deleteLegalContractUseCase.deleteLegalContract("RandomID");
        System.out.println(getAllLegalContractsUseCase.getAllLegalContracts());
    }
}
