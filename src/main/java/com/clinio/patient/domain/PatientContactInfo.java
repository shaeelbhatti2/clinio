package com.clinio.patient.domain;

public record PatientContactInfo(
        String emergencyName,
        String emergencyPhone,
        String relationship,
        String insuranceCarrier,
        String insurancePolicyNumber) {
}
