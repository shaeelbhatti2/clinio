package com.clinio.billing;

import com.clinio.auth.CurrentUserService;
import com.clinio.billing.domain.InvoiceStatus;
import com.clinio.billing.dto.CreateInvoiceRequest;
import com.clinio.billing.dto.InvoiceLineResponse;
import com.clinio.billing.dto.InvoiceResponse;
import com.clinio.billing.persistence.InvoiceEntity;
import com.clinio.billing.persistence.InvoiceLineEntity;
import com.clinio.billing.persistence.InvoiceRepository;
import com.clinio.shared.domain.DomainValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingService {

    private final InvoiceRepository invoiceRepository;
    private final CurrentUserService currentUserService;

    public BillingService(InvoiceRepository invoiceRepository, CurrentUserService currentUserService) {
        this.invoiceRepository = invoiceRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canViewBilling()")
    public InvoiceResponse createInvoice(CreateInvoiceRequest request) {
        UUID clinicId = currentUserService.requireClinicId();
        String invoiceNumber = nextInvoiceNumber(clinicId);
        InvoiceEntity entity = new InvoiceEntity(
                UUID.randomUUID(),
                clinicId,
                request.patientId(),
                request.appointmentId(),
                invoiceNumber,
                LocalDate.now(),
                InvoiceStatus.DRAFT);
        for (var line : request.lines()) {
            entity.addLine(new InvoiceLineEntity(
                    UUID.randomUUID(),
                    line.description(),
                    line.procedureCode(),
                    line.amount()));
        }
        entity = invoiceRepository.save(entity);
        return toResponse(entity);
    }

    @Transactional
    @PreAuthorize("@clinicSecurity.canViewBilling()")
    public InvoiceResponse recordPayment(UUID invoiceId) {
        UUID clinicId = currentUserService.requireClinicId();
        InvoiceEntity entity = invoiceRepository.findById(invoiceId)
                .filter(invoice -> invoice.getClinicId().equals(clinicId))
                .orElseThrow(() -> new DomainValidationException("invoice not found"));
        if (entity.getStatus() == InvoiceStatus.VOID) {
            throw new DomainValidationException("void invoice cannot be paid");
        }
        entity.markPaid();
        return toResponse(invoiceRepository.save(entity));
    }

    @Transactional(readOnly = true)
    @PreAuthorize("@clinicSecurity.canViewBilling()")
    public List<InvoiceResponse> listForPatient(UUID patientId) {
        UUID clinicId = currentUserService.requireClinicId();
        return invoiceRepository.findByClinicIdAndPatientIdOrderByIssuedDateDesc(clinicId, patientId).stream()
                .map(this::toResponse)
                .toList();
    }

    private String nextInvoiceNumber(UUID clinicId) {
        for (int attempt = 0; attempt < 50; attempt++) {
            String candidate = "INV-" + LocalDate.now().getYear() + "-" + (1000 + attempt);
            if (invoiceRepository.findByClinicIdAndInvoiceNumber(clinicId, candidate).isEmpty()) {
                return candidate;
            }
        }
        throw new IllegalStateException("unable to allocate invoice number");
    }

    private InvoiceResponse toResponse(InvoiceEntity entity) {
        List<InvoiceLineResponse> lines = entity.getLines().stream()
                .map(line -> new InvoiceLineResponse(
                        line.getDescription(), line.getProcedureCode(), line.getAmount()))
                .toList();
        return new InvoiceResponse(
                entity.getId(),
                entity.getPatientId(),
                entity.getAppointmentId(),
                entity.getInvoiceNumber(),
                entity.getIssuedDate(),
                entity.getStatus(),
                entity.getTotalAmount(),
                entity.getCurrency(),
                lines);
    }
}
