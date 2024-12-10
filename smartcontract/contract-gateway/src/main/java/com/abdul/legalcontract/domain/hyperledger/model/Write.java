package com.abdul.legalcontract.domain.hyperledger.model;

import lombok.AccessLevel;
import lombok.Getter;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KVWrite;

import java.nio.charset.StandardCharsets;

@Getter
public class Write {
    private final String channelName;
    private final String namespace;
    private final String key;
    private final boolean isDelete;

    @Getter(AccessLevel.NONE)
    private final String value; // Store as String for readability when serialized to JSON.

    public Write(final String channelName, final String namespace, final KVWrite write) {
        this.channelName = channelName;
        this.namespace = namespace;
        this.key = write.getKey();
        this.isDelete = write.getIsDelete();
        this.value = write.getValue().toString(StandardCharsets.UTF_8);
    }


    /**
     * If {@link #isDelete()}` is {@code false}, the value written to the key; otherwise ignored.
     *
     * @return A ledger value.
     */
    public byte[] getValue() {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
