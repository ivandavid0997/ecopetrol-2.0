package com.nttdata.ecopetrol.talento.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.ecopetrol.talento.dto.projection.VacacionesListView;
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
import com.nttdata.ecopetrol.talento.services.NotificacionCorreoService;
import com.nttdata.ecopetrol.talento.services.ValidacionVacacionesService;
import com.nttdata.ecopetrol.talento.services.VacacionesService;
import com.nttdata.ecopetrol.talento.utils.AuditHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final NotificacionCorreoService notificacionCorreoService;

    private final ValidacionVacacionesService validacionVacacionesService;

    private final UsuarioRepository usuarioRepository;

    private final AuditoriaService auditoriaService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public VacacionesServiceImpl(VacacionesRepository vacacionesRepository, NotificacionCorreoService notificacionCorreoService, ValidacionVacacionesService validacionVacacionesService, UsuarioRepository usuarioRepository, AuditoriaService auditoriaService) {
        this.vacacionesRepository = vacacionesRepository;
        this.notificacionCorreoService = notificacionCorreoService;
        this.validacionVacacionesService = validacionVacacionesService;
        this.usuarioRepository = usuarioRepository;
        this.auditoriaService = auditoriaService;
    }

    @Transactional
    public ResponseEntity<?> aprobarVacaciones(Long id, String usuarioActual, HttpServletRequest request) {
        try {
            Vacaciones vac = vacacionesRepository.findById(id).orElse(null);
            if (vac == null) {
                MDC.put("codigo_error", CodigoError.VACACIONES_NO_ENCONTRADAS.getCodigo());
                logger.error("Vacaciones no encontradas [vacacionesId={}]", id);
                MDC.remove("codigo_error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", CodigoError.VACACIONES_NO_ENCONTRADAS.getDescripcion(),
                                "solucion", CodigoError.VACACIONES_NO_ENCONTRADAS.getSolucion()));
            }

            Vacaciones copiaAntes = new Vacaciones();
            BeanUtils.copyProperties(vac, copiaAntes);

            logger.info("Validando días disponibles para empleado [numeroEmpleado={}, solicitado={}]",
                    vac.getNumeroEmpleado(), vac.getTotalDias());
            boolean valido = validacionVacacionesService.validarDiasVacacionesDisponibles(vac);
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

            notificacionCorreoService.enviarCorreo(
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

    /**
     * Mapper para operaciones que ya tienen la entidad completa cargada
     * (aprobar, rechazar, crear). Incluye liderId y liderNombre si el lider
     * ya está inicializado en la entidad.
     */
    private VacacionesRsDTO mapToDto(Vacaciones vac) {
        Long liderId = vac.getLider() != null ? vac.getLider().getId() : null;
        String liderNombre = vac.getLider() != null ? vac.getLider().getNombre() : null;
        return VacacionesRsDTO.builder()
                .id(vac.getId())
                .numeroEmpleado(vac.getNumeroEmpleado())
                .nombreEmpleado(vac.getNombreEmpleado())
                .unidadNegocio(vac.getUnidadNegocio())
                .fechaInicio(com.nttdata.ecopetrol.talento.utils.DateMapper.toDate(vac.getFechaInicio()))
                .fechaFin(com.nttdata.ecopetrol.talento.utils.DateMapper.toDate(vac.getFechaFin()))
                .totalDias(vac.getTotalDias())
                .estado(vac.getEstado() != null ? Estado.valueOf(vac.getEstado().name()) : null)
                .liderId(liderId)
                .liderNombre(liderNombre)
                .build();
    }

    /**
     * R-02: Mapper para proyecciones de listado.
     * Recibe VacacionesListView (proxy generado por Spring Data) y produce el DTO
     * sin tocar ninguna colección lazy ni columna no proyectada.
     */
    private VacacionesRsDTO mapFromProjection(VacacionesListView view) {
        Long liderId = view.getLider() != null ? view.getLider().getId() : null;
        String liderNombre = view.getLider() != null ? view.getLider().getNombre() : null;
        return VacacionesRsDTO.builder()
                .id(view.getId())
                .numeroEmpleado(view.getNumeroEmpleado())
                .nombreEmpleado(view.getNombreEmpleado())
                .unidadNegocio(view.getUnidadNegocio())
                .fechaInicio(view.getFechaInicio())
                .fechaFin(view.getFechaFin())
                .totalDias(view.getTotalDias())
                .estado(view.getEstado())
                .liderId(liderId)
                .liderNombre(liderNombre)
                .build();
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> listarVacaciones() {
        // R-02 fix: proyección anidada — Spring Data genera 1 query que selecciona SOLO
        // las columnas necesarias (v.cols + u.id + u.nombre), evitando cargar
        // password, role, activo y otras columnas del usuario lider.
        List<VacacionesRsDTO> dtoList = vacacionesRepository.findAllProjectedBy()
                .stream()
                .map(this::mapFromProjection)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @Transactional
    public ResponseEntity<?> crearVacaciones(VacacionesRqDTO dto) {
        try {
            Vacaciones vac = new Vacaciones();
            vac.setNumeroEmpleado(dto.getNumeroEmpleado());
            vac.setNombreEmpleado(dto.getNombreEmpleado());
            vac.setUnidadNegocio(dto.getUnidadNegocio());
            vac.setFechaInicio(com.nttdata.ecopetrol.talento.utils.DateMapper.toLocalDate(dto.getFechaInicio()));
            vac.setFechaFin(com.nttdata.ecopetrol.talento.utils.DateMapper.toLocalDate(dto.getFechaFin()));
            vac.setComentario(dto.getComentario());
            vac.setTotalDias(dto.getTotalDias());
            vac.setEstado(Estado.PENDIENTE);
            if (dto.getLiderId() != null) {
                vac.setLider(usuarioRepository.findById(dto.getLiderId()).orElse(null));
            }

            vacacionesRepository.save(vac);
            logger.info("Solicitud de vacaciones creada para empleado {}", dto.getNumeroEmpleado());
            return ResponseEntity.ok(mapToDto(vac));
        } catch (Exception e) {
            MDC.put("codigo_error", CodigoError.ERROR_CREAR_SOLICITUD.getCodigo());
            logger.error("Error creando solicitud de vacaciones: {}", e.getMessage(), e);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", CodigoError.ERROR_CREAR_SOLICITUD.getDescripcion(),
                            "solucion", CodigoError.ERROR_CREAR_SOLICITUD.getSolucion()));
        }
    }

    @Transactional
    public ResponseEntity<?> borrarVacaciones(Long id) {
        if (!vacacionesRepository.existsById(id)) {
            MDC.put("codigo_error", CodigoError.VACACIONES_NO_ENCONTRADAS.getCodigo());
            logger.warn("Intento de borrar registro de vacaciones que no existe: id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.VACACIONES_NO_ENCONTRADAS.getDescripcion(),
                            "solucion", CodigoError.VACACIONES_NO_ENCONTRADAS.getSolucion()));
        }
        logger.info("Eliminando registro de vacaciones con id {}", id);
        vacacionesRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    public ResponseEntity<?> rechazarVacaciones(Long id, String usuarioActual, HttpServletRequest request) throws JsonProcessingException {
        Vacaciones vac = vacacionesRepository.findById(id).orElse(null);
        if (vac == null) {
            MDC.put("codigo_error", CodigoError.VACACIONES_NO_ENCONTRADAS.getCodigo());
            logger.warn("Vacaciones no encontradas para rechazo: id={}", id);
            MDC.remove("codigo_error");
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", CodigoError.VACACIONES_NO_ENCONTRADAS.getDescripcion(),
                            "solucion", CodigoError.VACACIONES_NO_ENCONTRADAS.getSolucion()));
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

        notificacionCorreoService.enviarCorreo(
                "juanpablo.guaquetaanzola@emeal.nttdata.com",
                "Solicitud de Vacaciones Rechazada",
                "Estimado " + vac.getNombreEmpleado() + ", su solicitud ha sido rechazada."
        );
        logger.info("Vacaciones id={} rechazadas correctamente por {}", id, usuarioActual);
        return ResponseEntity.ok(mapToDto(vac));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<?> listarVacacionesPendientes() {
        // R-03 fix: filtra en BD (WHERE estado = 'PENDIENTE') en lugar de findAll() + filtro en memoria.
        // R-02 fix: proyección anidada — selecciona solo v.cols + u.id + u.nombre,
        // evitando N+1 y columnas innecesarias del usuario lider.
        List<VacacionesRsDTO> pendientes = vacacionesRepository
                .findProjectedByEstado(Estado.PENDIENTE)
                .stream()
                .map(this::mapFromProjection)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pendientes);
    }
}
