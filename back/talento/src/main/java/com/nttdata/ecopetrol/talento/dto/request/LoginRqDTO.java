package com.nttdata.ecopetrol.talento.dto.request;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRqDTO {
    private String usuario;
    private String contrasena;
}
