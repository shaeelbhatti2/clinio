package com.clinio.shared.domain.appointment;

public final class AppointmentStatuses {

    public static final Scheduled SCHEDULED = new Scheduled();
    public static final CheckedIn CHECKED_IN = new CheckedIn();
    public static final InProgress IN_PROGRESS = new InProgress();
    public static final Completed COMPLETED = new Completed();
    public static final Cancelled CANCELLED = new Cancelled();
    public static final NoShow NO_SHOW = new NoShow();

    private AppointmentStatuses() {
    }

    public static AppointmentStatus fromLabel(String label) {
        return switch (label) {
            case "SCHEDULED" -> SCHEDULED;
            case "CHECKED_IN" -> CHECKED_IN;
            case "IN_PROGRESS" -> IN_PROGRESS;
            case "COMPLETED" -> COMPLETED;
            case "CANCELLED" -> CANCELLED;
            case "NO_SHOW" -> NO_SHOW;
            default -> throw new IllegalArgumentException("unknown appointment status");
        };
    }
}
