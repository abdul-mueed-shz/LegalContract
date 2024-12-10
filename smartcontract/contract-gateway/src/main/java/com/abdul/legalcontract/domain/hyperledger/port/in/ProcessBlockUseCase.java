package com.abdul.legalcontract.domain.hyperledger.port.in;

import com.abdul.legalcontract.domain.hyperledger.parser.Block;

public interface ProcessBlockUseCase {

    void process(Block block);
}
