CREATE OR ALTER VIEW vw_todas_solicitudes AS
SELECT
    id,
    numero_empleado,
    nombre_empleado,
    unidad_negocio,
    'CALAMIDAD' AS tipo_solicitud,
    descripcion AS descripcion,
    fecha_creacion,
    fecha_inicio,
    fecha_fin,
    estado
FROM calamidades
UNION ALL
SELECT
    id,
    numero_empleado,
    nombre_empleado,
    unidad_negocio,
    'CUMPLEANIO' AS tipo_solicitud,
    comentario AS descripcion,
    fecha_creacion,
    fecha_cumpleanio AS fecha_inicio,
    fecha_cumpleanio AS fecha_fin,
    estado
FROM dias_cumpleanios
UNION ALL
SELECT
    id,
    numero_empleado,
    nombre_empleado,
    unidad_negocio,
    'INCAPACIDAD' AS tipo_solicitud,
    diagnostico AS descripcion,
    fecha_creacion,
    fecha_inicio,
    fecha_fin,
    estado
FROM incapacidades
UNION ALL
SELECT
    id,
    numero_empleado,
    nombre_empleado,
    unidad_negocio,
    'VACACIONES' AS tipo_solicitud,
    comentario AS descripcion,
    fecha_creacion,
    fecha_inicio,
    fecha_fin,
    estado
FROM vacaciones;
