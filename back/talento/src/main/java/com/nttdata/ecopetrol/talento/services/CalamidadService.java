package com.nttdata.ecopetrol.talento.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.ecopetrol.talento.dto.request.CalamidadRqDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface CalamidadService {
    ResponseEntity<?> crearCalamidad(CalamidadRqDTO dto);
    ResponseEntity<?> listarCalamidades();
    ResponseEntity<?> borrarCalamidad(Long id);
    ResponseEntity<?> aprobarCalamidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException;
    ResponseEntity<?> rechazarCalamidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException;
    ResponseEntity<?> listarCalamidadesPendientes();
}
