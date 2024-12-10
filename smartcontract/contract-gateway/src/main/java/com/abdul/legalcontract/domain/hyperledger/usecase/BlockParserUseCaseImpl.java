/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.abdul.legalcontract.domain.hyperledger.usecase;

import com.abdul.legalcontract.domain.hyperledger.parser.Block;
import com.abdul.legalcontract.domain.hyperledger.parser.ParsedBlock;
import com.abdul.legalcontract.domain.hyperledger.port.in.BlockParserUseCase;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor
public final class BlockParserUseCaseImpl implements BlockParserUseCase {

    public Block parseBlock(final org.hyperledger.fabric.protos.common.Block block) {
        return new ParsedBlock(block);
    }
}
