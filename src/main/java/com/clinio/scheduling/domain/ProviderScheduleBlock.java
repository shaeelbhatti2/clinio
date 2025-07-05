package com.clinio.scheduling.domain;

import com.clinio.shared.domain.TimeSlot;
import com.clinio.shared.domain.UserId;
import java.time.DayOfWeek;
import java.time.LocalTime;

public record ProviderScheduleBlock(
        UserId providerId,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime,
        LocalTime lunchStart,
        LocalTime lunchEnd) {

    public boolean covers(TimeSlot slot) {
        if (slot.start().getDayOfWeek() != dayOfWeek) {
            return false;
        }
        LocalTime slotStart = slot.start().toLocalTime();
        LocalTime slotEnd = slot.end().toLocalTime();
        if (slotStart.isBefore(startTime) || slotEnd.isAfter(endTime)) {
            return false;
        }
        if (lunchStart != null && lunchEnd != null) {
            boolean overlapsLunch = slotStart.isBefore(lunchEnd) && slotEnd.isAfter(lunchStart);
            if (overlapsLunch) {
                return false;
            }
        }
        return true;
    }
}
