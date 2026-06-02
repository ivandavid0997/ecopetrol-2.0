package com.nttdata.ecopetrol.talento.services;

public interface NotificacionCorreoService {

    void enviarCorreo(String destinatario, String asunto, String texto);
}
