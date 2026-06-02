-- ============================================================
-- V2__add_performance_indexes.sql
-- Migración Flyway: Índices secundarios para mejora de rendimiento
-- Fecha: 2026-06-01
-- Bug: R-01
-- ============================================================

-- -----------------------------------------------
-- Tabla: vacaciones
-- -----------------------------------------------

-- Índice sobre 'estado': usado en listarVacacionesPendientes()
-- y en cualquier filtro WHERE estado = 'PENDIENTE'
CREATE INDEX IF NOT EXISTS idx_vacaciones_estado
    ON vacaciones (estado);

-- Índice sobre 'numero_empleado': usado en validaciones de días
-- disponibles y consultas por empleado
CREATE INDEX IF NOT EXISTS idx_vacaciones_numero_empleado
    ON vacaciones (numero_empleado);

-- Índice sobre 'lider_id': usado en LEFT JOIN FETCH con líder (R-04)
CREATE INDEX IF NOT EXISTS idx_vacaciones_lider_id
    ON vacaciones (lider_id);

-- -----------------------------------------------
-- Tabla: calamidades
-- -----------------------------------------------

-- Índice sobre 'numero_empleado': consultas y filtros por empleado
CREATE INDEX IF NOT EXISTS idx_calamidades_numero_empleado
    ON calamidades (numero_empleado);

-- -----------------------------------------------
-- Tabla: incapacidades
-- -----------------------------------------------

-- Índice sobre 'numero_empleado': consultas y filtros por empleado
CREATE INDEX IF NOT EXISTS idx_incapacidades_numero_empleado
    ON incapacidades (numero_empleado);
