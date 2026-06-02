package com.nttdata.ecopetrol.talento.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.ecopetrol.talento.dto.request.CalamidadRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.CalamidadRsDTO;
import com.nttdata.ecopetrol.talento.enums.CodigoError;
import com.nttdata.ecopetrol.talento.enums.Estado;
import com.nttdata.ecopetrol.talento.model.Calamidad;
import com.nttdata.ecopetrol.talento.repository.CalamidadRepository;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.services.AuditoriaService;
import com.nttdata.ecopetrol.talento.services.CalamidadService;
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
public class CalamidadServiceImpl implements CalamidadService {
    private final CalamidadRepository calamidadRepository;
    private final ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl;
    private final NotificacionCorreoServiceImpl notificacionCorreoServiceImpl;
    private final UsuarioRepository usuarioRepository;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CalamidadServiceImpl.class);

    public CalamidadServiceImpl(CalamidadRepository calamidadRepository, ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl, NotificacionCorreoServiceImpl notificacionCorreoServiceImpl, UsuarioRepository usuarioRepository, AuditoriaService auditoriaService, ObjectMapper objectMapper) {
        this.calamidadRepository = calamidadRepository;
        this.validacionVacacionesServiceImpl = validacionVacacionesServiceImpl;
        this.notificacionCorreoServiceImpl = notificacionCorreoServiceImpl;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> crearCalamidad(CalamidadRqDTO dto) {
        try {
            Calamidad cal = new Calamidad();
            cal.setNumeroEmpleado(dto.getNumeroEmpleado());
            cal.setNombreEmpleado(dto.getNombreEmpleado());
            cal.setUnidadNegocio(dto.getUnidadNegocio());
            cal.setDescripcion(dto.getDescripcion());
            cal.setFechaInicio(com.nttdata.ecopetrol.talento.utils.DateMapper.toLocalDate(dto.getFechaInicio()));
            cal.setFechaFin(com.nttdata.ecopetrol.talento.utils.DateMapper.toLocalDate(dto.getFechaFin()));
            cal.setComentario(dto.getComentario());
            cal.setTotalDias(dto.getTotalDias());
            cal.setEstado(Estado.PENDIENTE);
            cal.setArchivoAdjunto(dto.getArchivoAdjunto());
            if (dto.getLider() != null) {
                cal.setLider(usuarioRepository.findById(dto.getLider()).orElse(null));
            }
            calamidadRepository.save(cal);

            logger.info("Calamidad creada para empleado {}", dto.getNumeroEmpleado());
            return ResponseEntity.ok(mapToDto(cal));
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_CREAR_SOLICITUD.getCodigo());
            logger.error("Error creando calamidad: {}", e.getMessage(), e);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", CodigoError.ERROR_CREAR_SOLICITUD.getDescripcion(),
                            "solucion", CodigoError.ERROR_CREAR_SOLICITUD.getSolucion()));
        }
    }

    @Override
    public ResponseEntity<?> listarCalamidades() {
        List<Calamidad> list = calamidadRepository.findAll();
        List<CalamidadRsDTO> dtos = list.stream().map(this::mapToDto).collect(Collectors.toList());
        logger.info("Listando calamidades (total: {})", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<?> borrarCalamidad(Long id) {
        if (!calamidadRepository.existsById(id)) {
            MDC.put("codigo_error", CodigoError.CALAMIDAD_NO_ENCONTRADA.getCodigo());
            logger.warn("Intento de borrar calamidad inexistente id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.CALAMIDAD_NO_ENCONTRADA.getDescripcion(),
                            "solucion", CodigoError.CALAMIDAD_NO_ENCONTRADA.getSolucion()));
        }
        logger.info("Eliminando calamidad id={}", id);
        calamidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> aprobarCalamidad(Long id, String usuarioActual,HttpServletRequest request) throws JsonProcessingException {
        Calamidad cal = calamidadRepository.findById(id).orElse(null);
        if (cal == null) {
            MDC.put("codigo_error", CodigoError.CALAMIDAD_NO_ENCONTRADA.getCodigo());
            logger.error("Calamidad no encontrada [calamidadId={}]", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.CALAMIDAD_NO_ENCONTRADA.getDescripcion(),
                            "solucion", CodigoError.CALAMIDAD_NO_ENCONTRADA.getSolucion()));
        }

        Calamidad copiaAntes = new Calamidad();
        BeanUtils.copyProperties(cal, copiaAntes);

        try {
            logger.info("Validando días disponibles para calamidad [numeroEmpleado={}, solicitado={}]",
                    cal.getNumeroEmpleado(), cal.getTotalDias());
            boolean valido = validacionVacacionesServiceImpl.validarDiasCalamidadDisponibles(cal);
            if (!valido) {
                MDC.put("codigo_error", CodigoError.SIN_DIAS_SUFI.getCodigo());
                logger.warn("Empleado sin días suficientes para calamidad [numeroEmpleado={}, solicitado={}]",
                        cal.getNumeroEmpleado(), cal.getTotalDias());
                MDC.remove("codigo_error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "El empleado no tiene suficientes días de calamidad disponibles."));
            }
        } catch (InterruptedException e) {
            MDC.put("codigo_error", CodigoError.VALIDACION_INTERRUP.getCodigo());
            logger.error("Validación interrumpida para calamidad [calamidadId={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al validar días disponibles (interrumpido)."));
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_VALIDACION.getCodigo());
            logger.error("Error inesperado al validar calamidad [calamidadId={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado de validación: " + e.getMessage()));
        }

        cal.setEstado(Estado.APROBADA);
        calamidadRepository.save(cal);
        logger.info("Calamidad aprobada correctamente [calamidadId={}, numeroEmpleado={}, aprobador={}]",
                id, cal.getNumeroEmpleado(), usuarioActual);

        AuditHelper.auditarCambio(
                "calamidades",
                copiaAntes,
                cal,
                cal.getId(),
                "APROBAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        notificacionCorreoServiceImpl.enviarCorreo(
                "farid.esteban.martinez.hernandez@emeal.nttdata.com",
                "Solicitud de Calamidad Aprobada",
                "Estimado " + cal.getNombreEmpleado() + ", su solicitud ha sido aprobada."
        );
        logger.info("Correo de aprobación enviado a {}", "farid.esteban.martinez.hernandez@emeal.nttdata.com");

        return ResponseEntity.ok(mapToDto(cal));
    }

    @Override
    public ResponseEntity<?> rechazarCalamidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        Calamidad cal = calamidadRepository.findById(id).orElse(null);
        if (cal == null) {
            MDC.put("codigo_error", CodigoError.CALAMIDAD_NO_ENCONTRADA.getCodigo());
            logger.error("Calamidad no encontrada [calamidadId={}]", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.CALAMIDAD_NO_ENCONTRADA.getDescripcion(),
                            "solucion", CodigoError.CALAMIDAD_NO_ENCONTRADA.getSolucion()));
        }

        Calamidad copiaAntes = new Calamidad();
        BeanUtils.copyProperties(cal, copiaAntes);

        cal.setEstado(Estado.RECHAZADA);
        calamidadRepository.save(cal);

        AuditHelper.auditarCambio(
                "calamidades",
                copiaAntes,
                cal,
                cal.getId(),
                "RECHAZAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        notificacionCorreoServiceImpl.enviarCorreo(
                "farid.esteban.martinez.hernandez@emeal.nttdata.com",
                "Solicitud de Calamidad Rechazada",
                "Estimado " + cal.getNombreEmpleado() + ", su solicitud ha sido rechazada."
        );
        logger.info("Calamidad rechazada correctamente [calamidadId={}, numeroEmpleado={}, aprobador={}]",
                id, cal.getNumeroEmpleado(), usuarioActual);

        return ResponseEntity.ok(mapToDto(cal));
    }

    @Override
    public ResponseEntity<?> listarCalamidadesPendientes() {
        List<Calamidad> todas = calamidadRepository.findAll();
        List<CalamidadRsDTO> pendientes = todas.stream()
                .filter(c -> Estado.PENDIENTE.name().equals(c.getEstado().name()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
        logger.info("Listando calamidades pendientes: {}", pendientes.size());
        return ResponseEntity.ok(pendientes);
    }

    private CalamidadRsDTO mapToDto(Calamidad cal) {
        return CalamidadRsDTO.builder()
                .id(cal.getId())
                .numeroEmpleado(cal.getNumeroEmpleado())
                .nombreEmpleado(cal.getNombreEmpleado())
                .unidadNegocio(cal.getUnidadNegocio())
                .totalDias(cal.getTotalDias())
                .estado(Estado.valueOf(cal.getEstado().name()))
                .build();
    }
}
