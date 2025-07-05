package com.clinio.patient.domain;

import com.clinio.shared.domain.PatientStatus;
import java.time.LocalDate;

public record PatientRegistration(
        String firstName,
        String lastName,
        LocalDate dateOfBirth,
        String phone,
        String email,
        String insuranceCarrier,
        String insurancePolicyNumber,
        PatientStatus status) {
}
