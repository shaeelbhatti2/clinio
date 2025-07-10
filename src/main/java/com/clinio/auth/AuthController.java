package com.clinio.auth;

import com.clinio.auth.dto.AuthUserDto;
import com.clinio.auth.dto.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final LocalAuthService localAuthService;

    public AuthController(LocalAuthService localAuthService) {
        this.localAuthService = localAuthService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserDto> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {
        return localAuthService.authenticate(request.clinicSlug(), request.email(), request.password())
                .map(user -> {
                    String principal = request.clinicSlug() + ":" + request.email().toLowerCase();
                    var authentication = new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            java.util.List.of(new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                    "ROLE_" + user.role().name())));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    var session = httpRequest.getSession(true);
                    session.setAttribute(
                            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext());
                    return ResponseEntity.ok(user);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        var session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.noContent().build();
    }
}
