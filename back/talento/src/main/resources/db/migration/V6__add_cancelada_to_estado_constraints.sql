-- ============================================================
-- V6__add_cancelada_to_estado_constraints.sql
-- Migración Flyway: Añade CANCELADA a los CHECK CONSTRAINTs de estado
-- Fecha: 2026-06-02
-- Bug: M-03 (Estado enum ahora incluye CANCELADA)
-- ============================================================
-- Nota: V3 ya aplicó los CHECK CONSTRAINTs sin CANCELADA.
-- Esta migración los reemplaza para alinear la BD con el enum Estado.
-- En MySQL/MariaDB los CHECK CONSTRAINTs se reemplazan con DROP + ADD.
-- ============================================================

ALTER TABLE vacaciones
DROP CONSTRAINT chk_vac_estado;

ALTER TABLE vacaciones
ADD CONSTRAINT chk_vac_estado
    CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CANCELADA'));

-- --------------------------------------------------------

ALTER TABLE incapacidades
DROP CONSTRAINT chk_inc_estado;

ALTER TABLE incapacidades
ADD CONSTRAINT chk_inc_estado
    CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CANCELADA'));

-- --------------------------------------------------------

ALTER TABLE calamidades
DROP CONSTRAINT chk_cal_estado;

ALTER TABLE calamidades
ADD CONSTRAINT chk_cal_estado
    CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CANCELADA'));

-- --------------------------------------------------------

ALTER TABLE dias_cumpleanios
DROP CONSTRAINT chk_diac_estado;

ALTER TABLE dias_cumpleanios
ADD CONSTRAINT chk_diac_estado
    CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA', 'CANCELADA'));

-- --------------------------------------------------------
-- Actualizar también auditoria_solicitudes para incluir CANCELAR
-- --------------------------------------------------------

ALTER TABLE auditoria_solicitudes
DROP CONSTRAINT IF EXISTS auditoria_solicitudes_chk1;

-- En SQL Server el check inline no tiene nombre explícito; se añade uno nombrado:
ALTER TABLE auditoria_solicitudes
ADD CONSTRAINT chk_auditoria_accion
    CHECK (accion IN ('CREAR', 'APROBAR', 'RECHAZAR', 'CANCELAR', 'MODIFICAR'));
