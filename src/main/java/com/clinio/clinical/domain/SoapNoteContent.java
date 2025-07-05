package com.clinio.clinical.domain;

import java.util.List;

public record SoapNoteContent(
        String subjective,
        String objective,
        String assessment,
        String plan) {

    public List<String> sections() {
        return List.of(subjective, objective, assessment, plan);
    }
}
