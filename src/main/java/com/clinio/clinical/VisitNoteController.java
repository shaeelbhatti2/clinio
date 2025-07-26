package com.clinio.clinical;

import com.clinio.clinical.dto.SaveVisitNoteRequest;
import com.clinio.clinical.dto.VisitNoteResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clinical/notes")
public class VisitNoteController {

    private final VisitNoteService visitNoteService;

    public VisitNoteController(VisitNoteService visitNoteService) {
        this.visitNoteService = visitNoteService;
    }

    @PostMapping("/from-appointment/{appointmentId}")
    public VisitNoteResponse start(@PathVariable UUID appointmentId) {
        return visitNoteService.startFromAppointment(appointmentId);
    }

    @PutMapping("/{noteId}")
    public VisitNoteResponse save(@PathVariable UUID noteId, @Valid @RequestBody SaveVisitNoteRequest request) {
        return visitNoteService.saveDraft(noteId, request);
    }

    @PostMapping("/{noteId}/sign")
    public VisitNoteResponse sign(@PathVariable UUID noteId) {
        return visitNoteService.sign(noteId);
    }

    @GetMapping("/patients/{patientId}")
    public List<VisitNoteResponse> list(@PathVariable UUID patientId) {
        return visitNoteService.listForPatient(patientId);
    }
}
