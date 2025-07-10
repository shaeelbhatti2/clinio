package com.clinio.auth.dto;

import com.clinio.shared.domain.RoleType;
import java.util.UUID;

public record AuthUserDto(
        UUID id,
        UUID clinicId,
        String email,
        String displayName,
        RoleType role) {
}
