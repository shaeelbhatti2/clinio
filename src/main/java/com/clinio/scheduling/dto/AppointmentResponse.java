package com.clinio.scheduling.dto;

import java.time.Instant;
import java.util.UUID;

public record AppointmentResponse(
        UUID id,
        UUID patientId,
        UUID providerId,
        UUID roomId,
        String appointmentType,
        Instant startAt,
        int durationMinutes,
        String status) {
}
