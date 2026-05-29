package com.nttdata.ecopetrol.talento.dto.request;
import com.nttdata.ecopetrol.talento.enums.Rol;
import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRqDTO {
    private String nombre;
    private String usuario;
    private Rol rol;
    private Long numeroEmpleado;
    private String unidadNegocio;
    private String password;
}
