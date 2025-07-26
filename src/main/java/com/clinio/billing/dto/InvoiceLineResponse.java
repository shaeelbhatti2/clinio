package com.clinio.billing.dto;

import java.math.BigDecimal;

public record InvoiceLineResponse(
        String description,
        String procedureCode,
        BigDecimal amount) {
}
