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
public class CalamidadRsDTO {
    private Long id;
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaEvento;
    private Integer totalDias;
    private String motivo;
    private Estado estado;
}
