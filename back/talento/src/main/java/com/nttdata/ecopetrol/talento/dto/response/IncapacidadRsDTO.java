package com.nttdata.ecopetrol.talento.dto.response;

import com.nttdata.ecopetrol.talento.enums.Estado;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncapacidadRsDTO {
    private Long id;
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer totalDias;
    private Estado estado;
}
