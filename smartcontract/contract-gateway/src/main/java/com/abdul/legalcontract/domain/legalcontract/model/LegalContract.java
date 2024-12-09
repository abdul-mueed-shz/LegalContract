package com.abdul.legalcontract.domain.legalcontract.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LegalContract {
    @JsonProperty("ID")
    private String id;

    @JsonProperty("Title")
    private String title;

    @JsonProperty("Description")
    private String description;

    @JsonProperty("Participants")
    private List<String> participants;
}
