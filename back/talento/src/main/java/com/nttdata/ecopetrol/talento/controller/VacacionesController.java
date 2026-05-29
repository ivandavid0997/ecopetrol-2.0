package com.nttdata.ecopetrol.talento.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.ecopetrol.talento.dto.request.VacacionesRqDTO;
import com.nttdata.ecopetrol.talento.services.VacacionesService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vacaciones")
public class VacacionesController {

    private final VacacionesService vacacionesService;
    private static final Logger logger = LoggerFactory.getLogger(VacacionesController.class);

    public VacacionesController(VacacionesService vacacionesService) {
        this.vacacionesService = vacacionesService;
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/aprobarVacaciones/{id}")
    public ResponseEntity<?> aprobarVacaciones(@PathVariable Long id, HttpServletRequest request) {
        // El controller extrae el usuario y pasa a servicio
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando aprobar vacaciones [vacacionesId={}, usuario_aprobador={}]", id, usuarioActual);

        return vacacionesService.aprobarVacaciones(id, usuarioActual, request);
    }

    @GetMapping("/listarVacaciones")
    public ResponseEntity<?> listarVacaciones() {
        logger.info("Listando todas las vacaciones");
        return vacacionesService.listarVacaciones();
    }

    @PostMapping("/crearVacaciones")
    public ResponseEntity<?> crearVacaciones(@RequestBody VacacionesRqDTO dto) {
        logger.info("Creando solicitud de vacaciones para empleado {}", dto.getNumeroEmpleado());
        return vacacionesService.crearVacaciones(dto);
    }

    @DeleteMapping("/borrarVacaciones/{id}")
    public ResponseEntity<?> borrarVacaciones(@PathVariable Long id) {
        logger.info("Intentando borrar registro de vacaciones con id {}", id);
        return vacacionesService.borrarVacaciones(id);
    }

    @PreAuthorize("hasRole('LIDER')")
    @PostMapping("/rechazarVacaciones/{id}")
    public ResponseEntity<?> rechazarVacaciones(@PathVariable Long id,HttpServletRequest request) throws JsonProcessingException {
        String usuarioActual = SecurityContextHolder.getContext().getAuthentication().getName();
        logger.info("Intentando rechazar vacaciones [vacacionesId={}, usuario_aprobador={}]", id, usuarioActual);
        return vacacionesService.rechazarVacaciones(id, usuarioActual, request);
    }

    @GetMapping("/listarVacacionesPendientes")
    public ResponseEntity<?> listarVacacionesPendientes() {
        logger.info("Listando todas las vacaciones pendientes");
        return vacacionesService.listarVacacionesPendientes();
    }
}
