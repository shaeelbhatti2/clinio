package com.clinio.scheduling.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

public record CreateScheduleBlockRequest(
        @NotNull UUID providerId,
        @NotNull DayOfWeek dayOfWeek,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        LocalTime lunchStart,
        LocalTime lunchEnd) {
}
