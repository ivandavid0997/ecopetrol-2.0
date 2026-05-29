package com.nttdata.ecopetrol.talento.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.ecopetrol.talento.dto.request.IncapacidadRqDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface IncapacidadService {
    ResponseEntity<?> crearIncapacidad(IncapacidadRqDTO dto);
    ResponseEntity<?> listarIncapacidades();
    ResponseEntity<?> borrarIncapacidad(Long id);
    ResponseEntity<?> aprobarIncapacidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException;
    ResponseEntity<?> rechazarIncapacidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException;
    ResponseEntity<?> listarIncapacidadesPendientes();
}
