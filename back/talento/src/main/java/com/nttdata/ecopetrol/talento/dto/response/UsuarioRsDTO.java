package com.nttdata.ecopetrol.talento.dto.response;
import com.nttdata.ecopetrol.talento.enums.Rol;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRsDTO {
    private Long id;
    private String nombre;
    private String usuario;
    private Rol rol; // Enum
    private Long numeroEmpleado;
    private String unidadNegocio;
    private int intentosFallidos;
    private LocalDateTime bloqueadoHasta;
}
