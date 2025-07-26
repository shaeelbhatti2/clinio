package com.clinio.billing.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "invoice_lines")
public class InvoiceLineEntity {

    @Id
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "invoice_id")
    private InvoiceEntity invoice;

    @Column(nullable = false)
    private String description;

    @Column(name = "procedure_code")
    private String procedureCode;

    @Column(nullable = false)
    private BigDecimal amount;

    protected InvoiceLineEntity() {
    }

    public InvoiceLineEntity(UUID id, String description, String procedureCode, BigDecimal amount) {
        this.id = id;
        this.description = description;
        this.procedureCode = procedureCode;
        this.amount = amount;
    }

    void attach(InvoiceEntity invoice) {
        this.invoice = invoice;
    }

    public String getDescription() {
        return description;
    }

    public String getProcedureCode() {
        return procedureCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
