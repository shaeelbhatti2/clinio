package com.clinio.auth;

import com.clinio.shared.persistence.ClinicRepository;
import com.clinio.shared.persistence.UserEntity;
import com.clinio.shared.persistence.UserRepository;
import java.util.Optional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ClinioUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ClinicRepository clinicRepository;

    public ClinioUserDetailsService(UserRepository userRepository, ClinicRepository clinicRepository) {
        this.userRepository = userRepository;
        this.clinicRepository = clinicRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String[] parts = username.split(":", 2);
        if (parts.length != 2) {
            throw new UsernameNotFoundException("invalid username format");
        }
        String clinicSlug = parts[0];
        String email = parts[1];
        var clinic = clinicRepository.findBySlug(clinicSlug)
                .orElseThrow(() -> new UsernameNotFoundException("clinic not found"));
        UserEntity entity = userRepository.findByClinicIdAndEmail(clinic.getId(), email.toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        if (!entity.isActive()) {
            throw new UsernameNotFoundException("user inactive");
        }
        return User.builder()
                .username(username)
                .password(entity.getPasswordHash())
                .authorities(new SimpleGrantedAuthority("ROLE_" + entity.getRole().name()))
                .build();
    }

    public Optional<UserEntity> findEntity(String clinicSlug, String email) {
        return clinicRepository.findBySlug(clinicSlug)
                .flatMap(clinic -> userRepository.findByClinicIdAndEmail(clinic.getId(), email.toLowerCase()));
    }
}
