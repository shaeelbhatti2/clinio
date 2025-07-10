package com.clinio.shared.security;

import com.clinio.auth.CurrentUserService;
import com.clinio.shared.domain.RoleType;
import com.clinio.shared.persistence.UserEntity;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component("clinicSecurity")
public class ClinicSecurityService {

    private static final Set<RoleType> CLINICAL_READ = EnumSet.of(
            RoleType.ADMIN, RoleType.PROVIDER, RoleType.NURSE);
    private static final Set<RoleType> CLINICAL_WRITE = EnumSet.of(
            RoleType.ADMIN, RoleType.PROVIDER);
    private static final Set<RoleType> BILLING_READ = EnumSet.of(
            RoleType.ADMIN, RoleType.BILLING, RoleType.RECEPTIONIST);

    private final CurrentUserService currentUserService;

    public ClinicSecurityService(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    public boolean canAccessClinic(UUID clinicId) {
        return currentUserService.currentUser()
                .map(UserEntity::getClinicId)
                .map(id -> id.equals(clinicId))
                .orElse(false);
    }

    public boolean canReadClinicalNotes() {
        return currentUserService.currentUser()
                .map(UserEntity::getRole)
                .map(CLINICAL_READ::contains)
                .orElse(false);
    }

    public boolean canWriteClinicalNotes() {
        return currentUserService.currentUser()
                .map(UserEntity::getRole)
                .map(CLINICAL_WRITE::contains)
                .orElse(false);
    }

    public boolean canManagePatients() {
        RoleType role = currentUserService.requireRole();
        return role == RoleType.ADMIN
                || role == RoleType.RECEPTIONIST
                || role == RoleType.NURSE
                || role == RoleType.PROVIDER;
    }

    public boolean canViewBilling() {
        return currentUserService.currentUser()
                .map(UserEntity::getRole)
                .map(BILLING_READ::contains)
                .orElse(false);
    }

    public boolean isAdmin() {
        return currentUserService.currentUser()
                .map(user -> user.getRole() == RoleType.ADMIN)
                .orElse(false);
    }
}
