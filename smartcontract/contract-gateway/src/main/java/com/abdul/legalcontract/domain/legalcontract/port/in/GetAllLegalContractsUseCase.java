package com.abdul.legalcontract.domain.legalcontract.port.in;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import org.hyperledger.fabric.client.GatewayException;

import java.io.IOException;
import java.util.List;

public interface GetAllLegalContractsUseCase {
    List<LegalContract> getAllLegalContracts() throws GatewayException, IOException;
}
