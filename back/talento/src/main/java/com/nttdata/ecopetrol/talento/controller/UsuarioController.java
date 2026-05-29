package com.nttdata.ecopetrol.talento.controller;

import com.nttdata.ecopetrol.talento.dto.request.UsuarioRqDTO;
import com.nttdata.ecopetrol.talento.services.UsuarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody UsuarioRqDTO dto) {
        logger.info("Creando nuevo usuario con usuario '{}'", dto.getUsuario());
        return usuarioService.crearUsuario(dto);
    }

    @GetMapping("/listaUsuarios")
    public ResponseEntity<?> listar() {
        logger.info("Listando usuarios");
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        logger.info("Obteniendo usuario por id {}", id);
        return usuarioService.obtenerUsuarioPorId(id);
    }

    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        logger.info("Eliminando usuario por id {}", id);
        return usuarioService.eliminarUsuario(id);
    }

    @GetMapping("/porRol")
    public ResponseEntity<?> buscarPorRol(@RequestParam String rol) {
        logger.info("Buscando usuarios con rol '{}'", rol);
        return usuarioService.buscarUsuariosPorRol(rol);
    }
}