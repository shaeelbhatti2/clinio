package com.clinio.scheduling.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record BookAppointmentRequest(
        @NotNull UUID patientId,
        @NotNull UUID providerId,
        UUID roomId,
        @NotBlank String appointmentType,
        @NotNull Instant startAt,
        @Min(5) int durationMinutes) {
}
