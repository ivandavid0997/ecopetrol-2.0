-- ============================================================
-- V4__soft_delete_usuarios.sql
-- Migración Flyway: Soft Delete para tabla usuarios
-- Fecha: 2026-06-01
-- Bug: C-02
-- ============================================================

-- Agregar columna de soft delete
-- 1 = activo, 0 = eliminado lógicamente
ALTER TABLE usuarios
ADD activo BIT NOT NULL DEFAULT 1;

-- Índice para filtrar usuarios activos eficientemente
CREATE INDEX idx_usuarios_activo ON usuarios(activo);
