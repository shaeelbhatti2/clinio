package com.clinio.shared.domain;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public record TimeSlot(LocalDateTime start, Duration duration) {

    public TimeSlot {
        Objects.requireNonNull(start, "start required");
        Objects.requireNonNull(duration, "duration required");
        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("duration must be positive");
        }
    }

    public LocalDateTime end() {
        return start.plus(duration);
    }

    public boolean overlaps(TimeSlot other) {
        return start.isBefore(other.end()) && other.start.isBefore(end());
    }
}
