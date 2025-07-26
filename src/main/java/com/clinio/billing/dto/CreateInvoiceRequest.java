package com.clinio.billing.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

public record CreateInvoiceRequest(
        @NotNull UUID patientId,
        UUID appointmentId,
        @NotEmpty @Valid List<InvoiceLineRequest> lines) {
}
