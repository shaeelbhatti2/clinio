package com.clinio.billing.domain;

import com.clinio.shared.domain.Money;
import com.clinio.shared.domain.PatientId;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InvoiceSummary(
        UUID id,
        PatientId patientId,
        String invoiceNumber,
        LocalDate issuedDate,
        InvoiceStatus status,
        Money total,
        List<InvoiceLineItem> lines) {
}
