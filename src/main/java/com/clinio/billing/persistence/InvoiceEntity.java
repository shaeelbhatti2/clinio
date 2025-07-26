package com.clinio.billing.persistence;

import com.clinio.shared.persistence.ClinicScopedEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "invoices")
public class InvoiceEntity extends ClinicScopedEntity {

    @Id
    private UUID id;

    @Column(name = "patient_id", nullable = false)
    private UUID patientId;

    @Column(name = "appointment_id")
    private UUID appointmentId;

    @Column(name = "invoice_number", nullable = false)
    private String invoiceNumber;

    @Column(name = "issued_date", nullable = false)
    private LocalDate issuedDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.clinio.billing.domain.InvoiceStatus status;

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount;

    @Column(nullable = false)
    private String currency;

    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceLineEntity> lines = new ArrayList<>();

    protected InvoiceEntity() {
    }

    public InvoiceEntity(UUID id, UUID clinicId, UUID patientId, UUID appointmentId, String invoiceNumber,
                         LocalDate issuedDate, com.clinio.billing.domain.InvoiceStatus status) {
        super(clinicId);
        this.id = id;
        this.patientId = patientId;
        this.appointmentId = appointmentId;
        this.invoiceNumber = invoiceNumber;
        this.issuedDate = issuedDate;
        this.status = status;
        this.totalAmount = BigDecimal.ZERO;
        this.currency = "USD";
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getPatientId() {
        return patientId;
    }

    public UUID getAppointmentId() {
        return appointmentId;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public LocalDate getIssuedDate() {
        return issuedDate;
    }

    public com.clinio.billing.domain.InvoiceStatus getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public List<InvoiceLineEntity> getLines() {
        return lines;
    }

    public void addLine(InvoiceLineEntity line) {
        line.attach(this);
        lines.add(line);
        totalAmount = totalAmount.add(line.getAmount());
    }

    public void markPaid() {
        this.status = com.clinio.billing.domain.InvoiceStatus.PAID;
    }
}
