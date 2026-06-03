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
        String usuarioActual = MDC.get("usuario") != null ? MDC.get("usuario") : "desconocido";
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
            CodigoError ce = CodigoError.ERROR_CREAR_SOLICITUD;
            MDC.put("error_code", ce.getCodigo());
            logger.error("Error creando calamidad: {}", e.getMessage(), e);
            MDC.remove("error_code");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("usuario", usuarioActual, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
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
        String usuarioActual = MDC.get("usuario") != null ? MDC.get("usuario") : "desconocido";
        if (!calamidadRepository.existsById(id)) {
            CodigoError ce = CodigoError.CALAMIDAD_NO_ENCONTRADA;
            MDC.put("error_code", ce.getCodigo());
            logger.warn("Intento de borrar calamidad inexistente id={}", id);
            MDC.remove("error_code");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("usuario", usuarioActual, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }
        logger.info("Eliminando calamidad id={}", id);
        calamidadRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> aprobarCalamidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        Calamidad cal = calamidadRepository.findById(id).orElse(null);
        if (cal == null) {
            CodigoError ce = CodigoError.CALAMIDAD_NO_ENCONTRADA;
            MDC.put("error_code", ce.getCodigo());
            logger.error("Calamidad no encontrada [calamidadId={}]", id);
            MDC.remove("error_code");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("usuario", usuarioActual, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }

        Calamidad copiaAntes = new Calamidad();
        BeanUtils.copyProperties(cal, copiaAntes);

        try {
            logger.info("Validando días disponibles para calamidad [numeroEmpleado={}, solicitado={}]",
                    cal.getNumeroEmpleado(), cal.getTotalDias());
            boolean valido = validacionVacacionesServiceImpl.validarDiasCalamidadDisponibles(cal);
            if (!valido) {
                CodigoError ce = CodigoError.SIN_DIAS_SUFI;
                MDC.put("error_code", ce.getCodigo());
                logger.warn("Empleado sin días suficientes para calamidad [numeroEmpleado={}, solicitado={}]",
                        cal.getNumeroEmpleado(), cal.getTotalDias());
                MDC.remove("error_code");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("usuario", usuarioActual, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
            }
        } catch (InterruptedException e) {
            CodigoError ce = CodigoError.VALIDACION_INTERRUP;
            MDC.put("error_code", ce.getCodigo());
            logger.error("Validación interrumpida para calamidad [calamidadId={}]: {}", id, e.getMessage());
            MDC.remove("error_code");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("usuario", usuarioActual, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        } catch (Exception e) {
            CodigoError ce = CodigoError.ERROR_VALIDACION;
            MDC.put("error_code", ce.getCodigo());
            logger.error("Error inesperado al validar calamidad [calamidadId={}]: {}", id, e.getMessage());
            MDC.remove("error_code");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("usuario", usuarioActual, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }

        cal.setEstado(Estado.APROBADA);
        calamidadRepository.save(cal);
        logger.info("Calamidad aprobada [calamidadId={}, numeroEmpleado={}, aprobador={}]", id, cal.getNumeroEmpleado(), usuarioActual);

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
                "responsable@dominio.com",
                "Solicitud de Calamidad Aprobada",
                "Estimado " + cal.getNombreEmpleado() + ", su solicitud de calamidad ha sido aprobada."
        );
        logger.info("Correo de aprobación de calamidad enviado a {}", cal.getNombreEmpleado());

        return ResponseEntity.ok(mapToDto(cal));
    }

    @Override
    public ResponseEntity<?> rechazarCalamidad(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        String usuarioCtx = MDC.get("usuario") != null ? MDC.get("usuario") : usuarioActual;
        Calamidad cal = calamidadRepository.findById(id).orElse(null);
        if (cal == null) {
            CodigoError ce = CodigoError.CALAMIDAD_NO_ENCONTRADA;
            MDC.put("error_code", ce.getCodigo());
            logger.warn("Calamidad no encontrada para rechazo [calamidadId={}]", id);
            MDC.remove("error_code");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("usuario", usuarioCtx, "error_code", ce.getCodigo(), "message", ce.getDescripcion()));
        }

        Calamidad copiaAntes = new Calamidad();
        BeanUtils.copyProperties(cal, copiaAntes);

        cal.setEstado(Estado.RECHAZADA);
        calamidadRepository.save(cal);
        logger.info("Calamidad rechazada [calamidadId={}, numeroEmpleado={}, aprobador={}]", id, cal.getNumeroEmpleado(), usuarioActual);

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
                "responsable@dominio.com",
                "Solicitud de Calamidad Rechazada",
                "Estimado " + cal.getNombreEmpleado() + ", su solicitud de calamidad ha sido rechazada."
        );
        logger.info("Correo de rechazo de calamidad enviado a {}", cal.getNombreEmpleado());

        return ResponseEntity.ok(mapToDto(cal));
    }

    @Override
    public ResponseEntity<?> listarCalamidadesPendientes() {
        List<Calamidad> list = calamidadRepository.findAll();
        List<CalamidadRsDTO> pendientes = list.stream()
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
                .fechaEvento(cal.getFechaInicio() != null ? java.sql.Date.valueOf(cal.getFechaInicio()) : null)
                .totalDias(cal.getTotalDias())
                .motivo(cal.getDescripcion())
                .estado(cal.getEstado())
                .build();
    }
}
