package com.clinio.scheduling;

import com.clinio.scheduling.dto.AppointmentResponse;
import com.clinio.scheduling.dto.WaitingRoomEntry;
import java.util.List;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/check-in")
public class CheckInController {

    private final CheckInService checkInService;
    private final WaitingRoomStreamService waitingRoomStreamService;

    public CheckInController(CheckInService checkInService, WaitingRoomStreamService waitingRoomStreamService) {
        this.checkInService = checkInService;
        this.waitingRoomStreamService = waitingRoomStreamService;
    }

    @PostMapping("/appointments/{appointmentId}")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse checkIn(@PathVariable UUID appointmentId) {
        AppointmentResponse response = checkInService.checkIn(appointmentId);
        waitingRoomStreamService.broadcastUpdate();
        return response;
    }

    @PostMapping("/appointments/{appointmentId}/start")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse start(@PathVariable UUID appointmentId) {
        return checkInService.startVisit(appointmentId);
    }

    @PostMapping("/appointments/{appointmentId}/complete")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public AppointmentResponse complete(
            @PathVariable UUID appointmentId,
            @RequestParam(defaultValue = "false") boolean noShow) {
        return checkInService.completeVisit(appointmentId, noShow);
    }

    @GetMapping("/waiting-room")
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public List<WaitingRoomEntry> queue() {
        return checkInService.waitingRoomQueue();
    }

    @GetMapping(value = "/waiting-room/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @PreAuthorize("@clinicSecurity.canManagePatients()")
    public SseEmitter stream() {
        return waitingRoomStreamService.subscribe();
    }
}
