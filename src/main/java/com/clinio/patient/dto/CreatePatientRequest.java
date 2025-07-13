package com.clinio.patient.dto;

import com.clinio.shared.domain.PatientStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CreatePatientRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotNull LocalDate dateOfBirth,
        @NotBlank String phone,
        String email,
        String emergencyName,
        String emergencyPhone,
        String emergencyRelationship,
        String insuranceCarrier,
        String insurancePolicyNumber,
        PatientStatus status) {
}
