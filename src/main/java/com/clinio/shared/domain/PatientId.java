package com.clinio.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record PatientId(UUID value) {

    public PatientId {
        Objects.requireNonNull(value, "patient id required");
    }

    public static PatientId generate() {
        return new PatientId(UUID.randomUUID());
    }

    public static PatientId of(String raw) {
        return new PatientId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
