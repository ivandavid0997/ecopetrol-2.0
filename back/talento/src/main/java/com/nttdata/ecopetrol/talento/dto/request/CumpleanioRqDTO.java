package com.nttdata.ecopetrol.talento.dto.request;

import lombok.*;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CumpleanioRqDTO {
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaCumpleanio;
    private String comentario;
    private Long lider;
    private String estado;
    private Date fechaCreacion;
}
