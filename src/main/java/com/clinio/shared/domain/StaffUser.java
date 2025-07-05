package com.clinio.shared.domain;

public record StaffUser(
        UserId id,
        ClinicId clinicId,
        String email,
        String displayName,
        RoleType role,
        boolean active) {
}
