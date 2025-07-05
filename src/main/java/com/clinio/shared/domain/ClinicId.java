package com.clinio.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record ClinicId(UUID value) {

    public ClinicId {
        Objects.requireNonNull(value, "clinic id required");
    }

    public static ClinicId generate() {
        return new ClinicId(UUID.randomUUID());
    }

    public static ClinicId of(String raw) {
        return new ClinicId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
