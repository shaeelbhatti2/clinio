package com.clinio.shared.seed;

import com.clinio.shared.domain.RoleType;
import com.clinio.shared.persistence.ClinicEntity;
import com.clinio.shared.persistence.ClinicRepository;
import com.clinio.shared.persistence.UserEntity;
import com.clinio.shared.persistence.UserRepository;
import java.util.UUID;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class DevSeedConfig {

    @Bean
    CommandLineRunner seedDemoClinic(
            ClinicRepository clinicRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        return args -> {
            if (clinicRepository.count() > 0) {
                return;
            }
            UUID clinicId = UUID.randomUUID();
            clinicRepository.save(new ClinicEntity(clinicId, "Demo Clinic", "demo", "America/Chicago"));
            userRepository.save(new UserEntity(
                    UUID.randomUUID(),
                    clinicId,
                    "admin@demo.clinio",
                    passwordEncoder.encode("changeme"),
                    "Demo Admin",
                    RoleType.ADMIN));
        };
    }
}
