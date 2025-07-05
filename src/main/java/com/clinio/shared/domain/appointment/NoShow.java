package com.clinio.shared.domain.appointment;

public record NoShow() implements AppointmentStatus {

    @Override
    public String label() {
        return "NO_SHOW";
    }
}
