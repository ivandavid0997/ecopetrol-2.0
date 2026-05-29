package com.nttdata.ecopetrol.talento.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.ecopetrol.talento.dto.request.CalamidadRqDTO;
import com.nttdata.ecopetrol.talento.services.CalamidadService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calamidad")
public class CalamidadController {

    private final CalamidadService calamidadService;
    private static final Logger logger = LoggerFactory.getLogger(CalamidadController.class);

    public CalamidadController(CalamidadService calamidadService) {
        this.calamidadService = calamidadService;
    }

    @PostMapping("/crearCalamidad")
    public ResponseEntity<?> crearCalamidad(@RequestBody CalamidadRqDTO dto) {
        logger.info("Creando calamidad para empleado {}", dto.getNumeroEmpleado());
        return calamidadService.crearCalamidad(dto);
    }

    @GetMapping("/listarCalamidades")
    public ResponseEntity<?> listarCalamidades() {
        logger.info("Listando todas las calamidades");
        return calamidadService.listarCalamidades();
    }

    @DeleteMapping("/borrarCalamidad/{id}")
    public ResponseEntity<?> borrarCalamidad(@PathVariable Long id) {
        logger.info("Intento de borrar calamidad con id {}", id);
        return calamidadService.borrarCalamidad(id);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/aprobarCalamidad/{id}")
    public ResponseEntity<?> aprobarCalamidad(@PathVariable Long id, HttpServletRequest request) throws JsonProcessingException {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando aprobar calamidad [id={}, usuario_aprobador={}]", id, usuarioActual);
        return calamidadService.aprobarCalamidad(id, usuarioActual,request);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/rechazarCalamidad/{id}")
    public ResponseEntity<?> rechazarCalamidad(@PathVariable Long id, HttpServletRequest request) throws JsonProcessingException {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando rechazar calamidad [id={}, usuario_aprobador={}]", id, usuarioActual);
        return calamidadService.rechazarCalamidad(id, usuarioActual,request);
    }

    @GetMapping("/listarCalamidadesPendientes")
    public ResponseEntity<?> listarPendientes() {
        logger.info("Listando todas las calamidades pendientes");
        return calamidadService.listarCalamidadesPendientes();
    }
}
