package com.nttdata.ecopetrol.talento.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRsDTO {
    private String token;
    private String usuario;
    private String rol;
}
