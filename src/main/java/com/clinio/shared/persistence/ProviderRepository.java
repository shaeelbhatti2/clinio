package com.clinio.shared.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProviderRepository extends JpaRepository<ProviderEntity, UUID> {

    List<ProviderEntity> findByClinicId(UUID clinicId);

    List<ProviderEntity> findByClinicIdAndUserId(UUID clinicId, UUID userId);
}
