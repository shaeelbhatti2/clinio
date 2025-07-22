package com.clinio.clinical.dto;

import jakarta.validation.constraints.NotBlank;

public record SaveVisitNoteRequest(
        @NotBlank String subjective,
        String objective,
        String assessment,
        String plan) {
}
