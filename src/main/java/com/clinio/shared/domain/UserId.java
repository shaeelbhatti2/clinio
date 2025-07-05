package com.clinio.shared.domain;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    public UserId {
        Objects.requireNonNull(value, "user id required");
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(String raw) {
        return new UserId(UUID.fromString(raw));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
