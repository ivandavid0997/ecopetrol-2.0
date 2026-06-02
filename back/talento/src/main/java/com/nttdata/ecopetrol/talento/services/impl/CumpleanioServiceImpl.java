package com.nttdata.ecopetrol.talento.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.ecopetrol.talento.dto.request.CumpleanioRqDTO;
import com.nttdata.ecopetrol.talento.dto.response.CumpleanioRsDTO;
import com.nttdata.ecopetrol.talento.enums.CodigoError;
import com.nttdata.ecopetrol.talento.enums.Estado;
import com.nttdata.ecopetrol.talento.model.DiaCumpleanio;
import com.nttdata.ecopetrol.talento.repository.DiaCumpleanioRepository;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.services.AuditoriaService;
import com.nttdata.ecopetrol.talento.services.CumpleanioService;
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
public class CumpleanioServiceImpl implements CumpleanioService {

    private final DiaCumpleanioRepository cumpleanioRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionCorreoServiceImpl notificacionCorreoServiceImpl;
    private final ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl;
    private final AuditoriaService auditoriaService;
    private final ObjectMapper objectMapper;

    private static final Logger logger = LoggerFactory.getLogger(CumpleanioServiceImpl.class);

    public CumpleanioServiceImpl(DiaCumpleanioRepository cumpleanioRepository, UsuarioRepository usuarioRepository, NotificacionCorreoServiceImpl notificacionCorreoServiceImpl, ValidacionVacacionesServiceImpl validacionVacacionesServiceImpl, AuditoriaService auditoriaService, ObjectMapper objectMapper) {
        this.cumpleanioRepository = cumpleanioRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionCorreoServiceImpl = notificacionCorreoServiceImpl;
        this.validacionVacacionesServiceImpl = validacionVacacionesServiceImpl;
        this.auditoriaService = auditoriaService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ResponseEntity<?> crearCumpleanio(CumpleanioRqDTO dto) {
        try {
            DiaCumpleanio dia = new DiaCumpleanio();
            dia.setNumeroEmpleado(dto.getNumeroEmpleado());
            dia.setNombreEmpleado(dto.getNombreEmpleado());
            dia.setUnidadNegocio(dto.getUnidadNegocio());
            dia.setFechaCumpleanio(dto.getFechaCumpleanio());
            dia.setComentario(dto.getComentario());
            dia.setEstado(Estado.PENDIENTE);
            // LIDER asignado si aplica
            if(dto.getLider() != null) {
                dia.setLider(usuarioRepository.findById(dto.getLider()).orElse(null));
            }
            dia.setFechaCreacion(new Date());
            cumpleanioRepository.save(dia);

            logger.info("Día de cumpleaños creado para empleado {}", dto.getNumeroEmpleado());
            return ResponseEntity.ok(mapToDto(dia));
        } catch (Exception e) {
            logger.error("Error creando registro día cumpleaños: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error al crear registro de día cumpleaños"));
        }
    }

    @Override
    public ResponseEntity<?> listarCumpleanios() {
        List<DiaCumpleanio> list = cumpleanioRepository.findAll();
        List<CumpleanioRsDTO> dtos = list.stream().map(this::mapToDto).collect(Collectors.toList());
        logger.info("Listando días de cumpleaños (total: {})", dtos.size());
        return ResponseEntity.ok(dtos);
    }

