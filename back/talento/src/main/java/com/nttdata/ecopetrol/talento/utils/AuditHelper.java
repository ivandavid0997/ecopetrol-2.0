package com.nttdata.ecopetrol.talento.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nttdata.ecopetrol.talento.model.Usuario;
import com.nttdata.ecopetrol.talento.repository.UsuarioRepository;
import com.nttdata.ecopetrol.talento.services.AuditoriaService;
import jakarta.servlet.http.HttpServletRequest;

public class AuditHelper {
    public static void auditarCambio(
            String tabla,
            Object antes,
            Object despues,
            Long registroId,
            String accion,
            String usuarioActual,
            UsuarioRepository usuarioRepo,
            AuditoriaService auditoriaService,
            HttpServletRequest request,
            ObjectMapper objectMapper
    ) {
        try {
            String valorAntes = objectMapper.writeValueAsString(antes);
            String valorDespues = objectMapper.writeValueAsString(despues);

            Usuario usuario = usuarioRepo.findByUsuario(usuarioActual);
            String ipOrigen = request.getHeader("X-Forwarded-For");
            if (ipOrigen == null || ipOrigen.isEmpty()) {
                ipOrigen = request.getRemoteAddr();
            } else {
                ipOrigen = ipOrigen.split(",")[0].trim();
            }

            String detalleJson = String.format("{\"antes\": %s, \"despues\": %s}", valorAntes, valorDespues);

            auditoriaService.auditar(tabla, registroId, accion, usuario, ipOrigen, detalleJson);
        } catch (Exception e) {
            // Nunca tumba la operación, solo loguea
        }
    }
}
