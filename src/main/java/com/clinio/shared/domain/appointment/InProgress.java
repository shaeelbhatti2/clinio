package com.clinio.shared.domain.appointment;

public record InProgress() implements AppointmentStatus {

    @Override
    public String label() {
        return "IN_PROGRESS";
    }
}
