package com.clinio.billing;

import com.clinio.billing.dto.CreateInvoiceRequest;
import com.clinio.billing.dto.InvoiceResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/billing/invoices")
public class BillingController {

    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceResponse create(@Valid @RequestBody CreateInvoiceRequest request) {
        return billingService.createInvoice(request);
    }

    @PostMapping("/{invoiceId}/payments")
    public InvoiceResponse pay(@PathVariable UUID invoiceId) {
        return billingService.recordPayment(invoiceId);
    }

    @GetMapping("/patients/{patientId}")
    public List<InvoiceResponse> list(@PathVariable UUID patientId) {
        return billingService.listForPatient(patientId);
    }
}
