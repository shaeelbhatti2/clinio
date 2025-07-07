package com.clinio.shared.persistence;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicRepository extends JpaRepository<ClinicEntity, UUID> {

    Optional<ClinicEntity> findBySlug(String slug);
}
