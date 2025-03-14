package com.playtomic.tests.wallet.infrastructure.service;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Refund {

    @NonNull
    private String id;

    @JsonCreator
    public Refund(@JsonProperty(value = "id", required = true) String id) {
        this.id = id;
    }
}
