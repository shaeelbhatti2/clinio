package com.clinio.shared.persistence;

import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class ClinicContext {

    private static final ThreadLocal<UUID> CURRENT = new ThreadLocal<>();

    public void setClinicId(UUID clinicId) {
        CURRENT.set(clinicId);
    }

    public UUID requireClinicId() {
        UUID clinicId = CURRENT.get();
        if (clinicId == null) {
            throw new IllegalStateException("clinic context not set");
        }
        return clinicId;
    }

    public void clear() {
        CURRENT.remove();
    }
}
