package com.clinio.clinical.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VisitNoteRepository extends JpaRepository<VisitNoteEntity, UUID> {

    List<VisitNoteEntity> findByClinicIdAndPatientIdOrderByCreatedAtDesc(UUID clinicId, UUID patientId);

    List<VisitNoteEntity> findByClinicIdAndAppointmentId(UUID clinicId, UUID appointmentId);
}
