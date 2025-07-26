package com.clinio.billing.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, UUID> {

    Optional<InvoiceEntity> findByClinicIdAndInvoiceNumber(UUID clinicId, String invoiceNumber);

    List<InvoiceEntity> findByClinicIdAndPatientIdOrderByIssuedDateDesc(UUID clinicId, UUID patientId);
}
