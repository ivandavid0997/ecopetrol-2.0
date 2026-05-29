package com.nttdata.ecopetrol.talento.dto.response;

import com.nttdata.ecopetrol.talento.enums.Estado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VacacionesRsDTO {

    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer totalDias;
    private Estado estado;
}
