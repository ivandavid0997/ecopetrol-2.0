package com.nttdata.ecopetrol.talento.services;

import com.nttdata.ecopetrol.talento.dto.request.UsuarioRqDTO;
import org.springframework.http.ResponseEntity;

public interface UsuarioService {
    ResponseEntity<?> crearUsuario(UsuarioRqDTO dto);
    ResponseEntity<?> listarUsuarios();
    ResponseEntity<?> obtenerUsuarioPorId(Long id);
    ResponseEntity<?> eliminarUsuario(Long id);
    ResponseEntity<?> buscarUsuariosPorRol(String rol);
}
