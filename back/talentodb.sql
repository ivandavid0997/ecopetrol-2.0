-- =============================================================
--  talentodb.sql  –  Base de datos Talento Humano · Ecopetrol
--  Motor: MySQL 8.x
--  Ejecutar como: mysql -u root -p < talentodb.sql
-- =============================================================

-- ----------------------------------------------------------------
-- 0. BASE DE DATOS Y USUARIO
-- ----------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS talentodb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'ADMIN'@'localhost' IDENTIFIED BY 'M1Cl4v3*+';
GRANT ALL PRIVILEGES ON talentodb.* TO 'ADMIN'@'localhost';
FLUSH PRIVILEGES;

USE talentodb;

-- ----------------------------------------------------------------
-- 1. TABLA: usuarios
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS usuarios (
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    nombre            VARCHAR(150)   NOT NULL,
    rol               VARCHAR(20)    NOT NULL COMMENT 'EMPLEADO | LIDER',
    usuario           VARCHAR(80)    NOT NULL,
    numero_empleado   BIGINT         NULL,
    unidad_negocio    VARCHAR(150)   NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uq_usuarios_usuario (usuario),
    INDEX idx_usuarios_rol (rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------------
-- 2. TABLA: vacaciones
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS vacaciones (
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    numero_empleado   VARCHAR(30)    NULL,
    nombre_empleado   VARCHAR(150)   NULL,
    unidad_negocio    VARCHAR(150)   NULL,
    fecha_inicio      DATETIME       NULL,
    fecha_fin         DATETIME       NULL,
    total_dias        INT            NULL,
    comentario        TEXT           NULL,
    estado            VARCHAR(20)    NULL COMMENT 'PENDIENTE | APROBADA | RECHAZADA',
    lider_id          BIGINT         NULL,

    PRIMARY KEY (id),
    INDEX idx_vac_estado   (estado),
    INDEX idx_vac_empleado (numero_empleado),
    CONSTRAINT fk_vac_lider
        FOREIGN KEY (lider_id) REFERENCES usuarios (id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------------
-- 3. TABLA: incapacidades
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS incapacidades (
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    numero_empleado   VARCHAR(30)    NULL,
    nombre_empleado   VARCHAR(150)   NULL,
    unidad_negocio    VARCHAR(150)   NULL,
    tipo_incapacidad  VARCHAR(100)   NULL,
    entidad_salud     VARCHAR(100)   NULL,
    categoria         VARCHAR(100)   NULL,
    fecha_inicio      DATETIME       NULL,
    fecha_fin         DATETIME       NULL,
    total_dias        INT            NULL,
    diagnostico       TEXT           NULL,
    estado            VARCHAR(20)    NULL COMMENT 'PENDIENTE | APROBADA | RECHAZADA',
    archivo_adjunto   LONGTEXT       NULL,
    lider_id          BIGINT         NULL,

    PRIMARY KEY (id),
    INDEX idx_inc_estado   (estado),
    INDEX idx_inc_empleado (numero_empleado),
    CONSTRAINT fk_inc_lider
        FOREIGN KEY (lider_id) REFERENCES usuarios (id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------------
-- 4. TABLA: calamidades
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS calamidades (
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    numero_empleado   VARCHAR(30)    NULL,
    nombre_empleado   VARCHAR(150)   NULL,
    unidad_negocio    VARCHAR(150)   NULL,
    descripcion       TEXT           NULL,
    fecha_inicio      DATETIME       NULL,
    fecha_fin         DATETIME       NULL,
    total_dias        INT            NULL,
    comentario        TEXT           NULL,
    estado            VARCHAR(20)    NULL COMMENT 'PENDIENTE | APROBADA | RECHAZADA',
    archivo_adjunto   LONGTEXT       NULL,
    lider_id          BIGINT         NULL,

    PRIMARY KEY (id),
    INDEX idx_cal_estado   (estado),
    INDEX idx_cal_empleado (numero_empleado),
    CONSTRAINT fk_cal_lider
        FOREIGN KEY (lider_id) REFERENCES usuarios (id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------------------------------------------
-- 5. TABLA: dias_cumpleanios
-- ----------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dias_cumpleanios (
    id                BIGINT         NOT NULL AUTO_INCREMENT,
    numero_empleado   VARCHAR(30)    NULL,
    nombre_empleado   VARCHAR(150)   NULL,
    unidad_negocio    VARCHAR(150)   NULL,
    fecha_cumpleanio  DATETIME       NULL,
    comentario        TEXT           NULL,
    estado            VARCHAR(20)    NULL COMMENT 'PENDIENTE | APROBADA | RECHAZADA',
    lider_id          BIGINT         NULL,

    PRIMARY KEY (id),
    INDEX idx_cum_estado   (estado),
    INDEX idx_cum_empleado (numero_empleado),
    CONSTRAINT fk_cum_lider
        FOREIGN KEY (lider_id) REFERENCES usuarios (id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- =============================================================
--  DATOS SEMILLA (datos realistas)
-- =============================================================

-- ----------------------------------------------------------------
-- USUARIOS
--   Roles: EMPLEADO / LIDER
--   Contraseña: el backend solo busca por 'usuario' (no valida pwd)
-- ----------------------------------------------------------------
INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio) VALUES
-- Líderes
('Nidia Patricia Vivas Rocha',        'LIDER',    'nvivas',    301001, 'Application Services (AS)'),
('Sergio Alejandro Torres Mejía',     'LIDER',    'storres',   301002, 'Digital Strategy & Business'),

-- Empleados – Application Services
('Carlos Andrés Medina Vargas',       'EMPLEADO', 'cmedina',   401001, 'Application Services (AS)'),
('Diana Carolina Herrera Montoya',    'EMPLEADO', 'dherrera',  401002, 'Application Services (AS)'),
('Jhon Fredy Parra López',            'EMPLEADO', 'jparra',    401003, 'Application Services (AS)'),

-- Empleados – Digital Strategy & Business
('Ana María Gutiérrez Rondón',        'EMPLEADO', 'agutierrez', 401004, 'Digital Strategy & Business'),
('Miguel Ángel Ospina Bedoya',        'EMPLEADO', 'mospina',   401005, 'Digital Strategy & Business'),

-- Empleados – Infrastructure & Cloud
('Laura Daniela Suárez Castillo',     'EMPLEADO', 'lsuarez',   401006, 'Infrastructure & Cloud'),
('Ricardo Iván Morales Pedraza',      'EMPLEADO', 'rmorales',  401007, 'Infrastructure & Cloud');


-- ----------------------------------------------------------------
-- VACACIONES
-- lider_id: 1=Nidia (AS), 2=Sergio (Digital)
-- ----------------------------------------------------------------
INSERT INTO vacaciones
    (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
VALUES
-- Aprobadas
('401001', 'Carlos Andrés Medina Vargas',
 'Application Services (AS)',
 '2026-03-03 00:00:00', '2026-03-14 00:00:00', 10,
 'Vacaciones anuales reglamentarias. Viaje familiar planeado con anticipación.',
 'APROBADA', 1),

('401004', 'Ana María Gutiérrez Rondón',
 'Digital Strategy & Business',
 '2026-02-16 00:00:00', '2026-02-27 00:00:00', 10,
 'Descanso por acumulación de días de vacaciones del periodo anterior.',
 'APROBADA', 2),

-- Pendientes
('401002', 'Diana Carolina Herrera Montoya',
 'Application Services (AS)',
 '2026-06-09 00:00:00', '2026-06-20 00:00:00', 10,
 'Solicitud de vacaciones para semana de Corpus Christi y días adicionales.',
 'PENDIENTE', 1),

('401005', 'Miguel Ángel Ospina Bedoya',
 'Digital Strategy & Business',
 '2026-07-20 00:00:00', '2026-07-31 00:00:00', 10,
 'Aprovechamiento de temporada de fiestas patrias para viaje al exterior.',
 'PENDIENTE', 2),

('401006', 'Laura Daniela Suárez Castillo',
 'Infrastructure & Cloud',
 '2026-08-03 00:00:00', '2026-08-07 00:00:00', 5,
 'Vacaciones cortas por asuntos personales.',
 'PENDIENTE', 1),

-- Rechazadas
('401003', 'Jhon Fredy Parra López',
 'Application Services (AS)',
 '2026-04-13 00:00:00', '2026-04-17 00:00:00', 5,
 'Vacaciones solicitadas durante semana de lanzamiento del proyecto. No es viable.',
 'RECHAZADA', 1);


-- ----------------------------------------------------------------
-- INCAPACIDADES
-- ----------------------------------------------------------------
INSERT INTO incapacidades
    (numero_empleado, nombre_empleado, unidad_negocio,
     tipo_incapacidad, entidad_salud, categoria,
     fecha_inicio, fecha_fin, total_dias,
     diagnostico, estado, archivo_adjunto, lider_id)
VALUES
-- Aprobadas
('401003', 'Jhon Fredy Parra López',
 'Application Services (AS)',
 'Enfermedad General', 'EPS Sanitas', 'Incapacidad Ambulatoria',
 '2026-04-07 00:00:00', '2026-04-11 00:00:00', 5,
 'CIE-10 J069: Infección aguda de las vías respiratorias superiores, no especificada.',
 'APROBADA', NULL, 1),

('401006', 'Laura Daniela Suárez Castillo',
 'Infrastructure & Cloud',
 'Accidente de Trabajo', 'Nueva EPS', 'Incapacidad con hospitalización',
 '2026-03-10 00:00:00', '2026-03-24 00:00:00', 15,
 'CIE-10 S820: Fractura de rótula. Accidente en vía pública al desplazarse al trabajo.',
 'APROBADA', NULL, 1),

-- Pendientes
('401001', 'Carlos Andrés Medina Vargas',
 'Application Services (AS)',
 'Enfermedad General', 'EPS Sura', 'Incapacidad Ambulatoria',
 '2026-05-19 00:00:00', '2026-05-21 00:00:00', 3,
 'CIE-10 K297: Gastritis crónica, no especificada. Restricción de actividades físicas.',
 'PENDIENTE', NULL, 1),

('401007', 'Ricardo Iván Morales Pedraza',
 'Infrastructure & Cloud',
 'Enfermedad General', 'Compensar EPS', 'Incapacidad Ambulatoria',
 '2026-05-15 00:00:00', '2026-05-19 00:00:00', 5,
 'CIE-10 M545: Lumbago no especificado. Reposo relativo y fisioterapia.',
 'PENDIENTE', NULL, 1),

-- Rechazada
('401004', 'Ana María Gutiérrez Rondón',
 'Digital Strategy & Business',
 'Enfermedad General', 'EPS Famisanar', 'Incapacidad Ambulatoria',
 '2026-02-02 00:00:00', '2026-02-04 00:00:00', 3,
 'CIE-10 J00X: Rinofaringitis aguda (resfriado común). Documento presentado extemporáneamente.',
 'RECHAZADA', NULL, 2);


-- ----------------------------------------------------------------
-- CALAMIDADES
-- ----------------------------------------------------------------
INSERT INTO calamidades
    (numero_empleado, nombre_empleado, unidad_negocio,
     descripcion, fecha_inicio, fecha_fin, total_dias,
     comentario, estado, archivo_adjunto, lider_id)
VALUES
-- Aprobadas
('401002', 'Diana Carolina Herrera Montoya',
 'Application Services (AS)',
 'Fallecimiento de familiar en primer grado de consanguinidad (padre).',
 '2026-01-20 00:00:00', '2026-01-23 00:00:00', 4,
 'Licencia de luto de acuerdo con el Artículo 57 numeral 10 del CST.',
 'APROBADA', NULL, 1),

('401005', 'Miguel Ángel Ospina Bedoya',
 'Digital Strategy & Business',
 'Inundación de vivienda propia por desbordamiento de quebrada en el sector.',
 '2026-04-22 00:00:00', '2026-04-24 00:00:00', 3,
 'Se adjunta certificado de la Alcaldía y fotografías del siniestro.',
 'APROBADA', NULL, 2),

-- Pendiente
('401007', 'Ricardo Iván Morales Pedraza',
 'Infrastructure & Cloud',
 'Hospitalización urgente de hijo menor de edad por cuadro febril severo.',
 '2026-05-12 00:00:00', '2026-05-14 00:00:00', 3,
 'Se adjunta epicrisis del hospital y registro civil del menor.',
 'PENDIENTE', NULL, 1),

('401006', 'Laura Daniela Suárez Castillo',
 'Infrastructure & Cloud',
 'Incendio parcial en vivienda arrendada. Pérdida de enseres.',
 '2026-05-16 00:00:00', '2026-05-17 00:00:00', 2,
 'Reporte del Cuerpo de Bomberos adjunto para validación.',
 'PENDIENTE', NULL, 1),

-- Rechazada
('401003', 'Jhon Fredy Parra López',
 'Application Services (AS)',
 'Calamidad doméstica por problemas de convivencia.',
 '2026-03-03 00:00:00', '2026-03-04 00:00:00', 2,
 'No cumple los criterios de calamidad doméstica establecidos en el reglamento.',
 'RECHAZADA', NULL, 1);


-- ----------------------------------------------------------------
-- DÍAS DE CUMPLEAÑOS
-- ----------------------------------------------------------------
INSERT INTO dias_cumpleanios
    (numero_empleado, nombre_empleado, unidad_negocio,
     fecha_cumpleanio, comentario, estado, lider_id)
VALUES
-- Aprobadas
('401001', 'Carlos Andrés Medina Vargas',
 'Application Services (AS)',
 '2026-03-15 00:00:00',
 'Solicitud de medio día libre por beneficio de cumpleaños institucional.',
 'APROBADA', 1),

('401004', 'Ana María Gutiérrez Rondón',
 'Digital Strategy & Business',
 '2026-02-28 00:00:00',
 'Beneficio anual de día de cumpleaños. Trámite realizado con 5 días de anticipación.',
 'APROBADA', 2),

('401007', 'Ricardo Iván Morales Pedraza',
 'Infrastructure & Cloud',
 '2026-04-10 00:00:00',
 'Solicitud de día libre reglamentario por fecha de nacimiento.',
 'APROBADA', 1),

-- Pendientes
('401002', 'Diana Carolina Herrera Montoya',
 'Application Services (AS)',
 '2026-06-03 00:00:00',
 'Solicitud anticipada del beneficio de cumpleaños para el mes de junio.',
 'PENDIENTE', 1),

('401005', 'Miguel Ángel Ospina Bedoya',
 'Digital Strategy & Business',
 '2026-07-18 00:00:00',
 'Beneficio de cumpleaños — solicitud enviada con un mes de anticipación.',
 'PENDIENTE', 2),

('401006', 'Laura Daniela Suárez Castillo',
 'Infrastructure & Cloud',
 '2026-08-22 00:00:00',
 'Solicitud de día libre por cumpleaños. Primera solicitud del beneficio.',
 'PENDIENTE', 1),

-- Rechazada
('401003', 'Jhon Fredy Parra López',
 'Application Services (AS)',
 '2026-05-05 00:00:00',
 'Rechazada: la solicitud fue enviada el mismo día del cumpleaños, incumpliendo los 3 días hábiles requeridos.',
 'RECHAZADA', 1);


-- ----------------------------------------------------------------
-- VERIFICACIÓN FINAL
-- ----------------------------------------------------------------
SELECT 'usuarios'         AS tabla, COUNT(*) AS registros FROM usuarios
UNION ALL
SELECT 'vacaciones',        COUNT(*) FROM vacaciones
UNION ALL
SELECT 'incapacidades',     COUNT(*) FROM incapacidades
UNION ALL
SELECT 'calamidades',       COUNT(*) FROM calamidades
UNION ALL
SELECT 'dias_cumpleanios',  COUNT(*) FROM dias_cumpleanios;
