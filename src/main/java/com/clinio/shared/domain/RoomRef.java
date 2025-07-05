package com.clinio.shared.domain;

import java.util.UUID;

public record RoomRef(UUID id, ClinicId clinicId, String name, boolean active) {
}
