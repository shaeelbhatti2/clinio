package com.clinio.billing.dto;

import com.clinio.billing.domain.InvoiceStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record InvoiceResponse(
        UUID id,
        UUID patientId,
        UUID appointmentId,
        String invoiceNumber,
        LocalDate issuedDate,
        InvoiceStatus status,
        BigDecimal totalAmount,
        String currency,
        List<InvoiceLineResponse> lines) {
}
