package com.clinio.shared.domain.appointment;

public sealed interface AppointmentStatus permits
        Scheduled,
        CheckedIn,
        InProgress,
        Completed,
        Cancelled,
        NoShow {

    String label();
}
