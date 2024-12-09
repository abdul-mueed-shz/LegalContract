package com.abdul.legalcontract.domain.legalcontract.port.in;

import com.abdul.legalcontract.domain.legalcontract.model.LegalContract;
import org.hyperledger.fabric.client.GatewayException;

import java.io.IOException;

public interface GetLegalContractByIdUseCase {
    LegalContract readLegalContractById(String contractId) throws GatewayException, IOException;
}
