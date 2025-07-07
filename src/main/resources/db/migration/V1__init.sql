CREATE TABLE clinics (
    id UUID PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    slug VARCHAR(80) NOT NULL UNIQUE,
    timezone VARCHAR(64) NOT NULL,
    address_line1 VARCHAR(200),
    city VARCHAR(100),
    state VARCHAR(50),
    postal_code VARCHAR(20),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    email VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    display_name VARCHAR(120) NOT NULL,
    role VARCHAR(32) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (clinic_id, email)
);

CREATE TABLE providers (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    user_id UUID NOT NULL REFERENCES users(id),
    npi_number VARCHAR(10) NOT NULL,
    specialty VARCHAR(120),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (clinic_id, user_id)
);

CREATE TABLE rooms (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    name VARCHAR(80) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    UNIQUE (clinic_id, name)
);

CREATE TABLE patients (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    medical_record_number VARCHAR(12) NOT NULL,
    first_name VARCHAR(80) NOT NULL,
    last_name VARCHAR(80) NOT NULL,
    date_of_birth DATE NOT NULL,
    phone VARCHAR(32) NOT NULL,
    email VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    emergency_name VARCHAR(120),
    emergency_phone VARCHAR(32),
    emergency_relationship VARCHAR(80),
    insurance_carrier VARCHAR(120),
    insurance_policy_number VARCHAR(80),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (clinic_id, medical_record_number)
);

CREATE INDEX idx_patients_clinic_name ON patients (clinic_id, last_name, first_name);
CREATE INDEX idx_patients_clinic_phone ON patients (clinic_id, phone);

CREATE TABLE provider_schedules (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    provider_id UUID NOT NULL REFERENCES providers(id),
    day_of_week SMALLINT NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    lunch_start TIME,
    lunch_end TIME
);

CREATE TABLE appointments (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    patient_id UUID NOT NULL REFERENCES patients(id),
    provider_id UUID NOT NULL REFERENCES providers(id),
    room_id UUID REFERENCES rooms(id),
    appointment_type VARCHAR(80) NOT NULL,
    start_at TIMESTAMPTZ NOT NULL,
    duration_minutes INT NOT NULL,
    status VARCHAR(32) NOT NULL,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_appointments_provider_start ON appointments (clinic_id, provider_id, start_at);
CREATE INDEX idx_appointments_room_start ON appointments (clinic_id, room_id, start_at);

CREATE TABLE visit_notes (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    appointment_id UUID NOT NULL REFERENCES appointments(id),
    patient_id UUID NOT NULL REFERENCES patients(id),
    provider_id UUID NOT NULL REFERENCES providers(id),
    template_code VARCHAR(40) NOT NULL,
    subjective TEXT,
    objective TEXT,
    assessment TEXT,
    plan TEXT,
    signed BOOLEAN NOT NULL DEFAULT FALSE,
    signed_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE invoices (
    id UUID PRIMARY KEY,
    clinic_id UUID NOT NULL REFERENCES clinics(id),
    patient_id UUID NOT NULL REFERENCES patients(id),
    appointment_id UUID REFERENCES appointments(id),
    invoice_number VARCHAR(40) NOT NULL,
    issued_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    total_amount NUMERIC(12, 2) NOT NULL,
    currency VARCHAR(3) NOT NULL DEFAULT 'USD',
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (clinic_id, invoice_number)
);

CREATE TABLE invoice_lines (
    id UUID PRIMARY KEY,
    invoice_id UUID NOT NULL REFERENCES invoices(id) ON DELETE CASCADE,
    description VARCHAR(255) NOT NULL,
    procedure_code VARCHAR(40),
    amount NUMERIC(12, 2) NOT NULL
);
