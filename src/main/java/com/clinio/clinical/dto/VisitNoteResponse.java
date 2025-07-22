package com.clinio.clinical.dto;

import java.time.Instant;
import java.util.UUID;

public record VisitNoteResponse(
        UUID id,
        UUID appointmentId,
        UUID patientId,
        UUID providerId,
        String templateCode,
        String subjective,
        String objective,
        String assessment,
        String plan,
        boolean signed,
        Instant signedAt) {
}
