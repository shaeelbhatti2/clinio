package com.clinio.patient.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PatientRepository extends JpaRepository<PatientEntity, UUID> {

    Optional<PatientEntity> findByClinicIdAndMedicalRecordNumber(UUID clinicId, String medicalRecordNumber);

    @Query("""
            select p from PatientEntity p
            where p.clinicId = :clinicId
              and (lower(p.lastName) like lower(concat('%', :query, '%'))
                or lower(p.firstName) like lower(concat('%', :query, '%'))
                or p.phone like concat('%', :query, '%'))
            order by p.lastName, p.firstName
            """)
    List<PatientEntity> search(@Param("clinicId") UUID clinicId, @Param("query") String query);

    List<PatientEntity> findByClinicIdAndLastNameIgnoreCaseAndDateOfBirth(
            UUID clinicId, String lastName, java.time.LocalDate dateOfBirth);
}
