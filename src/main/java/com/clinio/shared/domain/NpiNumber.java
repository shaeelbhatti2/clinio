package com.clinio.shared.domain;

import java.util.regex.Pattern;

public record NpiNumber(String value) {

    private static final Pattern NPI_PATTERN = Pattern.compile("^\\d{10}$");

    public NpiNumber {
        if (value == null || !NPI_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("invalid NPI number");
        }
    }

    public static NpiNumber of(String raw) {
        return new NpiNumber(raw.trim());
    }
}
