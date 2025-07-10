package com.clinio.auth;

import com.clinio.shared.domain.RoleType;
import com.clinio.shared.persistence.ClinicContext;
import com.clinio.shared.persistence.UserEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final ClinioUserDetailsService userDetailsService;
    private final ClinicContext clinicContext;

    public CurrentUserService(ClinioUserDetailsService userDetailsService, ClinicContext clinicContext) {
        this.userDetailsService = userDetailsService;
        this.clinicContext = clinicContext;
    }

    public Optional<UserEntity> currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        String principal = authentication.getName();
        String[] parts = principal.split(":", 2);
        if (parts.length != 2) {
            return Optional.empty();
        }
        return userDetailsService.findEntity(parts[0], parts[1]);
    }

    public UUID requireClinicId() {
        return currentUser()
                .map(UserEntity::getClinicId)
                .map(clinicId -> {
                    clinicContext.setClinicId(clinicId);
                    return clinicId;
                })
                .orElseThrow(() -> new IllegalStateException("authenticated clinic required"));
    }

    public RoleType requireRole() {
        return currentUser()
                .map(UserEntity::getRole)
                .orElseThrow(() -> new IllegalStateException("authenticated role required"));
    }
}
