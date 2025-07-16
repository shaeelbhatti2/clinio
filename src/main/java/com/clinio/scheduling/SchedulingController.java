package com.clinio.scheduling;

import com.clinio.scheduling.dto.AppointmentResponse;
import com.clinio.scheduling.dto.BookAppointmentRequest;
import com.clinio.scheduling.dto.CreateScheduleBlockRequest;
import com.clinio.scheduling.persistence.ProviderScheduleEntity;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/scheduling")
public class SchedulingController {

    private final SchedulingService schedulingService;

    public SchedulingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @PostMapping("/providers/{providerId}/blocks")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public ProviderScheduleEntity addBlock(
            @PathVariable UUID providerId,
            @Valid @RequestBody CreateScheduleBlockRequest request) {
        CreateScheduleBlockRequest normalized = new CreateScheduleBlockRequest(
                providerId,
                request.dayOfWeek(),
                request.startTime(),
                request.endTime(),
                request.lunchStart(),
                request.lunchEnd());
        return schedulingService.addScheduleBlock(normalized);
    }

    @GetMapping("/providers/{providerId}/blocks")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<ProviderScheduleEntity> listBlocks(@PathVariable UUID providerId) {
        return schedulingService.listProviderSchedule(providerId);
    }

    @PostMapping("/appointments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse book(@Valid @RequestBody BookAppointmentRequest request) {
        return schedulingService.book(request);
    }

    @GetMapping("/providers/{providerId}/appointments")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<AppointmentResponse> listAppointments(@PathVariable UUID providerId) {
        return schedulingService.listByProvider(providerId);
    }
}
