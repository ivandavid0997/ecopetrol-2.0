package com.nttdata.ecopetrol.talento.dto.request;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CierreMesResultadoDTO {

    private String tipoSolicitud;
    private Long id;
    private String nombreEmpleado;
    private String estadoEnvio;
    private String mensaje;
}
