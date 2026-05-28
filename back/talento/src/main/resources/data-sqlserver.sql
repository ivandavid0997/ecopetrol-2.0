IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'nvivas')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Nidia Patricia Vivas Rocha', 'LIDER', 'nvivas', 301001, 'Application Services (AS)');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'storres')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Sergio Alejandro Torres Mejia', 'LIDER', 'storres', 301002, 'Digital Strategy & Business');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'cmedina')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Carlos Andres Medina Vargas', 'EMPLEADO', 'cmedina', 401001, 'Application Services (AS)');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'dherrera')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Diana Carolina Herrera Montoya', 'EMPLEADO', 'dherrera', 401002, 'Application Services (AS)');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'jparra')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Jhon Fredy Parra Lopez', 'EMPLEADO', 'jparra', 401003, 'Application Services (AS)');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'agutierrez')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Ana Maria Gutierrez Rondon', 'EMPLEADO', 'agutierrez', 401004, 'Digital Strategy & Business');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'mospina')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Miguel Angel Ospina Bedoya', 'EMPLEADO', 'mospina', 401005, 'Digital Strategy & Business');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'lsuarez')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Laura Daniela Suarez Castillo', 'EMPLEADO', 'lsuarez', 401006, 'Infrastructure & Cloud');

IF NOT EXISTS (SELECT 1 FROM usuarios WHERE usuario = 'rmorales')
    INSERT INTO usuarios (nombre, rol, usuario, numero_empleado, unidad_negocio)
    VALUES ('Ricardo Ivan Morales Pedraza', 'EMPLEADO', 'rmorales', 401007, 'Infrastructure & Cloud');

