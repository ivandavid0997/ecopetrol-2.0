package com.nttdata.ecopetrol.talento.dto.response;

import lombok.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CumpleanioRsDTO {
    private Long id;
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaCumpleanio;
    private String comentario;
    private String estado;
    private Long lider;
    private Date fechaCreacion;
}
