package com.clinio.scheduling.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderScheduleRepository extends JpaRepository<ProviderScheduleEntity, UUID> {

    List<ProviderScheduleEntity> findByClinicIdAndProviderId(UUID clinicId, UUID providerId);
}
