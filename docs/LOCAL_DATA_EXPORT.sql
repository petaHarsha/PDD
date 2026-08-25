-- Oral Surgery AI - Local Data Export for Supabase
-- Generated from clinical_data.db

BEGIN;

-- Data for table: users
INSERT INTO users (id, email, full_name, hashed_password, role, is_verified, is_active, last_login) VALUES (1, 'doctor@pdd.com', 'Dr. Smith', '$2b$12$rHkTqY3/QPiQ6hu8NPVOfeuB5Cghrby.YJkFQYuCmPuU1NQm8B6ZW', 'Surgeon', 0, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO users (id, email, full_name, hashed_password, role, is_verified, is_active, last_login) VALUES (2, 'admin@pdd.com', 'System Admin', '$2b$12$gZejA2fNcgjEIqnNWEAjruQuvGBAXWsq1u8ogPJc220Wwvvi28FvO', 'Admin', 0, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO users (id, email, full_name, hashed_password, role, is_verified, is_active, last_login) VALUES (3, 'pataharsha', 'Pata Harsha', '$2b$12$4OJ0o/VLO7AQ9R2mNNVA.O9gKR4pGfq5D85.NYysxkSfPGl/LJotW', 'Surgeon', 0, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO users (id, email, full_name, hashed_password, role, is_verified, is_active, last_login) VALUES (4, 'pataharsha@gmail.com', 'Pata Harsha (Admin)', '$2b$12$YmrMp3E/9M0jZsY0r.R8sugpeZBVXBD7X0RCITJrbRfet2/1ctTeu', 'Admin', 0, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO users (id, email, full_name, hashed_password, role, is_verified, is_active, last_login) VALUES (5, 'nageswarbellamkonda36@gmail.com', 'nagesh', '$2b$12$VPrXJafuiKLkROfwqQaONO/CE1ef6.q1JtjahYKuD3bdcfjkj7qfS', 'Surgeon', 0, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO users (id, email, full_name, hashed_password, role, is_verified, is_active, last_login) VALUES (6, 'harshavardhan1039.sse@saveetha.com', 'Harshavardhan (Surgeon)', '$2b$12$zIjk3V./2fYHdIOmHkGKBOaDFYzh4X63mc08IBKjupB4Gd.EikUbS', 'Surgeon', 1, 1, '2026-08-05 13:50:57.247839') ON CONFLICT DO NOTHING;

-- Data for table: patients
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (1, 'Alice Johnson', 45, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (2, 'Bob Williams', 52, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (3, 'Charlie Brown', 38, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (4, 'Diana Ross', 61, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (5, 'Edward Norton', 29, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (6, 'Fiona Apple', 44, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (7, 'George Miller', 57, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (8, 'Hannah Abbott', 33, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (9, 'Ian McKellen', 72, 1, NULL) ON CONFLICT DO NOTHING;
INSERT INTO patients (id, name, age, surgeon_id, created_at) VALUES (10, 'Jane Doe', 41, 1, NULL) ON CONFLICT DO NOTHING;

-- Error exporting clinical_notes: no such table: clinical_notes
-- Data for table: audit_logs
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (1, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 09:10:51', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (2, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 10:37:46', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (3, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 18:58:19', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (4, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 19:34:40', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (5, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 19:56:59', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (6, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:30:33', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (7, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:31:06', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (8, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:32:13', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (9, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:35:16', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (10, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:36:30', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (11, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:45:04', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (12, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-24 21:45:42', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (13, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-27 14:47:37', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (14, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-29 12:56:34', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (15, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-29 13:51:45', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (16, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-29 14:01:00', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (17, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-29 14:46:59', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (18, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-29 14:56:44', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (19, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-31 16:36:25', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (20, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-31 17:41:45', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (21, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-07-31 17:56:16', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (22, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-03 08:30:31', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (23, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-03 08:54:37', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (24, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 12:48:00', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (25, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 12:55:18', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (26, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 13:17:35', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (27, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 13:22:51', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (28, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 13:32:39', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (29, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 14:00:35', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (30, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 14:25:19', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (31, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 15:19:33', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (32, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 16:10:38', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (33, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-04 17:49:06', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (34, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 03:08:50', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (35, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 03:53:44', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (36, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 04:32:07', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (37, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 04:41:37', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (38, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 04:59:48', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (39, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 05:36:15', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (40, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 06:26:00', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (41, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 06:45:27', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (42, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 07:45:48', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;
INSERT INTO audit_logs (id, user_email, action, timestamp, details) VALUES (43, 'harshavardhan1039.sse@saveetha.com', 'Login', '2026-08-05 08:20:57', 'Web/Android Portal Login Success') ON CONFLICT DO NOTHING;

COMMIT;
