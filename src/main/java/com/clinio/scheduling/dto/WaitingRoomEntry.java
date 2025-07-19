package com.clinio.scheduling.dto;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public record WaitingRoomEntry(
        UUID appointmentId,
        UUID patientId,
        UUID providerId,
        Instant scheduledStart,
        Duration waitDuration) {
}
