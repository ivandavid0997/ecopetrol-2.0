package com.nttdata.ecopetrol.talento.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nttdata.ecopetrol.talento.dto.request.VacacionesRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.VacacionesRsDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface VacacionesService {

    ResponseEntity<?> aprobarVacaciones(Long id, String usuarioActual, HttpServletRequest request);
    ResponseEntity<?> listarVacaciones();
    ResponseEntity<?> crearVacaciones(VacacionesRqDTO dto);
    ResponseEntity<?> borrarVacaciones(Long id);
    ResponseEntity<?> rechazarVacaciones(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException;
    ResponseEntity<?> listarVacacionesPendientes();

}
