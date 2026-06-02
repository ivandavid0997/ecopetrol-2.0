-- ============================================================
-- V3__add_check_constraints.sql
-- Migración Flyway: CHECK CONSTRAINTs + Auditoría + Seguridad
-- Fecha: 2026-06-01
-- Bugs: S-01, S-02, S-03
-- ============================================================

-- ============================================================
-- S-01: Restricción de valores para rol en usuarios
-- ============================================================

ALTER TABLE usuarios
ADD CONSTRAINT chk_rol CHECK (rol IN ('EMPLEADO', 'LIDER', 'PEOPLE'));

-- ============================================================
-- S-02: Restricción de valores para estado
-- ============================================================

ALTER TABLE vacaciones
ADD CONSTRAINT chk_vac_estado CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA'));

ALTER TABLE incapacidades
ADD CONSTRAINT chk_inc_estado CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA'));

ALTER TABLE calamidades
ADD CONSTRAINT chk_cal_estado CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA'));

ALTER TABLE dias_cumpleanios
ADD CONSTRAINT chk_diac_estado CHECK (estado IN ('PENDIENTE', 'APROBADA', 'RECHAZADA'));

-- ============================================================
-- S-03: Auditoría de acciones sensibles
-- ============================================================

CREATE TABLE auditoria_solicitudes (
    id            BIGINT IDENTITY(1,1) PRIMARY KEY,
    tabla_origen  VARCHAR(50)  NOT NULL,
    registro_id   BIGINT       NOT NULL,
    accion        VARCHAR(20)  NOT NULL
                               CHECK (accion IN ('CREAR', 'APROBAR', 'RECHAZAR', 'MODIFICAR')),
    usuario_id    BIGINT       REFERENCES usuarios(id) ON DELETE SET NULL,
    ip_origen     VARCHAR(45),
    fecha         DATETIME2    NOT NULL DEFAULT SYSUTCDATETIME(),
    detalle       VARCHAR(MAX)
);

-- ============================================================
-- S-03: Columnas de seguridad para bloqueo de cuentas
-- ============================================================

ALTER TABLE usuarios
ADD password VARCHAR(255) NOT NULL DEFAULT '';

ALTER TABLE usuarios
ADD intentos_fallidos INT NOT NULL DEFAULT 0;

ALTER TABLE usuarios
ADD bloqueado_hasta DATETIME NULL;