IF NOT EXISTS (SELECT 1 FROM vacaciones WHERE numero_empleado = '401001' AND fecha_inicio = CAST('2026-03-03T00:00:00' AS datetime2))
    INSERT INTO vacaciones (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
    VALUES ('401001', 'Carlos Andres Medina Vargas', 'Application Services (AS)', CAST('2026-03-03T00:00:00' AS datetime2), CAST('2026-03-14T00:00:00' AS datetime2), 10, 'Vacaciones anuales reglamentarias. Viaje familiar planeado con anticipacion.', 'APROBADA', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM vacaciones WHERE numero_empleado = '401004' AND fecha_inicio = CAST('2026-02-16T00:00:00' AS datetime2))
    INSERT INTO vacaciones (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
    VALUES ('401004', 'Ana Maria Gutierrez Rondon', 'Digital Strategy & Business', CAST('2026-02-16T00:00:00' AS datetime2), CAST('2026-02-27T00:00:00' AS datetime2), 10, 'Descanso por acumulacion de dias del periodo anterior.', 'APROBADA', (SELECT id FROM usuarios WHERE usuario = 'storres'));

IF NOT EXISTS (SELECT 1 FROM vacaciones WHERE numero_empleado = '401002' AND fecha_inicio = CAST('2026-06-09T00:00:00' AS datetime2))
    INSERT INTO vacaciones (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
    VALUES ('401002', 'Diana Carolina Herrera Montoya', 'Application Services (AS)', CAST('2026-06-09T00:00:00' AS datetime2), CAST('2026-06-20T00:00:00' AS datetime2), 10, 'Solicitud para semana de Corpus Christi y dias adicionales.', 'PENDIENTE', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM vacaciones WHERE numero_empleado = '401005' AND fecha_inicio = CAST('2026-07-20T00:00:00' AS datetime2))
    INSERT INTO vacaciones (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
    VALUES ('401005', 'Miguel Angel Ospina Bedoya', 'Digital Strategy & Business', CAST('2026-07-20T00:00:00' AS datetime2), CAST('2026-07-31T00:00:00' AS datetime2), 10, 'Aprovechamiento de temporada de fiestas patrias para viaje al exterior.', 'PENDIENTE', (SELECT id FROM usuarios WHERE usuario = 'storres'));

IF NOT EXISTS (SELECT 1 FROM vacaciones WHERE numero_empleado = '401006' AND fecha_inicio = CAST('2026-08-03T00:00:00' AS datetime2))
    INSERT INTO vacaciones (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
    VALUES ('401006', 'Laura Daniela Suarez Castillo', 'Infrastructure & Cloud', CAST('2026-08-03T00:00:00' AS datetime2), CAST('2026-08-07T00:00:00' AS datetime2), 5, 'Vacaciones cortas por asuntos personales.', 'PENDIENTE', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM vacaciones WHERE numero_empleado = '401003' AND fecha_inicio = CAST('2026-04-13T00:00:00' AS datetime2))
    INSERT INTO vacaciones (numero_empleado, nombre_empleado, unidad_negocio, fecha_inicio, fecha_fin, total_dias, comentario, estado, lider_id)
    VALUES ('401003', 'Jhon Fredy Parra Lopez', 'Application Services (AS)', CAST('2026-04-13T00:00:00' AS datetime2), CAST('2026-04-17T00:00:00' AS datetime2), 5, 'Solicitadas durante semana de lanzamiento del proyecto. No viable.', 'RECHAZADA', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM incapacidades WHERE numero_empleado = '401003' AND fecha_inicio = CAST('2026-04-07T00:00:00' AS datetime2))
    INSERT INTO incapacidades (numero_empleado, nombre_empleado, unidad_negocio, tipo_incapacidad, entidad_salud, categoria, fecha_inicio, fecha_fin, total_dias, diagnostico, estado, archivo_adjunto, lider_id)
    VALUES ('401003', 'Jhon Fredy Parra Lopez', 'Application Services (AS)', 'Enfermedad General', 'EPS Sanitas', 'Incapacidad Ambulatoria', CAST('2026-04-07T00:00:00' AS datetime2), CAST('2026-04-11T00:00:00' AS datetime2), 5, 'CIE-10 J069: Infeccion aguda de vias respiratorias superiores.', 'APROBADA', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM incapacidades WHERE numero_empleado = '401006' AND fecha_inicio = CAST('2026-03-10T00:00:00' AS datetime2))
    INSERT INTO incapacidades (numero_empleado, nombre_empleado, unidad_negocio, tipo_incapacidad, entidad_salud, categoria, fecha_inicio, fecha_fin, total_dias, diagnostico, estado, archivo_adjunto, lider_id)
    VALUES ('401006', 'Laura Daniela Suarez Castillo', 'Infrastructure & Cloud', 'Accidente de Trabajo', 'Nueva EPS', 'Incapacidad con hospitalizacion', CAST('2026-03-10T00:00:00' AS datetime2), CAST('2026-03-24T00:00:00' AS datetime2), 15, 'CIE-10 S820: Fractura de rotula. Accidente en via publica.', 'APROBADA', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM incapacidades WHERE numero_empleado = '401001' AND fecha_inicio = CAST('2026-05-19T00:00:00' AS datetime2))
    INSERT INTO incapacidades (numero_empleado, nombre_empleado, unidad_negocio, tipo_incapacidad, entidad_salud, categoria, fecha_inicio, fecha_fin, total_dias, diagnostico, estado, archivo_adjunto, lider_id)
    VALUES ('401001', 'Carlos Andres Medina Vargas', 'Application Services (AS)', 'Enfermedad General', 'EPS Sura', 'Incapacidad Ambulatoria', CAST('2026-05-19T00:00:00' AS datetime2), CAST('2026-05-21T00:00:00' AS datetime2), 3, 'CIE-10 K297: Gastritis cronica. Restriccion de actividades.', 'PENDIENTE', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM incapacidades WHERE numero_empleado = '401007' AND fecha_inicio = CAST('2026-05-15T00:00:00' AS datetime2))
    INSERT INTO incapacidades (numero_empleado, nombre_empleado, unidad_negocio, tipo_incapacidad, entidad_salud, categoria, fecha_inicio, fecha_fin, total_dias, diagnostico, estado, archivo_adjunto, lider_id)
    VALUES ('401007', 'Ricardo Ivan Morales Pedraza', 'Infrastructure & Cloud', 'Enfermedad General', 'Compensar EPS', 'Incapacidad Ambulatoria', CAST('2026-05-15T00:00:00' AS datetime2), CAST('2026-05-19T00:00:00' AS datetime2), 5, 'CIE-10 M545: Lumbago no especificado. Reposo y fisioterapia.', 'PENDIENTE', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM incapacidades WHERE numero_empleado = '401004' AND fecha_inicio = CAST('2026-02-02T00:00:00' AS datetime2))
    INSERT INTO incapacidades (numero_empleado, nombre_empleado, unidad_negocio, tipo_incapacidad, entidad_salud, categoria, fecha_inicio, fecha_fin, total_dias, diagnostico, estado, archivo_adjunto, lider_id)
    VALUES ('401004', 'Ana Maria Gutierrez Rondon', 'Digital Strategy & Business', 'Enfermedad General', 'EPS Famisanar', 'Incapacidad Ambulatoria', CAST('2026-02-02T00:00:00' AS datetime2), CAST('2026-02-04T00:00:00' AS datetime2), 3, 'CIE-10 J00X: Rinofaringitis aguda. Documento presentado extemporaneamente.', 'RECHAZADA', NULL, (SELECT id FROM usuarios WHERE usuario = 'storres'));

IF NOT EXISTS (SELECT 1 FROM calamidades WHERE numero_empleado = '401002' AND fecha_inicio = CAST('2026-01-20T00:00:00' AS datetime2))
    INSERT INTO calamidades (numero_empleado, nombre_empleado, unidad_negocio, descripcion, fecha_inicio, fecha_fin, total_dias, comentario, estado, archivo_adjunto, lider_id)
    VALUES ('401002', 'Diana Carolina Herrera Montoya', 'Application Services (AS)', 'Fallecimiento de familiar en primer grado de consanguinidad.', CAST('2026-01-20T00:00:00' AS datetime2), CAST('2026-01-23T00:00:00' AS datetime2), 4, 'Licencia de luto segun reglamento interno.', 'APROBADA', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM calamidades WHERE numero_empleado = '401005' AND fecha_inicio = CAST('2026-04-22T00:00:00' AS datetime2))
    INSERT INTO calamidades (numero_empleado, nombre_empleado, unidad_negocio, descripcion, fecha_inicio, fecha_fin, total_dias, comentario, estado, archivo_adjunto, lider_id)
    VALUES ('401005', 'Miguel Angel Ospina Bedoya', 'Digital Strategy & Business', 'Inundacion de vivienda propia por desbordamiento de quebrada.', CAST('2026-04-22T00:00:00' AS datetime2), CAST('2026-04-24T00:00:00' AS datetime2), 3, 'Se adjunta certificado de la Alcaldia y fotografias del siniestro.', 'APROBADA', NULL, (SELECT id FROM usuarios WHERE usuario = 'storres'));

IF NOT EXISTS (SELECT 1 FROM calamidades WHERE numero_empleado = '401007' AND fecha_inicio = CAST('2026-05-12T00:00:00' AS datetime2))
    INSERT INTO calamidades (numero_empleado, nombre_empleado, unidad_negocio, descripcion, fecha_inicio, fecha_fin, total_dias, comentario, estado, archivo_adjunto, lider_id)
    VALUES ('401007', 'Ricardo Ivan Morales Pedraza', 'Infrastructure & Cloud', 'Hospitalizacion urgente de hijo menor de edad por cuadro febril severo.', CAST('2026-05-12T00:00:00' AS datetime2), CAST('2026-05-14T00:00:00' AS datetime2), 3, 'Se adjunta epicrisis del hospital y registro civil del menor.', 'PENDIENTE', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM calamidades WHERE numero_empleado = '401006' AND fecha_inicio = CAST('2026-05-16T00:00:00' AS datetime2))
    INSERT INTO calamidades (numero_empleado, nombre_empleado, unidad_negocio, descripcion, fecha_inicio, fecha_fin, total_dias, comentario, estado, archivo_adjunto, lider_id)
    VALUES ('401006', 'Laura Daniela Suarez Castillo', 'Infrastructure & Cloud', 'Incendio parcial en vivienda arrendada. Perdida de enseres.', CAST('2026-05-16T00:00:00' AS datetime2), CAST('2026-05-17T00:00:00' AS datetime2), 2, 'Reporte de Bomberos adjunto para validacion.', 'PENDIENTE', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM calamidades WHERE numero_empleado = '401003' AND fecha_inicio = CAST('2026-03-03T00:00:00' AS datetime2))
    INSERT INTO calamidades (numero_empleado, nombre_empleado, unidad_negocio, descripcion, fecha_inicio, fecha_fin, total_dias, comentario, estado, archivo_adjunto, lider_id)
    VALUES ('401003', 'Jhon Fredy Parra Lopez', 'Application Services (AS)', 'Calamidad domestica por problemas de convivencia.', CAST('2026-03-03T00:00:00' AS datetime2), CAST('2026-03-04T00:00:00' AS datetime2), 2, 'No cumple los criterios establecidos en el reglamento interno.', 'RECHAZADA', NULL, (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401001' AND fecha_cumpleanio = CAST('2026-03-15T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401001', 'Carlos Andres Medina Vargas', 'Application Services (AS)', CAST('2026-03-15T00:00:00' AS datetime2), 'Solicitud de medio dia libre por beneficio de cumpleanos institucional.', 'APROBADA', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401004' AND fecha_cumpleanio = CAST('2026-02-28T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401004', 'Ana Maria Gutierrez Rondon', 'Digital Strategy & Business', CAST('2026-02-28T00:00:00' AS datetime2), 'Beneficio anual de dia de cumpleanos. Tramite realizado con 5 dias de anticipacion.', 'APROBADA', (SELECT id FROM usuarios WHERE usuario = 'storres'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401007' AND fecha_cumpleanio = CAST('2026-04-10T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401007', 'Ricardo Ivan Morales Pedraza', 'Infrastructure & Cloud', CAST('2026-04-10T00:00:00' AS datetime2), 'Solicitud de dia libre reglamentario por fecha de nacimiento.', 'APROBADA', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401002' AND fecha_cumpleanio = CAST('2026-06-03T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401002', 'Diana Carolina Herrera Montoya', 'Application Services (AS)', CAST('2026-06-03T00:00:00' AS datetime2), 'Solicitud anticipada del beneficio de cumpleanos para junio.', 'PENDIENTE', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401005' AND fecha_cumpleanio = CAST('2026-07-18T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401005', 'Miguel Angel Ospina Bedoya', 'Digital Strategy & Business', CAST('2026-07-18T00:00:00' AS datetime2), 'Beneficio de cumpleanos solicitado con un mes de anticipacion.', 'PENDIENTE', (SELECT id FROM usuarios WHERE usuario = 'storres'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401006' AND fecha_cumpleanio = CAST('2026-08-22T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401006', 'Laura Daniela Suarez Castillo', 'Infrastructure & Cloud', CAST('2026-08-22T00:00:00' AS datetime2), 'Solicitud de dia libre por cumpleanos. Primera solicitud del beneficio.', 'PENDIENTE', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));

IF NOT EXISTS (SELECT 1 FROM dias_cumpleanios WHERE numero_empleado = '401003' AND fecha_cumpleanio = CAST('2026-05-05T00:00:00' AS datetime2))
    INSERT INTO dias_cumpleanios (numero_empleado, nombre_empleado, unidad_negocio, fecha_cumpleanio, comentario, estado, lider_id)
    VALUES ('401003', 'Jhon Fredy Parra Lopez', 'Application Services (AS)', CAST('2026-05-05T00:00:00' AS datetime2), 'Rechazada: solicitud enviada el mismo dia del cumpleanos.', 'RECHAZADA', (SELECT id FROM usuarios WHERE usuario = 'nvivas'));
