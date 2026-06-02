package com.nttdata.ecopetrol.talento.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.ecopetrol.talento.dto.request.IncapacidadRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.IncapacidadRsDTO;
import com.nttdata.ecopetrol.talento.enums.CodigoError;
import com.nttdata.ecopetrol.talento.enums.Estado;
import com.nttdata.ecopetrol.talento.model.Incapacidad;
import com.nttdata.ecopetrol.talento.model.Usuario;
import com.nttdata.ecopetrol.talento.repository.IncapacidadRepository;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.services.AuditoriaService;
import com.nttdata.ecopetrol.talento.services.IncapacidadService;
import com.nttdata.ecopetrol.talento.utils.AuditHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncapacidadServiceImpl implements IncapacidadService {

    private final IncapacidadRepository incapacidadRepository;
    private final ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl;
    private final NotificacionCorreoServiceImpl notificacionCorreoServiceImpl;
    private final AuditoriaService auditoriaService;
    private final UsuarioRepository usuarioRepository;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(IncapacidadServiceImpl.class);

    public IncapacidadServiceImpl(IncapacidadRepository incapacidadRepository, ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl, NotificacionCorreoServiceImpl notificacionCorreoServiceImpl, AuditoriaService auditoriaService, UsuarioRepository usuarioRepository, ObjectMapper objectMapper) {
        this.incapacidadRepository = incapacidadRepository;
        this.validacionVacacionesServiceImpl = validacionVacacionesServiceImpl;
        this.notificacionCorreoServiceImpl = notificacionCorreoServiceImpl;
        this.auditoriaService = auditoriaService;
        this.usuarioRepository = usuarioRepository;
        this.objectMapper = objectMapper;
    }


    @Override
    public ResponseEntity<?> crearIncapacidad(IncapacidadRqDTO dto) {
        try {
            Incapacidad inc = new Incapacidad();
            inc.setNumeroEmpleado(dto.getNumeroEmpleado());
            inc.setNombreEmpleado(dto.getNombreEmpleado());
            inc.setUnidadNegocio(dto.getUnidadNegocio());
            inc.setTipoIncapacidad(dto.getTipoIncapacidad());
            inc.setEntidadSalud(dto.getEntidadSalud());
            inc.setCategoria(dto.getCategoria());
            inc.setFechaInicio(com.nttdata.ecopetrol.talento.utils.DateMapper.toLocalDate(dto.getFechaInicio()));
            inc.setFechaFin(com.nttdata.ecopetrol.talento.utils.DateMapper.toLocalDate(dto.getFechaFin()));
            inc.setTotalDias(dto.getTotalDias());
            inc.setDiagnostico(dto.getDiagnostico());
            inc.setArchivoAdjunto(dto.getArchivoAdjunto());
            inc.setEstado(Estado.PENDIENTE);

            incapacidadRepository.save(inc);
            logger.info("Solicitud de incapacidad creada para empleado {}", dto.getNumeroEmpleado());
            return ResponseEntity.ok(mapToDto(inc));
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_CREAR_SOLICITUD.getCodigo());
            logger.error("Error creando incapacidad: {}", e.getMessage(), e);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", CodigoError.ERROR_CREAR_SOLICITUD.getDescripcion(),
                            "solucion", CodigoError.ERROR_CREAR_SOLICITUD.getSolucion()));
        }
    }

    @Override
    public ResponseEntity<?> listarIncapacidades() {
        List<Incapacidad> list = incapacidadRepository.findAll();
        List<IncapacidadRsDTO> dtos = list.stream().map(this::mapToDto).collect(Collectors.toList());
        logger.info("Listando todas las incapacidades (total: {})", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<?> borrarIncapacidad(Long id) {
        if (!incapacidadRepository.existsById(id)) {
            MDC.put("codigo_error", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getCodigo());
            logger.warn("Intento de borrar incapacidad inexistente id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getDescripcion(),
                            "solucion", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getSolucion()));
        }
        logger.info("Eliminando incapacidad id={}", id);
        incapacidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> aprobarIncapacidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        Incapacidad inc = incapacidadRepository.findById(id).orElse(null);
        if (inc == null) {
            MDC.put("codigo_error", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getCodigo());
            logger.error("Incapacidad no encontrada [incapacidadId={}]", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getDescripcion(),
                            "solucion", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getSolucion()));
        }

        Incapacidad copiaAntes = new Incapacidad();
        BeanUtils.copyProperties(inc, copiaAntes);

        try {
            logger.info("Validando días disponibles para incapacidad [numeroEmpleado={}, solicitado={}]",
                    inc.getNumeroEmpleado(), inc.getTotalDias());
            boolean valido = validacionVacacionesServiceImpl.validarDiasIncapacidadDisponibles(inc);
            if (!valido) {
                MDC.put("codigo_error", CodigoError.SIN_DIAS_SUFI.getCodigo());
                logger.warn("Empleado sin días suficientes para incapacidad [numeroEmpleado={}, solicitado={}]",
                        inc.getNumeroEmpleado(), inc.getTotalDias());
                MDC.remove("codigo_error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "El empleado no tiene suficientes días de incapacidad disponibles."));
            }
        } catch (InterruptedException e) {
            MDC.put("codigo_error", CodigoError.VALIDACION_INTERRUP.getCodigo());
            logger.error("Validación interrumpida para incapacidad [incapacidadId={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al validar días disponibles (interrumpido)."));
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_VALIDACION.getCodigo());
            logger.error("Error inesperado al validar incapacidad [incapacidadId={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado de validación: " + e.getMessage()));
        }

        inc.setEstado(Estado.APROBADA);
        incapacidadRepository.save(inc);

        AuditHelper.auditarCambio(
                "incapacidades",
                copiaAntes,
                inc,
                inc.getId(),
                "APROBAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        logger.info("Incapacidad aprobada correctamente [incapacidadId={}, numeroEmpleado={}, aprobador={}]",
                id, inc.getNumeroEmpleado(), usuarioActual);

        notificacionCorreoServiceImpl.enviarCorreo(
                "german.edmundo.velasco.bravo@emeal.nttdata.com",
                "Solicitud de Incapacidad Aprobada",
                "Estimado " + inc.getNombreEmpleado() + ", su solicitud ha sido aprobada."
        );
        logger.info("Correo de aprobación enviado a {}", "german.edmundo.velasco.bravo@emeal.nttdata.com");

        return ResponseEntity.ok(mapToDto(inc));
    }

    @Override
    public ResponseEntity<?> rechazarIncapacidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        Incapacidad inc = incapacidadRepository.findById(id).orElse(null);

        if (inc == null) {
            MDC.put("codigo_error", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getCodigo());
            logger.error("Incapacidad no encontrada [incapacidadId={}]", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getDescripcion(),
                            "solucion", CodigoError.INCAPACIDAD_NO_ENCONTRADA.getSolucion()));
        }

        Incapacidad copiaAntes = new Incapacidad();
        BeanUtils.copyProperties(inc, copiaAntes);

        inc.setEstado(Estado.RECHAZADA);
        incapacidadRepository.save(inc);

        AuditHelper.auditarCambio(
                "incapacidades",
                copiaAntes,
                inc,
                inc.getId(),
                "RECHAZAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        notificacionCorreoServiceImpl.enviarCorreo(
                "german.edmundo.velasco.bravo@emeal.nttdata.com",
                "Solicitud de Incapacidad Rechazada",
                "Estimado " + inc.getNombreEmpleado() + ", su solicitud ha sido rechazada."
        );
        logger.info("Incapacidad rechazada correctamente [incapacidadId={}, numeroEmpleado={}, aprobador={}]",
                id, inc.getNumeroEmpleado(), usuarioActual);

        return ResponseEntity.ok(mapToDto(inc));
    }

    @Override
    public ResponseEntity<?> listarIncapacidadesPendientes() {
        List<Incapacidad> todas = incapacidadRepository.findAll();
        List<IncapacidadRsDTO> pendientes = todas.stream()
                .filter(i -> Estado.PENDIENTE.name().equals(i.getEstado().name()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
        logger.info("Listando incapacidades pendientes: {}", pendientes.size());
        return ResponseEntity.ok(pendientes);
    }

    private IncapacidadRsDTO mapToDto(Incapacidad inc) {
        return IncapacidadRsDTO.builder()
                .id(inc.getId())
                .numeroEmpleado(inc.getNumeroEmpleado())
                .nombreEmpleado(inc.getNombreEmpleado())
                .unidadNegocio(inc.getUnidadNegocio())
                .fechaInicio(com.nttdata.ecopetrol.talento.utils.DateMapper.toDate(inc.getFechaInicio()))
                .fechaFin(com.nttdata.ecopetrol.talento.utils.DateMapper.toDate(inc.getFechaFin()))
                .totalDias(inc.getTotalDias())
                .estado(Estado.valueOf(inc.getEstado().name()))
                .build();
    }
}
