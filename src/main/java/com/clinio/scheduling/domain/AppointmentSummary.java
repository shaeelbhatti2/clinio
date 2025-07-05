package com.clinio.scheduling.domain;

import com.clinio.shared.domain.ClinicId;
import com.clinio.shared.domain.PatientId;
import com.clinio.shared.domain.TimeSlot;
import com.clinio.shared.domain.UserId;
import com.clinio.shared.domain.appointment.AppointmentStatus;
import java.util.UUID;

public record AppointmentSummary(
        UUID id,
        ClinicId clinicId,
        PatientId patientId,
        UserId providerId,
        UUID roomId,
        String appointmentType,
        TimeSlot slot,
        AppointmentStatus status) {
}
