package com.nttdata.ecopetrol.talento.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncapacidadRqDTO {
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private String tipoIncapacidad;
    private String entidadSalud;
    private String categoria;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer totalDias;
    private String diagnostico;
    private String estado;
    private String archivoAdjunto;
    private Date fechaCreacion;
}
