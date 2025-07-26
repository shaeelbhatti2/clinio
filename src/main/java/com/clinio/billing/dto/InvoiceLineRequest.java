package com.clinio.billing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record InvoiceLineRequest(
        @NotBlank String description,
        String procedureCode,
        @NotNull BigDecimal amount) {
}
