package com.clinio.shared.domain.appointment;

public record Cancelled() implements AppointmentStatus {

    @Override
    public String label() {
        return "CANCELLED";
    }
}
