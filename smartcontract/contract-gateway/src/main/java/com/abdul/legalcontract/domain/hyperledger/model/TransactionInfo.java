package com.abdul.legalcontract.domain.hyperledger.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TransactionInfo {

    private Long blockNumber;
    private String transactionId;
    private String contractName;
    private String channelName;

}
