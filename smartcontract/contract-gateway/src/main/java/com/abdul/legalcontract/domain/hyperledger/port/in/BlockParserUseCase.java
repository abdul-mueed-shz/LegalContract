package com.abdul.legalcontract.domain.hyperledger.port.in;

import com.abdul.legalcontract.domain.hyperledger.parser.Block;

public interface BlockParserUseCase {

    Block parseBlock(final org.hyperledger.fabric.protos.common.Block block);
}
