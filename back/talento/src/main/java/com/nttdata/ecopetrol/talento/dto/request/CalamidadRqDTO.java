package com.nttdata.ecopetrol.talento.dto.request;

import com.nttdata.ecopetrol.talento.enums.Estado;
import com.nttdata.ecopetrol.talento.model.Usuario;
import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalamidadRqDTO {
    private String numeroEmpleado;
    private String nombreEmpleado;
    private String unidadNegocio;
    private Date fechaInicio;
    private Date fechaFin;
    private Integer totalDias;
    private Long lider;
    private Date fechaCreacion;
    private String archivoAdjunto;
    private String comentario;
    private String descripcion;
    private String estado;
}
