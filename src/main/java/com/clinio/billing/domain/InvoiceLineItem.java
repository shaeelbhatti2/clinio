package com.clinio.billing.domain;

import com.clinio.shared.domain.Money;

public record InvoiceLineItem(
        String description,
        String procedureCode,
        Money amount) {
}