    @Override
    public ResponseEntity<?> listarCumpleaniosPendientes() {
        List<DiaCumpleanio> list = cumpleanioRepository.findAll();
        List<CumpleanioRsDTO> pendientes = list.stream()
                .filter(d -> Estado.PENDIENTE.name().equals(d.getEstado().name()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
        logger.info("Listando días de cumpleaños pendientes: {}", pendientes.size());
        return ResponseEntity.ok(pendientes);
    }

    @Override
    public ResponseEntity<?> borrarCumpleanio(Long id) {
        if (!cumpleanioRepository.existsById(id)) {
            MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
            logger.warn("Intento de borrar cumpleaños inexistente id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Registro día cumpleaños no encontrado."));
        }
        logger.info("Eliminando registro de cumpleaños con id {}", id);
        cumpleanioRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<?> aprobarCumpleanio(Long id, String usuarioActual, HttpServletRequest request) {
        DiaCumpleanio dia = cumpleanioRepository.findById(id).orElse(null);
        if (dia == null) {
            MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
            logger.error("Día cumpleaños no encontrado [diaCumpleanioId={}]", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Día de cumpleaños no encontrado."));
        }

        DiaCumpleanio copiaAntes = new DiaCumpleanio();
        BeanUtils.copyProperties(dia, copiaAntes);

        try {
            logger.info("Validando días disponibles para día cumpleaños [numeroEmpleado={}]", dia.getNumeroEmpleado());
            boolean valido = validacionVacacionesServiceImpl.validarDiasCumpleanioDisponibles(dia);
            if (!valido) {
                MDC.put("codigo_error", CodigoError.SIN_DIAS_SUFI.getCodigo());
                logger.warn("Empleado sin días disponibles para día cumpleaños [numeroEmpleado={}]", dia.getNumeroEmpleado());
                MDC.remove("codigo_error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("error", "No tiene días de cumpleaños disponibles."));
            }
        } catch (InterruptedException e) {
            MDC.put("codigo_error", CodigoError.VALIDACION_INTERRUP.getCodigo());
            logger.error("Validación interrumpida para día cumpleaños [id={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            Thread.currentThread().interrupt();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Validación interrumpida."));
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_VALIDACION.getCodigo());
            logger.error("Error inesperado al validar día cumpleaños [id={}]: {}", id, e.getMessage());
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error inesperado de validación: " + e.getMessage()));
        }

        dia.setEstado(Estado.APROBADA);
        cumpleanioRepository.save(dia);
        logger.info("Día cumpleaños aprobado correctamente [id={}, numeroEmpleado={}, aprobador={}]", id, dia.getNumeroEmpleado(), usuarioActual);

        AuditHelper.auditarCambio(
                "dias_cumpleanios",
                copiaAntes,
                dia,
                dia.getId(),
                "APROBAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        notificacionCorreoServiceImpl.enviarCorreo(
                "responsable@dominio.com", // ajusta a la persona responsable real o calculada
                "Solicitud de Día Cumpleaños Aprobada",
                "Estimado " + dia.getNombreEmpleado() + ", su solicitud ha sido aprobada."
        );
        logger.info("Correo de aprobación enviado a {}", "responsable@dominio.com");

        return ResponseEntity.ok(mapToDto(dia));
    }

    @Override
    public ResponseEntity<?> rechazarCumpleanio(Long id, String usuarioActual, HttpServletRequest request) {
        DiaCumpleanio dia = cumpleanioRepository.findById(id).orElse(null);
        if (dia == null) {
            MDC.put("codigo_error", CodigoError.USUARIO_NO_ENCONTRADO.getCodigo());
            logger.warn("Día cumpleaños no encontrado [diaCumpleanioId={}]", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Día de cumpleaños no encontrado."));
        }

        DiaCumpleanio copiaAntes = new DiaCumpleanio();
        BeanUtils.copyProperties(dia, copiaAntes);

        dia.setEstado(Estado.RECHAZADA);
        cumpleanioRepository.save(dia);

        AuditHelper.auditarCambio(
                "dias_cumpleanios",
                copiaAntes,
                dia,
                dia.getId(),
                "RECHAZAR",
                usuarioActual,
                usuarioRepository,
                auditoriaService,
                request,
                objectMapper
        );

        notificacionCorreoServiceImpl.enviarCorreo(
                "responsable@dominio.com",
                "Solicitud de Día Cumpleaños Rechazada",
                "Estimado " + dia.getNombreEmpleado() + ", su solicitud ha sido rechazada."
        );
        logger.info("Día cumpleaños rechazada correctamente [id={}, numeroEmpleado={}, aprobador={}]", id, dia.getNumeroEmpleado(), usuarioActual);

        return ResponseEntity.ok(mapToDto(dia));
    }

    private CumpleanioRsDTO mapToDto(DiaCumpleanio dia) {
        return CumpleanioRsDTO.builder()
                .id(dia.getId())
                .numeroEmpleado(dia.getNumeroEmpleado())
                .nombreEmpleado(dia.getNombreEmpleado())
                .unidadNegocio(dia.getUnidadNegocio())
                .fechaCumpleanio(dia.getFechaCumpleanio())
                .comentario(dia.getComentario())
                .estado(dia.getEstado() != null ? dia.getEstado().name() : null)
                .lider(dia.getLider() != null ? dia.getLider().getId() : null)
                .fechaCreacion(dia.getFechaCreacion())
                .build();
    }
}
