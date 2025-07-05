package com.clinio.clinical.domain;

import com.clinio.shared.domain.PatientId;
import com.clinio.shared.domain.UserId;
import java.time.Instant;
import java.util.UUID;

public record VisitNoteSummary(
        UUID id,
        UUID appointmentId,
        PatientId patientId,
        UserId providerId,
        String templateCode,
        boolean signed,
        Instant signedAt) {
}
