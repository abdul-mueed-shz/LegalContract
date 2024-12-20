/*
 * Copyright IBM Corp. All Rights Reserved.
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.abdul.legalcontract.domain.hyperledger.parser;

import com.google.protobuf.InvalidProtocolBufferException;
import org.hyperledger.fabric.client.identity.Identity;
import org.hyperledger.fabric.protos.common.ChannelHeader;
import org.hyperledger.fabric.protos.common.Payload;
import org.hyperledger.fabric.protos.peer.TxValidationCode;

import java.util.List;

public interface Transaction {
    ChannelHeader getChannelHeader() throws InvalidProtocolBufferException;

    Identity getCreator() throws InvalidProtocolBufferException;

    TxValidationCode getValidationCode();

    boolean isValid();

    List<NamespaceReadWriteSet> getNamespaceReadWriteSets() throws InvalidProtocolBufferException;

    Payload toProto();
}
