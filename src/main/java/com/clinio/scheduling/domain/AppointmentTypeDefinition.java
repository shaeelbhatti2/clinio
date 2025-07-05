package com.clinio.scheduling.domain;

import java.time.Duration;

public record AppointmentTypeDefinition(
        String code,
        String label,
        Duration defaultDuration,
        boolean newPatient) {
}
