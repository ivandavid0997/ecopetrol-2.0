-- =============================================================
--  V5__fix_numero_empleado_type.sql
--  M-01: Estandariza numero_empleado a VARCHAR(30) en tabla usuarios
--  para ser consistente con vacaciones, incapacidades, calamidades
--  y dias_cumpleanios que ya usan VARCHAR(30).
-- =============================================================

ALTER TABLE usuarios
    MODIFY COLUMN numero_empleado VARCHAR(30) NULL;
