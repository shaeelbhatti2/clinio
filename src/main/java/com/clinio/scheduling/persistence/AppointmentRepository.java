package com.clinio.scheduling.persistence;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<AppointmentEntity, UUID> {

    List<AppointmentEntity> findByClinicIdAndProviderIdAndStartAtLessThanAndStatusNot(
            UUID clinicId, UUID providerId, Instant endAt, String status);

    List<AppointmentEntity> findByClinicIdAndRoomIdAndStartAtLessThanAndStatusNot(
            UUID clinicId, UUID roomId, Instant endAt, String status);

    List<AppointmentEntity> findByClinicIdAndStatusOrderByStartAtAsc(UUID clinicId, String status);
}
