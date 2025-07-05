package com.clinio.shared.domain;

import java.util.regex.Pattern;

public record MedicalRecordNumber(String value) {

    private static final Pattern MRN_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");

    public MedicalRecordNumber {
        if (value == null || !MRN_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("invalid medical record number");
        }
    }

    public static MedicalRecordNumber of(String raw) {
        return new MedicalRecordNumber(raw.trim().toUpperCase());
    }
}
