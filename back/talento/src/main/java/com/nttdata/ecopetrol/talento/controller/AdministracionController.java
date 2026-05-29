package com.nttdata.ecopetrol.talento.controller;

import com.nttdata.ecopetrol.talento.services.AdministracionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/administracion")
public class AdministracionController {

    private final AdministracionService administracionService;

    public AdministracionController(AdministracionService administracionService) {
        this.administracionService = administracionService;
    }

    @PreAuthorize("hasRole('PEOPLE')")
    @GetMapping("/consolidadoSolicitudes")
    public ResponseEntity<?> getSolicitudesByFechaCreacion() {
        return ResponseEntity.ok(administracionService.listarConsolidadoSolicitud());
    }

    @PreAuthorize("hasRole('PEOPLE')")
    @PostMapping("/cierreMes")
    public ResponseEntity<?> cierreMes() {
        return ResponseEntity.ok(administracionService.cierreMes());
    }
}