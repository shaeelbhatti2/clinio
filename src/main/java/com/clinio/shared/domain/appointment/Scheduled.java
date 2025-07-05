package com.clinio.shared.domain.appointment;

public record Scheduled() implements AppointmentStatus {

    @Override
    public String label() {
        return "SCHEDULED";
    }
}
