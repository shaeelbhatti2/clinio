package com.clinio.shared.domain;

import java.time.ZoneId;

public record ClinicProfile(
        ClinicId id,
        String name,
        String slug,
        ZoneId timezone,
        String addressLine1,
        String city,
        String state,
        String postalCode) {
}
