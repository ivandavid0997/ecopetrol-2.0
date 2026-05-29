package com.nttdata.ecopetrol.talento.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.ecopetrol.talento.dto.request.VacacionesRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.VacacionesRsDTO;
import com.nttdata.ecopetrol.talento.enums.CodigoError;
import com.nttdata.ecopetrol.talento.enums.Estado;
import com.nttdata.ecopetrol.talento.model.Calamidad;
import com.nttdata.ecopetrol.talento.model.Usuario;
import com.nttdata.ecopetrol.talento.model.Vacaciones;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.repository.VacacionesRepository;
import com.nttdata.ecopetrol.talento.services.AuditoriaService;
import com.nttdata.ecopetrol.talento.services.VacacionesService;
import com.nttdata.ecopetrol.talento.utils.AuditHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VacacionesServiceImpl implements VacacionesService {

    private static final Logger logger = LoggerFactory.getLogger(VacacionesServiceImpl.class);


    private final VacacionesRepository vacacionesRepository;

    private final NotificacionCorreoServiceImpl notificacionCorreoServiceImpl;

    private final ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl;

    private final UsuarioRepository usuarioRepository;

    private final AuditoriaService auditoriaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public VacacionesServiceImpl(VacacionesRepository vacacionesRepository, NotificacionCorreoServiceImpl notificacionCorreoServiceImpl, ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl, UsuarioRepository usuarioRepository, AuditoriaService auditoriaService) {
        this.vacacionesRepository = vacacionesRepository;
        this.notificacionCorreoServiceImpl = notificacionCorreoServiceImpl;
        this.validacionVacacionesServiceImpl = validacionVacacionesServiceImpl;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    public ResponseEntity<?> aprobarVacaciones(Long id, String usuarioActual,HttpServletRequest request) {
        try {
            Vacaciones vac = vacacionesRepository.findById(id).orElse(null);
            if (vac == null) {
                MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
                logger.error("Vacaciones no encontradas [vacacionesId={}]", id);
                MDC.remove("codigo_error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Vacación no encontrada.");
            }

            Vacaciones copiaAntes = new Vacaciones();
            BeanUtils.copyProperties(vac, copiaAntes);

            logger.info("Validando días disponibles para empleado [numeroEmpleado={}, solicitado={}]",
                    vac.getNumeroEmpleado(), vac.getTotalDias());
            boolean valido = validacionVacacionesServiceImpl.validarDiasVacacionesDisponibles(vac);
            if (!valido) {
                MDC.put("codigo_error", CodigoError.SIN_DIAS_SUFI.getCodigo());
                logger.warn("Empleado sin días suficientes para sus vacaciones [numeroEmpleado={}, solicitado={}]",
                        vac.getNumeroEmpleado(), vac.getTotalDias());
                MDC.remove("codigo_error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error: El empleado no tiene suficientes días de vacaciones disponibles.");
            }

            vac.setEstado(Estado.APROBADA);
            vacacionesRepository.save(vac);

            AuditHelper.auditarCambio(
                    "vacaciones",
                    copiaAntes,
                    vac,
                    vac.getId(),
                    "APROBAR",
                    usuarioActual,
                    usuarioRepository,
                    auditoriaService,
                    request,
                    objectMapper
            );

            logger.info("Vacaciones aprobadas correctamente [vacacionesId={}, numeroEmpleado={}, aprobador={}]",
                    id, vac.getNumeroEmpleado(), usuarioActual);

            notificacionCorreoServiceImpl.enviarCorreo(
                    "juanpablo.guaquetaanzola@emeal.nttdata.com",
                    "Solicitud de Vacaciones Aprobada",
                    "Estimado " + vac.getNombreEmpleado() + ", su solicitud ha sido aprobada."
            );
            logger.info("Correo de aprobación enviado a {}", "juanpablo.guaquetaanzola@emeal.nttdata.com");

            return ResponseEntity.ok(mapToDto(vac));

        } catch (InterruptedException e) {
            MDC.put("codigo_error", CodigoError.VALIDACION_INTERRUP.getCodigo());
            logger.error("Validación interrumpida para vacaciones [vacacionesId={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al validar días disponibles (interrumpido).");
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_VALIDACION.getCodigo());
            logger.error("Error inesperado al validar vacaciones [vacacionesId={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado de validación: " + e.getMessage());
        }
    }

    private VacacionesRsDTO mapToDto(Vacaciones vac) {
        return VacacionesRsDTO.builder()
                .numeroEmpleado(vac.getNumeroEmpleado())
                .nombreEmpleado(vac.getNombreEmpleado())
                .unidadNegocio(vac.getUnidadNegocio())
                .fechaInicio(vac.getFechaInicio())
                .fechaFin(vac.getFechaFin())
                .totalDias(vac.getTotalDias())
                .estado(vac.getEstado() != null ? Estado.valueOf(vac.getEstado().name()) : null)
                .build();
    }

    public ResponseEntity<?> listarVacaciones() {
        List<Vacaciones> vacaciones = vacacionesRepository.findAll();
        List<VacacionesRsDTO> dtoList = vacaciones.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    public ResponseEntity<?> crearVacaciones(VacacionesRqDTO dto) {
        try {
            Vacaciones vac = new Vacaciones();
            vac.setNumeroEmpleado(dto.getNumeroEmpleado());
            vac.setNombreEmpleado(dto.getNombreEmpleado());
            vac.setUnidadNegocio(dto.getUnidadNegocio());
            vac.setFechaInicio(dto.getFechaInicio());
            vac.setFechaFin(dto.getFechaFin());
            vac.setComentario(dto.getComentario());
            vac.setTotalDias(dto.getTotalDias());
            vac.setEstado(Estado.PENDIENTE);
            vac.setFechaCreacion(new Date());
            if (dto.getLiderId() != null) {
                vac.setLider(usuarioRepository.findById(dto.getLiderId()).orElse(null));
            }

            vacacionesRepository.save(vac);
            logger.info("Solicitud de vacaciones creada para empleado {}", dto.getNumeroEmpleado());
            return ResponseEntity.ok(mapToDto(vac));
        } catch (Exception e) {
            logger.error("Error creando solicitud de vacaciones: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear vacaciones"));
        }
    }

    public ResponseEntity<?> borrarVacaciones(Long id) {
        if (!vacacionesRepository.existsById(id)) {
            MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
            logger.warn("Intento de borrar registro de vacaciones que no existe: id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Registro de vacaciones no encontrado."));
        }
        logger.info("Eliminando registro de vacaciones con id {}", id);
        vacacionesRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<?> rechazarVacaciones(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        Vacaciones vac = vacacionesRepository.findById(id).orElse(null);
        if (vac == null) {
            MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
            logger.warn("Vacaciones no encontradas para rechazo: id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Vacación no encontrada."));
        }

        Vacaciones copiaAntes = new Vacaciones();
        BeanUtils.copyProperties(vac, copiaAntes);

        vac.setEstado(Estado.RECHAZADA);
        vacacionesRepository.save(vac);

        AuditHelper.auditarCambio(
                "vacaciones",
                copiaAntes,
                vac,
                vac.getId(),
                "RECHAZAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        notificacionCorreoServiceImpl.enviarCorreo(
                "juanpablo.guaquetaanzola@emeal.nttdata.com",
                "Solicitud de Vacaciones Rechazada",
                "Estimado " + vac.getNombreEmpleado() + ", su solicitud ha sido rechazada."
        );
        logger.info("Vacaciones id={} rechazadas correctamente por {}", id, usuarioActual);
        return ResponseEntity.ok(mapToDto(vac));
    }

    public ResponseEntity<?> listarVacacionesPendientes() {
        List<Vacaciones> todas = vacacionesRepository.findAll();
        List<VacacionesRsDTO> pendientes = todas.stream()
                .filter(v -> Estado.PENDIENTE.name().equals(v.getEstado().name()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pendientes);
    }
}
