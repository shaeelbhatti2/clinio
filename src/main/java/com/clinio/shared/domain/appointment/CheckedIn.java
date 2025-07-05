package com.clinio.shared.domain.appointment;

public record CheckedIn() implements AppointmentStatus {

    @Override
    public String label() {
        return "CHECKED_IN";
    }
}
