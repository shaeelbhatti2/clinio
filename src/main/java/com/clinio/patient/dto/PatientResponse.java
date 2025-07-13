package com.clinio.patient.dto;

import com.clinio.shared.domain.PatientStatus;
import java.time.LocalDate;
import java.util.UUID;

public record PatientResponse(
        UUID id,
        String medicalRecordNumber,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phone,
        String email,
        PatientStatus status) {
}
