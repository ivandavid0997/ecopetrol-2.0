package com.nttdata.ecopetrol.talento.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.ecopetrol.talento.dto.request.IncapacidadRqDTO;
import com.nttdata.ecopetrol.talento.services.IncapacidadService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/incapacidad")
public class IncapacidadController {

    private final IncapacidadService incapacidadService;
    private static final Logger logger = LoggerFactory.getLogger(IncapacidadController.class);

    public IncapacidadController(IncapacidadService incapacidadService) {
        this.incapacidadService = incapacidadService;
    }

    @PostMapping("/crearIncapacidad")
    public ResponseEntity<?> crearIncapacidad(@RequestBody IncapacidadRqDTO dto) {
        logger.info("Creando incapacidad para empleado {}", dto.getNumeroEmpleado());
        return incapacidadService.crearIncapacidad(dto);
    }

    @GetMapping("/listarIncapacidades")
    public ResponseEntity<?> listarIncapacidades() {
        logger.info("Listando todas las incapacidades");
        return incapacidadService.listarIncapacidades();
    }

    @DeleteMapping("/borrarIncapacidad/{id}")
    public ResponseEntity<?> borrarIncapacidad(@PathVariable Long id) {
        logger.info("Intento de borrar incapacidad con id {}", id);
        return incapacidadService.borrarIncapacidad(id);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/aprobarIncapacidad/{id}")
    public ResponseEntity<?> aprobarIncapacidad(@PathVariable Long id, HttpServletRequest request) throws JsonProcessingException {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando aprobar incapacidad [id={}, usuario_aprobador={}]", id, usuarioActual);
        return incapacidadService.aprobarIncapacidad(id, usuarioActual, request);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/rechazarIncapacidad/{id}")
    public ResponseEntity<?> rechazarIncapacidad(@PathVariable Long id, HttpServletRequest request) throws JsonProcessingException {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando rechazar incapacidad [id={}, usuario_aprobador={}]", id, usuarioActual);
        return incapacidadService.rechazarIncapacidad(id, usuarioActual, request);
    }

    @GetMapping("/listarIncapacidadesPendientes")
    public ResponseEntity<?> listarPendientes() {
        logger.info("Listando todas las incapacidades pendientes");
        return incapacidadService.listarIncapacidadesPendientes();
    }
}
