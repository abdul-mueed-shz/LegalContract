package com.abdul.legalcontract.domain.hyperledger.model;

import lombok.Getter;
import lombok.Setter;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KVWrite;

import java.nio.charset.StandardCharsets;

@Getter
@Setter
public final class Write {
    private final String channelName;
    private final String namespace;
    private final String key;
    private final boolean isDelete;
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
    public byte[] getValueByes() {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
