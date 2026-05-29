package com.nttdata.ecopetrol.talento.services;

import com.nttdata.ecopetrol.talento.model.Usuario;

public interface AuditoriaService {
    void auditar(String tabla, Long registroId, String accion, Usuario usuario, String ipOrigen, String detalle);
}
