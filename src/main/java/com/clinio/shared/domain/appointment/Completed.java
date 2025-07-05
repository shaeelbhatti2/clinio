package com.clinio.shared.domain.appointment;

public record Completed() implements AppointmentStatus {

    @Override
    public String label() {
        return "COMPLETED";
    }
}
