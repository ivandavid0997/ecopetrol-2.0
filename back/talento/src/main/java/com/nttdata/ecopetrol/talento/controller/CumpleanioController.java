package com.nttdata.ecopetrol.talento.controller;

import com.nttdata.ecopetrol.talento.dto.request.CumpleanioRqDTO;
import com.nttdata.ecopetrol.talento.services.CumpleanioService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cumpleanio")
public class CumpleanioController {

    private final CumpleanioService cumpleanioService;
    private static final Logger logger = LoggerFactory.getLogger(CumpleanioController.class);

    public CumpleanioController(CumpleanioService cumpleanioService) {
        this.cumpleanioService = cumpleanioService;
    }

    @PostMapping("/crearDiaCumpleanio")
    public ResponseEntity<?> crear(@RequestBody CumpleanioRqDTO dto) {
        logger.info("Creando registro de día cumpleaños para empleado {}", dto.getNumeroEmpleado());
        return cumpleanioService.crearCumpleanio(dto);
    }

    @GetMapping("/listarDiasCumpleanios")
    public ResponseEntity<?> listar() {
        logger.info("Listando todos los días de cumpleaños");
        return cumpleanioService.listarCumpleanios();
    }

    @GetMapping("/listarDiasCumpleaniosPendientes")
    public ResponseEntity<?> listarPendientes() {
        logger.info("Listando días de cumpleaños pendientes");
        return cumpleanioService.listarCumpleaniosPendientes();
    }

    @DeleteMapping("/borrarDiaCumpleanio/{id}")
    public ResponseEntity<?> borrar(@PathVariable Long id) {
        logger.info("Eliminando registro de cumpleaños con id {}", id);
        return cumpleanioService.borrarCumpleanio(id);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/aprobarDiaCumpleanio/{id}")
    public ResponseEntity<?> aprobar(@PathVariable Long id, HttpServletRequest request) {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando aprobar día cumpleaños [id={}, usuario_aprobador={}]", id, usuarioActual);
        return cumpleanioService.aprobarCumpleanio(id, usuarioActual, request);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/rechazarDiaCumpleanio/{id}")
    public ResponseEntity<?> rechazar(@PathVariable Long id, HttpServletRequest request) {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando rechazar día cumpleaños [id={}, usuario_aprobador={}]", id, usuarioActual);
        return cumpleanioService.rechazarCumpleanio(id, usuarioActual, request);
    }
}
