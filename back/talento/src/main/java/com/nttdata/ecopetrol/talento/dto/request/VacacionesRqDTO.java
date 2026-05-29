package com.nttdata.ecopetrol.talento.dto.request;

import java.util.Date;

import com.nttdata.ecopetrol.talento.enums.Estado;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacacionesRqDTO {

    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer totalDias;
    private Estado estado;
    private String comentario;
    private Long liderId;
}
