package com.clinio.patient.domain;

import com.clinio.shared.domain.ClinicId;
import com.clinio.shared.domain.PatientId;
import com.clinio.shared.domain.PatientStatus;
import java.time.LocalDate;

public record PatientSummary(
        PatientId id,
        ClinicId clinicId,
        String medicalRecordNumber,
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phone,
        PatientStatus status) {
}
