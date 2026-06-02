package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.dto.request.UsuarioRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.UsuarioRsDTO;
import com.nttdata.ecopetrol.talento.model.Usuario;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.services.UsuarioService;
import com.nttdata.ecopetrol.talento.utils.AesUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioServiceImpl.class);

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ResponseEntity<?> crearUsuario(UsuarioRqDTO dto) {
        try {
            Usuario user = new Usuario();
            user.setNombre(dto.getNombre());
            user.setUsuario(dto.getUsuario());
            user.setRol(dto.getRol());
            user.setNumeroEmpleado(dto.getNumeroEmpleado() == null ? null : String.valueOf(dto.getNumeroEmpleado()));
            user.setUnidadNegocio(dto.getUnidadNegocio());
            user.setPassword(AesUtil.encrypt(dto.getPassword()));
            user.setIntentosFallidos(0);
            user.setBloqueadoHasta(null);

            usuarioRepository.save(user);
            logger.info("Usuario '{}' creado correctamente", user.getUsuario());
            return ResponseEntity.ok(mapToDto(user));
        } catch (Exception e) {
            logger.error("Error creando usuario: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear usuario", "detalle", e.getMessage()));
        }
    }

    @Override
    public ResponseEntity<?> listarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<UsuarioRsDTO> dtos = usuarios.stream().map(this::mapToDto).collect(Collectors.toList());
        logger.info("Listando usuarios (total: {})", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<?> obtenerUsuarioPorId(Long id) {
        Usuario user = usuarioRepository.findById(id).orElse(null);
        if(user == null) {
            logger.warn("Usuario no encontrado para ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }
        return ResponseEntity.ok(mapToDto(user));
    }

    @Override
    public ResponseEntity<?> eliminarUsuario(Long id) {
        if(!usuarioRepository.existsById(id)) {
            logger.warn("Intento de eliminar usuario inexistente ID {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }
        usuarioRepository.deleteById(id);
        logger.info("Usuario eliminado ID {}", id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> buscarUsuariosPorRol(String rol) {
        List<Usuario> lista = usuarioRepository.findByRol(rol);
        List<UsuarioRsDTO> dtos = lista.stream().map(this::mapToDto).collect(Collectors.toList());
        logger.info("Buscando usuarios con rol '{}', total encontrados: {}", rol, dtos.size());
        return ResponseEntity.ok(dtos);
    }

    private UsuarioRsDTO mapToDto(Usuario user) {
        return UsuarioRsDTO.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .usuario(user.getUsuario())
                .rol(user.getRol())
                .numeroEmpleado(user.getNumeroEmpleado() == null ? null : Long.valueOf(user.getNumeroEmpleado()))
                .unidadNegocio(user.getUnidadNegocio())
                .intentosFallidos(user.getIntentosFallidos())
                .bloqueadoHasta(user.getBloqueadoHasta())
                .build();
    }
}
