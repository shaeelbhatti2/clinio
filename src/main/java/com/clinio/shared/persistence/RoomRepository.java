package com.clinio.shared.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<RoomEntity, UUID> {

    List<RoomEntity> findByClinicIdAndActiveTrue(UUID clinicId);
}
