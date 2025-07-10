package com.clinio.shared.web;

import com.clinio.auth.CurrentUserService;
import com.clinio.auth.dto.AuthUserDto;
import com.clinio.shared.persistence.UserEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/me")
public class CurrentUserController {

    private final CurrentUserService currentUserService;

    public CurrentUserController(CurrentUserService currentUserService) {
        this.currentUserService = currentUserService;
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AuthUserDto> me() {
        return currentUserService.currentUser()
                .map(this::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private AuthUserDto toDto(UserEntity entity) {
        return new AuthUserDto(
                entity.getId(),
                entity.getClinicId(),
                entity.getEmail(),
                entity.getDisplayName(),
                entity.getRole());
    }
}
