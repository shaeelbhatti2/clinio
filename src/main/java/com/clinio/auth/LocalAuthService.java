package com.clinio.auth;

import com.clinio.auth.dto.AuthUserDto;
import com.clinio.shared.persistence.ClinicEntity;
import com.clinio.shared.persistence.ClinicRepository;
import com.clinio.shared.persistence.UserEntity;
import com.clinio.shared.persistence.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LocalAuthService {

    private final ClinicRepository clinicRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalAuthService(
            ClinicRepository clinicRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.clinicRepository = clinicRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Optional<AuthUserDto> authenticate(String clinicSlug, String email, String rawPassword) {
        Optional<ClinicEntity> clinic = clinicRepository.findBySlug(clinicSlug);
        if (clinic.isEmpty()) {
            return Optional.empty();
        }
        Optional<UserEntity> user = userRepository.findByClinicIdAndEmail(clinic.get().getId(), email.toLowerCase());
        if (user.isEmpty() || !user.get().isActive()) {
            return Optional.empty();
        }
        UserEntity entity = user.get();
        if (!passwordEncoder.matches(rawPassword, entity.getPasswordHash())) {
            return Optional.empty();
        }
        return Optional.of(new AuthUserDto(
                entity.getId(),
                entity.getClinicId(),
                entity.getEmail(),
                entity.getDisplayName(),
                entity.getRole()));
    }
}
