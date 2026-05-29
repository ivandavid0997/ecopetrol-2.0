package com.nttdata.ecopetrol.talento.services;

import com.nttdata.ecopetrol.talento.dto.request.CumpleanioRqDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CumpleanioService {
    ResponseEntity<?> crearCumpleanio(CumpleanioRqDTO dto);
    ResponseEntity<?> listarCumpleanios();
    ResponseEntity<?> listarCumpleaniosPendientes();
    ResponseEntity<?> borrarCumpleanio(Long id);
    ResponseEntity<?> aprobarCumpleanio(Long id, String usuarioActual, HttpServletRequest request);
    ResponseEntity<?> rechazarCumpleanio(Long id, String usuarioActual, HttpServletRequest request);
}
