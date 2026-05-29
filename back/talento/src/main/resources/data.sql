-- =============================================================
--  data.sql  –  Datos semilla para H2 (desarrollo local)
--  Spring Boot lo ejecuta al arrancar, DESPUÉS de que
--  Hibernate crea las tablas con ddl-auto=update
-- =============================================================

-- Evita duplicar datos si el contexto se recarga
DELETE FROM dias_cumpleanios;
DELETE FROM incapacidades;
DELETE FROM calamidades;
DELETE FROM vacaciones;
DELETE FROM usuarios;

-- ----------------------------------------------------------------
-- USUARIOS  (id explícito para poder referenciar en FKs)
-- ----------------------------------------------------------------
INSERT INTO usuarios (id, nombre, rol, usuario, numero_empleado, unidad_negocio, password) VALUES
(1, 'Nidia Patricia Vivas Rocha',        'LIDER',    'nvivas',    301001, 'Application Services (AS)', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(2, 'Sergio Alejandro Torres Mejía',     'LIDER',    'storres',   301002, 'Digital Strategy & Business', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(3, 'Carlos Andrés Medina Vargas',       'EMPLEADO', 'cmedina',   401001, 'Application Services (AS)', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(4, 'Diana Carolina Herrera Montoya',    'EMPLEADO', 'dherrera',  401002, 'Application Services (AS)', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(5, 'Jhon Fredy Parra López',            'EMPLEADO', 'jparra',    401003, 'Application Services (AS)', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(6, 'Ana María Gutiérrez Rondón',        'EMPLEADO', 'agutierrez',401004, 'Digital Strategy & Business', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(7, 'Miguel Ángel Ospina Bedoya',        'EMPLEADO', 'mospina',   401005, 'Digital Strategy & Business', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(8, 'Laura Daniela Suárez Castillo',     'EMPLEADO', 'lsuarez',   401006, 'Infrastructure & Cloud', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(9, 'Ricardo Iván Morales Pedraza',      'EMPLEADO', 'rmorales',  401007, 'Infrastructure & Cloud', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8='),
(10, 'Jorge Eliecer Torres Morales',     'PEOPLE',   'jtorres',   401008, 'Infrastructure & Cloud', '6fVhmdqrgGs8gGZrkbps/b7nl8eTpfbG6YpDLLbD8M8=');

-- ----------------------------------------------------------------
-- VACACIONES
-- ----------------------------------------------------------------
INSERT INTO vacaciones
    (numero_empleado, nombre_empleado, unidad_negocio,
     fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id, fecha_creacion)
VALUES
('401001','Carlos Andrés Medina Vargas','Application Services (AS)',
 '2026-03-03','2026-03-14',10,
 'Vacaciones anuales reglamentarias. Viaje familiar planeado con anticipación.',
 'APROBADA',1,'2026-02-25 08:00:00'),

-- ... (continúa con los demás registros, cambiando la fecha)
('401004','Ana María Gutiérrez Rondón','Digital Strategy & Business',
 '2026-02-16','2026-02-27',10,
 'Descanso por acumulación de días del periodo anterior.',
 'APROBADA',2,'2026-02-02 09:10:00'),

('401002','Diana Carolina Herrera Montoya','Application Services (AS)',
 '2026-06-09','2026-06-20',10,
 'Solicitud para semana de Corpus Christi y días adicionales.',
 'PENDIENTE',1,'2026-05-23 17:42:00'),

('401005','Miguel Ángel Ospina Bedoya','Digital Strategy & Business',
 '2026-07-20','2026-07-31',10,
 'Aprovechamiento de temporada de fiestas patrias para viaje al exterior.',
 'PENDIENTE',2,'2026-06-20 13:59:00'),

('401006','Laura Daniela Suárez Castillo','Infrastructure & Cloud',
 '2026-08-03','2026-08-07',5,
 'Vacaciones cortas por asuntos personales.',
 'PENDIENTE',1,'2026-07-15 10:21:00'),

('401003','Jhon Fredy Parra López','Application Services (AS)',
 '2026-04-13','2026-04-17',5,
 'Solicitadas durante semana de lanzamiento del proyecto. No viable.',
 'RECHAZADA',1,'2026-03-25 15:31:00');

-- ----------------------------------------------------------------
-- INCAPACIDADES
-- ----------------------------------------------------------------
INSERT INTO incapacidades
    (numero_empleado, nombre_empleado, unidad_negocio,
     tipo_incapacidad, entidad_salud, categoria,
     fecha_inicio, fecha_fin, total_dias,
     diagnostico, estado, archivo_adjunto, lider_id, fecha_creacion)
VALUES
('401003','Jhon Fredy Parra López','Application Services (AS)',
 'Enfermedad General','EPS Sanitas','Incapacidad Ambulatoria',
 '2026-04-07','2026-04-11',5,
 'CIE-10 J069: Infección aguda de vías respiratorias superiores.',
 'APROBADA',NULL,1,'2026-04-05 08:14:00'),

('401006','Laura Daniela Suárez Castillo','Infrastructure & Cloud',
 'Accidente de Trabajo','Nueva EPS','Incapacidad con hospitalización',
 '2026-03-10','2026-03-24',15,
 'CIE-10 S820: Fractura de rótula. Accidente en vía pública.',
 'APROBADA',NULL,1,'2026-03-08 11:11:00'),

('401001','Carlos Andrés Medina Vargas','Application Services (AS)',
 'Enfermedad General','EPS Sura','Incapacidad Ambulatoria',
 '2026-05-19','2026-05-21',3,
 'CIE-10 K297: Gastritis crónica. Restricción de actividades.',
 'PENDIENTE',NULL,1,'2026-05-17 06:24:00'),

('401007','Ricardo Iván Morales Pedraza','Infrastructure & Cloud',
 'Enfermedad General','Compensar EPS','Incapacidad Ambulatoria',
 '2026-05-15','2026-05-19',5,
 'CIE-10 M545: Lumbago no especificado. Reposo y fisioterapia.',
 'PENDIENTE',NULL,1,'2026-05-10 16:48:00'),

('401004','Ana María Gutiérrez Rondón','Digital Strategy & Business',
 'Enfermedad General','EPS Famisanar','Incapacidad Ambulatoria',
 '2026-02-02','2026-02-04',3,
 'CIE-10 J00X: Rinofaringitis aguda. Documento presentado extemporáneamente.',
 'RECHAZADA',NULL,2,'2026-02-01 07:03:00');

-- ----------------------------------------------------------------
-- CALAMIDADES
-- ----------------------------------------------------------------
INSERT INTO calamidades
    (numero_empleado, nombre_empleado, unidad_negocio,
     descripcion, fecha_inicio, fecha_fin, total_dias,
     comentario, estado, archivo_adjunto, lider_id, fecha_creacion)
VALUES
('401002','Diana Carolina Herrera Montoya','Application Services (AS)',
 'Fallecimiento de familiar en primer grado de consanguinidad (padre).',
 '2026-01-20','2026-01-23',4,
 'Licencia de luto — Artículo 57 numeral 10 del CST.',
 'APROBADA',NULL,1,'2026-01-10 09:08:00'),

('401005','Miguel Ángel Ospina Bedoya','Digital Strategy & Business',
 'Inundación de vivienda propia por desbordamiento de quebrada.',
 '2026-04-22','2026-04-24',3,
 'Se adjunta certificado de la Alcaldía y fotografías del siniestro.',
 'APROBADA',NULL,2,'2026-04-18 21:00:00'),

('401007','Ricardo Iván Morales Pedraza','Infrastructure & Cloud',
 'Hospitalización urgente de hijo menor de edad por cuadro febril severo.',
 '2026-05-12','2026-05-14',3,
 'Se adjunta epicrisis del hospital y registro civil del menor.',
 'PENDIENTE',NULL,1,'2026-05-10 12:36:00'),

('401006','Laura Daniela Suárez Castillo','Infrastructure & Cloud',
 'Incendio parcial en vivienda arrendada. Pérdida de enseres.',
 '2026-05-16','2026-05-17',2,
 'Reporte del Cuerpo de Bomberos adjunto para validación.',
 'PENDIENTE',NULL,1,'2026-05-14 19:17:00'),

('401003','Jhon Fredy Parra López','Application Services (AS)',
 'Calamidad doméstica por problemas de convivencia.',
 '2026-03-03','2026-03-04',2,
 'No cumple los criterios establecidos en el reglamento interno.',
 'RECHAZADA',NULL,1,'2026-02-28 14:52:00');

-- ----------------------------------------------------------------
-- DÍAS DE CUMPLEAÑOS
-- ----------------------------------------------------------------
INSERT INTO dias_cumpleanios
    (numero_empleado, nombre_empleado, unidad_negocio,
     fecha_cumpleanio, comentario, estado, lider_id, fecha_creacion)
VALUES
('401001','Carlos Andrés Medina Vargas','Application Services (AS)',
 '2026-03-15',
 'Solicitud de medio día libre por beneficio de cumpleaños institucional.',
 'APROBADA',1,'2026-02-28 08:10:00'),

('401004','Ana María Gutiérrez Rondón','Digital Strategy & Business',
 '2026-02-28',
 'Beneficio anual de día de cumpleaños. Trámite realizado con 5 días de anticipación.',
 'APROBADA',2,'2026-02-23 18:42:00'),

('401007','Ricardo Iván Morales Pedraza','Infrastructure & Cloud',
 '2026-04-10',
 'Solicitud de día libre reglamentario por fecha de nacimiento.',
 'APROBADA',1,'2026-03-18 12:04:00'),

('401002','Diana Carolina Herrera Montoya','Application Services (AS)',
 '2026-06-03',
 'Solicitud anticipada del beneficio de cumpleaños para junio.',
 'PENDIENTE',1,'2026-05-15 09:03:00'),

('401005','Miguel Ángel Ospina Bedoya','Digital Strategy & Business',
 '2026-07-18',
 'Beneficio de cumpleaños — solicitud enviada con un mes de anticipación.',
 'PENDIENTE',2,'2026-06-10 22:30:00'),

('401006','Laura Daniela Suárez Castillo','Infrastructure & Cloud',
 '2026-08-22',
 'Solicitud de día libre por cumpleaños. Primera solicitud del beneficio.',
 'PENDIENTE',1,'2026-08-01 10:10:00'),

('401003','Jhon Fredy Parra López','Application Services (AS)',
 '2026-05-05',
 'Rechazada: solicitud enviada el mismo día del cumpleaños, incumpliendo los 3 días hábiles requeridos.',
 'RECHAZADA',1,'2026-05-05 07:23:00');
