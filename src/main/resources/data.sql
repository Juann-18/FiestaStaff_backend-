-- ============================================================
-- Schema para MySQL - FiestaStaff
-- ============================================================

CREATE DATABASE IF NOT EXISTS fiesta_staff;
USE fiesta_staff;

-- event_type
INSERT INTO event_type (name, description) VALUES
('Boda', 'Evento de boda completo'),
('Concierto', 'Evento musical en vivo'),
('Conferencia', 'Evento de presentaciones'),
('Cumpleanos', 'Celebracion de cumpleanos'),
('Corporativo', 'Evento empresarial');

-- Admin inicial (password: admin123)
INSERT INTO "user" (first_name, last_name, email, password, role, description) VALUES
('Admin', 'Sistema', 'admin@fiesta.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjzRGdjGj/n3.rsS3uJ5Z6wZ4bXKTOy', 'ADMIN', 'Administrador del sistema');
