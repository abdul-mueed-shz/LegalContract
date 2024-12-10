package com.abdul.legalcontract.adapter.in.web;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import com.abdul.legalcontract.domain.legalcontract.port.in.*;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.client.CommitException;
import org.hyperledger.fabric.client.GatewayException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/legal-contracts")
@RequiredArgsConstructor
public class LegalContractController {
    private final GetAllLegalContractsUseCase getAllLegalContractsUseCase;
    private final GetLegalContractByIdUseCase getLegalContractByIdUseCase;
    private final CreateLegalContractUseCase createLegalContractUseCase;
    private final UpdateLegalContractUseCase updateLegalContractUseCase;
    private final DeleteLegalContractUseCase deleteLegalContractUseCase;

    @GetMapping
    public ResponseEntity<List<LegalContract>> getLegalContracts() throws GatewayException, IOException {
        return ResponseEntity.ok(
                getAllLegalContractsUseCase.getAllLegalContracts()
        );
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<LegalContract> getLegalContract(@PathVariable("contractId") String contractId)
            throws GatewayException, IOException {
        return ResponseEntity.ok(
                getLegalContractByIdUseCase.readLegalContractById(contractId)
        );
    }


    @DeleteMapping("/{contractId}")
    public ResponseEntity<Void> deleteLegalContract(@PathVariable("contractId") String contractId)
            throws GatewayException, CommitException {
        deleteLegalContractUseCase.deleteLegalContract(contractId);
        return ResponseEntity.ok().build();
    }


    @PutMapping
    public ResponseEntity<Void> updateLegalContract(@RequestBody LegalContract legalContract)
            throws GatewayException, IOException, CommitException {
        updateLegalContractUseCase.updateLegalContract(legalContract);
        return ResponseEntity.accepted().build();
    }


    @PostMapping
    public ResponseEntity<Void> createLegalContract(@RequestBody LegalContract legalContract)
            throws GatewayException, IOException, CommitException {
        createLegalContractUseCase.createLegalContract(legalContract);
        return ResponseEntity.accepted().build();
    }
}
