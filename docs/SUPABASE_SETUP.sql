-- Oral Surgery AI - Supabase PostgreSQL Schema Setup & Seed Data
-- This script creates all required tables and inserts initial demo data.
-- Copy and paste this entire script into the Supabase SQL Editor.

-- ==========================================================
-- 1. TABLE CREATION
-- ==========================================================

-- Create 'users' table
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR UNIQUE NOT NULL,
    full_name VARCHAR NOT NULL,
    hashed_password VARCHAR NOT NULL,
    role VARCHAR DEFAULT 'User',
    patient_id VARCHAR UNIQUE,
    is_verified INTEGER DEFAULT 0,
    is_active INTEGER DEFAULT 1,
    last_login TIMESTAMP
);

-- Create 'patients' table
CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    age INTEGER,
    surgeon_id INTEGER REFERENCES users(id),
    user_id INTEGER REFERENCES users(id),
    is_active INTEGER DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Ensure this column exists
);

-- Create 'scans' table
CREATE TABLE IF NOT EXISTS scans (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
    file_path VARCHAR,
    modality VARCHAR DEFAULT 'CBCT',
    acquisition_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create 'clinical_findings' table
CREATE TABLE IF NOT EXISTS clinical_findings (
    id SERIAL PRIMARY KEY,
    scan_id INTEGER REFERENCES scans(id) ON DELETE CASCADE,
    ai_model_version VARCHAR,
    nerve_trace_data JSONB,
    malignancy_risk VARCHAR,
    prognosis_score FLOAT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create 'clinical_notes' table
CREATE TABLE IF NOT EXISTS clinical_notes (
    id SERIAL PRIMARY KEY,
    file_name VARCHAR,
    patient_id VARCHAR,
    doctor_name VARCHAR,
    doctor_email VARCHAR,
    content TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create 'otps' table
CREATE TABLE IF NOT EXISTS otps (
    id SERIAL PRIMARY KEY,
    email VARCHAR NOT NULL,
    code VARCHAR NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP
);

-- Create 'audit_logs' table
CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    user_email VARCHAR,
    action VARCHAR,
    details TEXT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ==========================================================
-- 2. SEED DATA (INITIAL USERS & PATIENTS)
-- ==========================================================

-- Insert Master Admin (Password is hashed)
INSERT INTO users (email, full_name, hashed_password, role, is_verified, is_active)
VALUES (
    'pataharsha@gmail.com',
    'Harsha Master Admin',
    '$2b$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGGa31S2', -- Hashed version of AdminSecurePassword123!
    'Admin',
    1,
    1
) ON CONFLICT (email) DO NOTHING;

-- Insert Master Surgeon
INSERT INTO users (email, full_name, hashed_password, role, is_verified, is_active)
VALUES (
    'dr.smith@oralsurgery.ai',
    'Dr. John Smith',
    '$2b$12$EixZaYVK1fsbw1ZfbX3OXePaWxn96p36WQoeG6Lruj3vjPGGa31S2',
    'Surgeon',
    1,
    1
) ON CONFLICT (email) DO NOTHING;

-- Insert Initial Patient for Demo
INSERT INTO patients (name, age, surgeon_id, is_active)
VALUES ('Jane Doe', 45, (SELECT id FROM users WHERE email='dr.smith@oralsurgery.ai'), 1);

-- Insert Sample Clinical Note
INSERT INTO clinical_notes (file_name, patient_id, doctor_name, doctor_email, content)
VALUES ('demo_scan_001.nii.gz', 'P-00001', 'Dr. John Smith', 'dr.smith@oralsurgery.ai', 'Initial assessment: Suspected IAN involvement. Planning 3D segmentation.');

-- ==========================================================
-- 3. VERIFICATION
-- ==========================================================
SELECT table_name FROM information_schema.tables WHERE table_schema = 'public';
