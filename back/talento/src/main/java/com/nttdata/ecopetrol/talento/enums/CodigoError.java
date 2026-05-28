package com.nttdata.ecopetrol.talento.enums;

public enum CodigoError {
    USUARIO_NO_ENCONTRADO("TLNT-001", "Usuario no encontrado", "Verifique usuario o cámbielo."),
    CREDENCIALES_INVALIDAS("TLNT-002", "Credenciales inválidas", "Revise usuario y contraseña."),
    PERMISO_DENEGADO("TLNT-003", "Permiso denegado", "Solicite acceso a su líder."),
    LOGIN_FALLIDO("TLNT-004", "Falta campos login", "Revisa los campos enviados al login."),
    SIN_DIAS_SUFI("TLNT-005", "Sin días suficientes", "Consulte su saldo de días."),
    VALIDACION_INTERRUP("TLNT-006", "Validación interrumpida", "Reintente la operación."),
    ERROR_VALIDACION("TLNT-007", "Error inesperado validación", "Reporte al soporte.");


    private final String codigo;
    private final String descripcion;
    private final String solucion;

    CodigoError(String codigo, String descripcion, String solucion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.solucion = solucion;
    }
    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public String getSolucion() { return solucion; }
}
