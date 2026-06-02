package com.nttdata.ecopetrol.talento.enums;

public enum CodigoError {
    USUARIO_NO_ENCONTRADO("TLNT-001", "Usuario no encontrado", "Verifique usuario o cámbielo."),
    CREDENCIALES_INVALIDAS("TLNT-002", "Credenciales inválidas", "Revise usuario y contraseña."),
    PERMISO_DENEGADO("TLNT-003", "Permiso denegado", "Solicite acceso a su líder."),
    LOGIN_FALLIDO("TLNT-004", "Falta campos login", "Revisa los campos enviados al login."),
    SIN_DIAS_SUFI("TLNT-005", "Sin días suficientes", "Consulte su saldo de días."),
    VALIDACION_INTERRUP("TLNT-006", "Validación interrumpida", "Reintente la operación."),
    ERROR_VALIDACION("TLNT-007", "Error inesperado validación", "Reporte al soporte."),
    PASSWORD_INCORRECTA(
            "TLNT-008",
            "Contraseña incorrecta",
            "Verifique su contraseña o reinicie si la olvidó."
    ),
    USUARIO_BLOQUEADO(
            "TLNT-009",
            "Usuario bloqueado temporalmente",
            "Espere el tiempo indicado o contacte a soporte si el problema persiste."
    ),
    USUARIO_BLOQUEADO_PERMANENTE(
            "TLNT-010",
            "Usuario bloqueado permanentemente",
            "Contacte a soporte para desbloqueo."
    ),
    INTENTOS_EXCEDIDOS(
            "TLNT-011",
            "Intentos de login excedidos",
            "Espere unos minutos e intente nuevamente."
    ),
    CALAMIDAD_NO_ENCONTRADA(
            "TLNT-012",
            "Calamidad no encontrada",
            "Verifique el identificador de la calamidad."
    ),
    INCAPACIDAD_NO_ENCONTRADA(
            "TLNT-013",
            "Incapacidad no encontrada",
            "Verifique el identificador de la incapacidad."
    ),
    VACACIONES_NO_ENCONTRADAS(
            "TLNT-014",
            "Vacaciones no encontradas",
            "Verifique el identificador del registro de vacaciones."
    ),
    ERROR_CREAR_SOLICITUD(
            "TLNT-015",
            "Error al crear la solicitud",
            "Revise los datos enviados e intente nuevamente."
    );


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
