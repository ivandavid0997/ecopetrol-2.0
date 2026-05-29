package com.nttdata.ecopetrol.talento.services.impl;

import com.nttdata.ecopetrol.talento.model.AuditoriaSolicitud;
import com.nttdata.ecopetrol.talento.model.Usuario;
import com.nttdata.ecopetrol.talento.repository.AuditoriaSolicitudRepository;
import com.nttdata.ecopetrol.talento.services.AuditoriaService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditoriaServiceImpl implements AuditoriaService {

    private final AuditoriaSolicitudRepository auditoriaRepo;

    public AuditoriaServiceImpl(AuditoriaSolicitudRepository auditoriaRepo) {
        this.auditoriaRepo = auditoriaRepo;
    }

    @Override
    public void auditar(String tabla, Long registroId, String accion, Usuario usuario, String ipOrigen, String detalle) {
        AuditoriaSolicitud audit = new AuditoriaSolicitud();
        audit.setTablaOrigen(tabla);
        audit.setRegistroId(registroId);
        audit.setAccion(accion);
        audit.setUsuario(usuario);
        audit.setIpOrigen(ipOrigen);
        audit.setFecha(LocalDateTime.now());
        audit.setDetalle(detalle);
        auditoriaRepo.save(audit);
    }
}