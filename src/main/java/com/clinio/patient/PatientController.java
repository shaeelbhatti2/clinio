package com.clinio.patient;

import com.clinio.auth.CurrentUserService;
import com.clinio.patient.dto.CreatePatientRequest;
import com.clinio.patient.dto.PatientResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/patients")
public class PatientController {

    private final PatientService patientService;
    private final CurrentUserService currentUserService;

    public PatientController(PatientService patientService, CurrentUserService currentUserService) {
        this.patientService = patientService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public PatientResponse create(@Valid @RequestBody CreatePatientRequest request) {
        return patientService.register(request);
    }

    @GetMapping
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<PatientResponse> search(@RequestParam(name = "q", required = false) String query) {
        return patientService.search(query);
    }

    @GetMapping("/{patientId}")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public PatientResponse get(@PathVariable UUID patientId) {
        UUID clinicId = currentUserService.requireClinicId();
        return patientService.get(clinicId, patientId);
    }
}
